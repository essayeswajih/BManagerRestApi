package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonLivV;
import org.example.gestionfactureapi.Entity.FactureV;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/facture")
public class FactureVController {
    private final FactureVService factureVService;
    private final SteService steService;
    private final FileService fileService;
    private final BonLivVService bonLivVService;
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
            FactureV sv = factureVService.save(f);
            for(BonLivV bon :sv.getBonLivVS()){
                bon.setFacture(sv);
                bonLivVService.saveAndFlush(bon);
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
        try{
            FactureV sv = factureVService.save(f);
            for(BonLivV bon :sv.getBonLivVS()){
                bon.setFacture(sv);
                bonLivVService.saveAndFlush(bon);
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
            //fileService.createAndSavePDF(res);
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
}
