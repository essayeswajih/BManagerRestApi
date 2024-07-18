package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonLivV;
import org.example.gestionfactureapi.Entity.FactureV;
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
        System.out.println(f);
        try{
            FactureV sv = factureVService.save(f);
            for(BonLivV bon :sv.getBonLivVS()){
                System.out.println(bon);
                bon.setFacture(sv);
                bonLivVService.saveAndFlush(bon);
            }
            FactureV res = factureVService.save(sv);
            this.toPdF(res);
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
