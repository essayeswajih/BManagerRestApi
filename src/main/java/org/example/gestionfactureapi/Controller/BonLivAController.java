package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.BonLivDTO;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Entity.Stock;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/bonLiv")
public class BonLivAController {
    private final BonLivAService bonLivAService;
    private final BonCmdAService bonCmdAService;
    private final FileService fileService;
    private final SteService steService;
    private final StockService stockService;
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
                        stockService.save(stock);
                    }


                }catch (Exception e){
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }

            }

            return ResponseEntity.ok(x);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody BonLivA bonLivA) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(bonLivA);
        return ResponseEntity.ok("created");
    }
}
