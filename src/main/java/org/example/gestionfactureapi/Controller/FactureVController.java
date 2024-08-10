package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/facture")
public class FactureVController {
    private final FactureVService factureVService;
    private final SteService steService;
    private final FileService fileService;
    private final BonLivVService bonLivVService;
    private final HistoriqueArticleService historiqueArticleService;
    private final StockService stockService;
    private final EmailService emailService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            return ResponseEntity.ok(factureVService.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findBySteId(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(factureVService.findAllBySte(steService.findById(id)));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            factureVService.delete(id);
            return ResponseEntity.ok("Facture Deleted");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody FactureV f){
        double baseTVA19=0;
        double baseTVA7=0;
        double baseTVA13=0;
        double montTVA19=0;
        double montTVA7=0;
        double montTVA13=0;
        double totalTH=0;
        double totalTTC=0;
        try{
            Ste ste = steService.findById(f.getSte().getIdSte());
            ste.setFen(ste.getFen()+1);
            steService.Save(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getFen(),7).toString();
            f.setRef(newRef);
            f.setSte(ste);
            FactureV sv = factureVService.save(f);
            for(BonLivV bon :sv.getBonLivVS()){
                bon.setFacture(sv);
                bon.setTrans(true);
                bonLivVService.saveAndFlush(bon);
                if (bon.getDevis() == null){
                    for(Item item :bon.getItems()){
                        int tva = item.getArticle().getTva();
                        if(tva==19){
                            montTVA19+=item.getTotalNet()*.19;
                            baseTVA19+=item.getTotalNet();
                        } else if (tva==13) {
                            montTVA13+=item.getTotalNet()*.13;
                            baseTVA13+=item.getTotalNet();
                        }else if (tva==7) {
                            montTVA7+=item.getTotalNet()*.7;
                            baseTVA7+=item.getTotalNet();
                        }
                        totalTH+=item.getTotalNet();
                    }
                }
                else {
                    for(Item item :bon.getDevis().getItems()){
                        int tva = item.getArticle().getTva();
                        if(tva==19){
                            montTVA19+=item.getTotalNet()*.19;
                            baseTVA19+=item.getTotalNet();
                        } else if (tva==13) {
                            montTVA13+=item.getTotalNet()*.13;
                            baseTVA13+=item.getTotalNet();
                        }else if (tva==7) {
                            montTVA7+=item.getTotalNet()*.7;
                            baseTVA7+=item.getTotalNet();
                        }
                        totalTH+=item.getTotalNet();
                    }
                }

            }
            totalTTC=totalTH+ baseTVA19 + baseTVA7 + baseTVA13+sv.getTimbre();
            sv.setBaseTVA7(baseTVA7);
            sv.setBaseTVA13(baseTVA13);
            sv.setBaseTVA19(baseTVA19);
            sv.setMontTVA7(montTVA7);
            sv.setMontTVA13(montTVA13);
            sv.setMontTVA19(montTVA19);
            sv.setTotal(totalTH);
            sv.setTotalTTC(totalTTC);
            FactureV res = factureVService.save(sv);
            fileService.createAndSavePDF(res);
            return ResponseEntity.ok(res);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("saveNew")
    public ResponseEntity<?> saveNew(@RequestBody FactureV f){
        double baseTVA19=0;
        double baseTVA7=0;
        double baseTVA13=0;
        double montTVA19=0;
        double montTVA7=0;
        double montTVA13=0;
        double totalTH=0;
        double totalTTC=0;
        String artcleNamesToAlert = "";
        List<String> ListOfArticlesToAlert= new ArrayList<>();
        try{
            Ste ste = steService.findById(f.getSte().getIdSte());
            ste.setFen(ste.getFen()+1);
            steService.Save(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getFen(),7).toString();
            f.setRef(newRef);
            f.setSte(ste);
            FactureV sv = factureVService.save(f);
            for(Item item :sv.getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),sv.getSte());
                Stock stock22 = stockService.findStockByIdArticle(stock.getArticle().getIdArticle());
                if(stock22!=null){
                    stock22.setQte(stock22.getQte()-stock.getQte());
                    stockService.save(stock22);
                    if(stock22.getQte()<10){
                        artcleNamesToAlert+=stock22.getArticle().getDesignation()+"\n";
                        ListOfArticlesToAlert.add(stock22.getArticle().getDesignation());
                    }
                }else {
                    stock22 = stockService.save(stock);
                }
                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);
                HistoriqueArticle ha = new HistoriqueArticle();
                ha.setId(null);
                ha.setDate(sqlDate);
                ha.setInput(0);
                ha.setOutput(item.getQte());
                ha.setArticle(item.getArticle());
                ha.setDocName("FactureVente"+sv.getId());
                ha.setDocId(sv.getId());
                ha.setPrice(item.getNewVenteHT());
                ha.setStock(stock22);
                ha.setQteReel(stock22.getQte());
                historiqueArticleService.save(ha);
                if (artcleNamesToAlert != null && !artcleNamesToAlert.isEmpty()) {
                    System.out.println(sv.getSte().getEmail());
                    System.out.println(artcleNamesToAlert);
                    if(sv.getSte().getEmail()!=null){
                        emailService.sendSimpleMessage(
                                sv.getSte().getEmail(),
                                "Stock Alert !!",
                                artcleNamesToAlert + "Stock will end soon !!!");
                    }
                }
                int tva = item.getArticle().getTva();
                if(tva==19){
                    montTVA19+=item.getTotalNet()*.19;
                    baseTVA19+=item.getTotalNet();
                } else if (tva==13) {
                    montTVA13+=item.getTotalNet()*.13;
                    baseTVA13+=item.getTotalNet();
                }else if (tva==7) {
                    montTVA7+=item.getTotalNet()*.7;
                    baseTVA7+=item.getTotalNet();
                }
                totalTH+=item.getTotalNet();
            }
            totalTTC=totalTH+ baseTVA19 + baseTVA7 + baseTVA13+sv.getTimbre();
            sv.setBaseTVA7(baseTVA7);
            sv.setBaseTVA13(baseTVA13);
            sv.setBaseTVA19(baseTVA19);
            sv.setMontTVA7(montTVA7);
            sv.setMontTVA13(montTVA13);
            sv.setMontTVA19(montTVA19);
            sv.setTotal(totalTH);
            sv.setTotalTTC(totalTTC);
            FactureV sv1 = factureVService.save(sv);
            //fileService.createAndSavePDF(res);
            HashMap<String, List<?>> respone = new HashMap<>();
            List<Object> res = new ArrayList<>();
            res.add(sv1);
            res.add(ListOfArticlesToAlert);
            return ResponseEntity.ok(res);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody FactureV factureV) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(factureV);
        return ResponseEntity.ok("created");
    }
    private StringBuilder adjustNumber(Integer idSte, int i) {
        StringBuilder id = new StringBuilder(idSte.toString());
        for(int x = id.length();x<i;i++){
            id.append("0");
        }
        return id;
    }
}
