package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.Service.*;
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
@RequestMapping("api/v1/vente/bonLiv")
public class BonLivVController {
    private final BonLivVService bonLivVService;
    private final DevisService devisService;
    private final FileService fileService;
    private final SteService steService;
    private final StockService stockService;
    private final EmailService emailService;
    private final HistoriqueArticleService historiqueArticleService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(bonLivVService.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonLivVService.findBonLivVByDevis(devisService.findById(id)));
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findByIdSte(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonLivVService.findAllBySte(steService.findById(id)));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try{
            bonLivVService.delete(id);
            return ResponseEntity.ok("Deleted");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody BonLivV b1){
        System.out.println(b1);
        try {
            b1.setSte(b1.getDevis().getSte());
            BonLivV x = bonLivVService.save(b1);
            Devis devis = x.getDevis();
            devis.setTrans(true);
            x.setDevis(devisService.save(devis));
            String artcleNamesToAlert = "";
            List<String> ListOfArticlesToAlert= new ArrayList<>();
            for (Item item:x.getDevis().getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),x.getSte());
                try {
                    Stock s = stockService.findStockByIdArticle(stock.getArticle().getIdArticle());
                    if(s!=null){
                        s.setQte(s.getQte()-stock.getQte());
                        stockService.save(s);
                        if(s.getQte()<10){
                            artcleNamesToAlert+=s.getArticle().getDesignation()+"\n";
                            ListOfArticlesToAlert.add(s.getArticle().getDesignation());
                        }
                    }else {
                        s = stockService.save(stock);
                    }
                    LocalDate localDate = LocalDate.now();
                    Date sqlDate = Date.valueOf(localDate);

                    HistoriqueArticle ha = new HistoriqueArticle();
                    ha.setId(null);
                    ha.setDate(sqlDate);
                    ha.setInput(0);
                    ha.setOutput(item.getQte());
                    ha.setArticle(item.getArticle());
                    ha.setDocName("bonLivVente"+x.getDevis().getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewVenteHT());
                    ha.setStock(s);
                    ha.setQteReel(s.getQte());
                    historiqueArticleService.save(ha);
                }catch (Exception e){
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            }

            if(artcleNamesToAlert!=""){
                System.out.println(x.getSte().getEmail());
                System.out.println(artcleNamesToAlert);
                emailService.sendSimpleMessage(
                        x.getSte().getEmail(),
                        "Stock Alert !!",
                        artcleNamesToAlert+"Stock will end soon !!!");
            }
            HashMap<String, List<?>> respone = new HashMap<>();
            List<Object> res = new ArrayList<>();
            res.add(x);
            res.add(ListOfArticlesToAlert);
            respone.put("Response",res);
            return ResponseEntity.ok(respone);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("saveNew")
    public ResponseEntity<?> saveNew(@RequestBody BonLivV b1){
        System.out.println(b1);
        try {
            BonLivV x = bonLivVService.save(b1);
            String artcleNamesToAlert = "";
            List<String> ListOfArticlesToAlert= new ArrayList<>();
            for (Item item:x.getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),x.getSte());
                try {
                    Stock s = stockService.findStockByIdArticle(stock.getArticle().getIdArticle());
                    if(s!=null){
                        s.setQte(s.getQte()-stock.getQte());
                        stockService.save(s);
                        if(s.getQte()<10){
                            artcleNamesToAlert+=s.getArticle().getDesignation()+"\n";
                            ListOfArticlesToAlert.add(s.getArticle().getDesignation());
                        }
                    }else {
                        s = stockService.save(stock);
                    }
                    LocalDate localDate = LocalDate.now();
                    Date sqlDate = Date.valueOf(localDate);

                    HistoriqueArticle ha = new HistoriqueArticle();
                    ha.setId(null);
                    ha.setDate(sqlDate);
                    ha.setInput(0);
                    ha.setOutput(item.getQte());
                    ha.setArticle(item.getArticle());
                    ha.setDocName("bonLivVente"+x.getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewVenteHT());
                    ha.setStock(s);
                    ha.setQteReel(s.getQte());
                    historiqueArticleService.save(ha);
                }catch (Exception e){
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            }

            if(artcleNamesToAlert!=""){
                System.out.println(x.getSte().getEmail());
                System.out.println(artcleNamesToAlert);
                emailService.sendSimpleMessage(
                        x.getSte().getEmail(),
                        "Stock Alert !!",
                        artcleNamesToAlert+"Stock will end soon !!!");
            }
            HashMap<String, List<?>> respone = new HashMap<>();
            List<Object> res = new ArrayList<>();
            res.add(x);
            res.add(ListOfArticlesToAlert);
            respone.put("Response",res);
            return ResponseEntity.ok(respone);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody BonLivV bonLivV) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(bonLivV);
        return ResponseEntity.ok("created");
    }
}
