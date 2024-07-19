package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.FactureA;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Service.BonLivAService;
import org.example.gestionfactureapi.Service.FactureAService;
import org.example.gestionfactureapi.Service.FileService;
import org.example.gestionfactureapi.Service.SteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/facture")
public class FactureAController {
    private final FactureAService factureAService;
    private final SteService steService;
    private final FileService fileService;
    private final BonLivAService bonLivAService;

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
            FactureA sv = factureAService.save(f);
            for(BonLivA bon :sv.getBonLivAS()){
                bon.setFacture(sv);
                bonLivAService.saveAndFlush(bon);
                for(Item item :bon.getBonCmdA().getItems()){
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
}
