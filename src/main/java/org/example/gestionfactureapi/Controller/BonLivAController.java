package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.BonLivDTO;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/bonLiv")
public class BonLivAController {
    private final BonLivAService bonLivAService;
    private final BonCmdAService bonCmdAService;
    private final FileService fileService;
    private final SteService steService;
    private final StockService stockService;
    private final HistoriqueArticleService historiqueArticleService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(bonLivAService.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonLivAService.findBonLivAByBonCmdA(bonCmdAService.findById(id)));
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findByIdSte(@PathVariable("id") Integer id){

        return ResponseEntity.ok(bonLivAService.findAllBySte(steService.findById(id)));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try{
            bonLivAService.delete(id);
            return ResponseEntity.ok("Deleted");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody BonLivDTO b1){
        try {
            var x = bonLivAService.save(b1);
            BonCmdA bb = x.getBonCmdA();
            bb.setTrans(true);
            x.setBonCmdA(bonCmdAService.save(bb));
            for (Item item:x.getBonCmdA().getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),x.getSte());
                try {
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
                    ha.setDocName("bonLivAchat"+x.getBonCmdA().getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewAchatHT());
                    ha.setStock(s);
                    ha.setQteReel(s.getQte());
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
    @PostMapping("saveNew")
    public ResponseEntity<?> saveNew(@RequestBody BonLivA b1){
        try {
            Ste ste = steService.findById(b1.getSte().getIdSte());
            ste.setBen(ste.getBen()+1);
            steService.Save(ste);
            String newRef = adjustNumber(ste.getIdSte(),7).toString() + adjustNumber(ste.getBen(),7).toString();
            b1.setRef(newRef);
            b1.setSte(ste);
            var x = bonLivAService.save(b1);
            for (Item item:x.getItems()){
                Stock stock = new Stock(null,item.getArticle(),item.getQte(),x.getSte());
                try {
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
                    ha.setDocName("bonLivAchat"+x.getId());
                    ha.setDocId(x.getId());
                    ha.setPrice(item.getNewAchatHT());
                    ha.setStock(s);
                    ha.setQteReel(s.getQte());
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
    @PostMapping(value = "toPdf")
    public ResponseEntity<?> toPdF(@RequestBody BonLivA bonLivA) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(bonLivA);
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
