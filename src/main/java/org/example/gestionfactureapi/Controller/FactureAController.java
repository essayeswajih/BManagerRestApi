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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/facture")
public class FactureAController {
    private final FactureAService factureAService;
    private final SteService steService;
    private final FileService fileService;
    private final BonLivAService bonLivAService;
    private final HistoriqueArticleService historiqueArticleService;
    private final StockService stockService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            return ResponseEntity.ok(factureAService.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findBySteId(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(factureAService.findAllBySte(steService.findById(id)));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody FactureA f){
        double baseTVA19=0;
        double baseTVA7=0;
        double baseTVA13=0;
        double totalHT=0;
        double totalTTC=0;
        try{
            Ste ste = steService.findById(f.getSte().getIdSte());
            ste.setFen(ste.getFen()+1);
            steService.Save(f.getSte());
            f.setSte(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getFen(),7).toString();
            f.setRef(newRef);
            FactureA sv = factureAService.save(f);
            for(BonLivA bon :sv.getBonLivAS()){
                bon.setFacture(sv);
                bon.setTrans(true);
                bonLivAService.saveAndFlush(bon);
                if(bon.getBonCmdA() == null){
                    for(Item item :bon.getItems()){
                        int tva = item.getArticle().getTva();
                        if(tva==19){
                            baseTVA19+=item.getNewAchatHT()*.19;
                        } else if (tva==7) {
                            baseTVA7+=item.getNewAchatHT()*.7;
                        } else if (tva==13) {
                            baseTVA13+=item.getNewAchatHT()*.13;
                        }
                        totalHT+=item.getNewAchatHT();
                    }
                }
                else{
                    for(Item item :bon.getBonCmdA().getItems()){
                        int tva = item.getArticle().getTva();
                        if(tva==19){
                            baseTVA19+=item.getNewAchatHT()*.19;
                        } else if (tva==7) {
                            baseTVA7+=item.getNewAchatHT()*.7;
                        } else if (tva==13) {
                            baseTVA13+=item.getNewAchatHT()*.13;
                        }
                        totalHT+=item.getNewAchatHT();
                    }
                }

            }
            totalTTC=totalHT+ baseTVA19 + baseTVA7 + baseTVA13+sv.getTimbre();
            sv.setBaseTVA7(baseTVA7);
            sv.setBaseTVA13(baseTVA13);
            sv.setBaseTVA19(baseTVA19);
            sv.setTotal(totalHT);
            sv.setTotalTTC(totalTTC);
            FactureA res = factureAService.save(sv);
            fileService.createAndSavePDF(res);
            return ResponseEntity.ok(res);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("saveNew")
    public ResponseEntity<?> saveNew(@RequestBody FactureA f){
        double baseTVA19=0;
        double baseTVA7=0;
        double baseTVA13=0;
        double totalHT=0;
        double totalTTC=0;
        try{
            Ste ste = steService.findById(f.getSte().getIdSte());
            ste.setFen(ste.getFen()+1);
            steService.Save(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getFen(),7).toString();
            f.setRef(newRef);
            f.setSte(ste);
            FactureA sv = factureAService.save(f);
            for(Item item :sv.getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),sv.getSte());
                Stock s = stockService.findStockByIdArticle(stock.getArticle().getIdArticle());
                if(s!=null){
                    s.setQte(s.getQte()+stock.getQte());
                    stockService.save(s);
                }else {
                    s = stockService.save(stock);
                }

                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);

                HistoriqueArticle ha = new HistoriqueArticle();
                ha.setId(null);
                ha.setDate(sqlDate);
                ha.setInput(item.getQte());
                ha.setOutput(0);
                ha.setArticle(item.getArticle());
                ha.setDocName("FactureAchat"+sv.getId());
                ha.setDocId(sv.getId());
                ha.setPrice(item.getArticle().getAchatHT());
                ha.setStock(s);
                ha.setQteReel(s.getQte());
                historiqueArticleService.save(ha);
                int tva = item.getArticle().getTva();
                if(tva==19){
                    baseTVA19+=item.getTotalNet()*.19;
                } else if (tva==7) {
                    baseTVA7+=item.getTotalNet()*.7;
                } else if (tva==13) {
                    baseTVA13+=item.getTotalNet()*.13;
                }
                totalHT+=item.getTotalNet();
            }
            totalTTC=totalHT+ baseTVA19 + baseTVA7 + baseTVA13+sv.getTimbre();
            sv.setBaseTVA7(baseTVA7);
            sv.setBaseTVA13(baseTVA13);
            sv.setBaseTVA19(baseTVA19);
            sv.setTotal(totalHT);
            sv.setTotalTTC(totalTTC);
            FactureA res = factureAService.save(sv);
            //fileService.createAndSavePDF(res);
            return ResponseEntity.ok(res);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            factureAService.delete(id);
            return ResponseEntity.ok("Facture Deleted");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody FactureA factureA) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(factureA);
        return ResponseEntity.ok("created");
    }
    private StringBuilder adjustNumber(Integer idSte, int i) {
        StringBuilder id = new StringBuilder(idSte.toString());
        int length = id.length();
        // Correct the loop condition to append zeros until length equals i
        while (length < i) {
            id.insert(0, "0"); // Insert zeros at the beginning
            length++;
        }
        return id;
    }
}
