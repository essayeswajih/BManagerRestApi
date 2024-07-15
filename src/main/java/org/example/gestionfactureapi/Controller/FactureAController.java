package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.FactureA;
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
import java.util.List;

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
            return ResponseEntity.ok(steService.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody FactureA f){
        System.out.println(f);
        try{
            FactureA sv = factureAService.save(f);
            for(BonLivA bon :sv.getBonLivAS()){
                bon.setFacture(sv);
                bonLivAService.saveAndFlush(bon);
            }
            FactureA res = factureAService.save(sv);
            this.toPdF(res);
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
