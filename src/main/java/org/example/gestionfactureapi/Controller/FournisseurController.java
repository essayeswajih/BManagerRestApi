package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Depot;
import org.example.gestionfactureapi.Entity.Fournisseur;
import org.example.gestionfactureapi.Service.FournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fournisseur")
@RequiredArgsConstructor
public class FournisseurController {
    private final FournisseurService fournisseurService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(fournisseurService.findAll());
    }
    @GetMapping("/ste/{id}")
    public ResponseEntity<?> findAllByIdSte(@PathVariable("id") Integer id){
        return ResponseEntity.ok(fournisseurService.findByIdSte(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok().body(fournisseurService.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Fournisseur fournisseur){
        try {
            return ResponseEntity.ok().body(fournisseurService.save(fournisseur));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<Fournisseur> fournisseurs){
        try {
            return ResponseEntity.ok(fournisseurService.saveAll(fournisseurs));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            fournisseurService.delete(fournisseurService.findById(id).getIdFournisseur());
            return ResponseEntity.ok().body("Fournisseur with id "+id+" deleted");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
