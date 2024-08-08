package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.GetBonLiv;
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
            b1.setSte(steService.findById(b1.getSte().getIdSte()));
            BonLivV x = bonLivVService.saveAndFlush(b1);
            String artcleNamesToAlert = "";
            List<String> ListOfArticlesToAlert= new ArrayList<>();
            for (Item item:x.getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),x.getSte());
                try {
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
                    ha.setDocName("bonLivVente"+x.getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewVenteHT());
                    ha.setStock(stock22);
                    ha.setQteReel(stock22.getQte());
                    historiqueArticleService.save(ha);
                }catch (Exception e){
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            }

            if (artcleNamesToAlert != null && !artcleNamesToAlert.isEmpty()) {
                System.out.println(x.getSte().getEmail());
                System.out.println(artcleNamesToAlert);
                if(x.getSte().getEmail()!=null){
                    emailService.sendSimpleMessage(
                            x.getSte().getEmail(),
                            "Stock Alert !!",
                            artcleNamesToAlert + "Stock will end soon !!!");
                }
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
    @PostMapping("saveBonRetour")
    public ResponseEntity<?> saveBonRetour(@RequestBody BonLivV b1){
        System.out.println(b1);
        try {
            BonLivV last = bonLivVService.findById(b1.getId());
            BonLivV x =b1;
            for (Item item:x.getItems()){
                int qte =0;
                for(Item item2:last.getItems()){
                    if(item.getArticle().getIdArticle()==item2.getArticle().getIdArticle()){
                        qte = item2.getQte()-item.getQte();
                    }
                }
                Stock stock = new Stock(null,item.getArticle(),qte,x.getSte()); //5555
                try {
                    Stock stock22 = stockService.findStockByIdArticle(stock.getArticle().getIdArticle());
                    if(stock22!=null){
                        stock22.setQte(stock22.getQte()+stock.getQte()); // ssss
                        stockService.save(stock22);
                    }else {
                        stock22 = stockService.save(stock);
                    }
                    LocalDate localDate = LocalDate.now();
                    Date sqlDate = Date.valueOf(localDate);

                    HistoriqueArticle ha = new HistoriqueArticle();
                    ha.setId(null);
                    ha.setDate(sqlDate);
                    ha.setInput(qte);
                    ha.setOutput(0);
                    ha.setArticle(item.getArticle());
                    ha.setDocName("bonLivRetour"+x.getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewVenteHT());
                    ha.setStock(stock22);
                    ha.setQteReel(stock22.getQte());
                    historiqueArticleService.save(ha);
                }catch (Exception e){
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            }

            return ResponseEntity.ok(x);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("getById")
    public ResponseEntity<?> getById(@RequestBody GetBonLiv bonliv){
        try {
            return ResponseEntity.ok(bonLivVService.getById(bonliv));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf")
    public ResponseEntity<?> toPdF(@RequestBody BonLivV bonLivV) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(bonLivV);
        return ResponseEntity.ok("created");
    }
}
