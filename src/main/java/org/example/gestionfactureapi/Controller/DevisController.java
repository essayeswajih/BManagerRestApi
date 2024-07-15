package org.example.gestionfactureapi.Controller;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Devis;
import org.example.gestionfactureapi.Service.DevisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/devis")
public class DevisController {
    private final DevisService devisService;
    @GetMapping
    ResponseEntity<?> findAll(){
        try {
            return ResponseEntity.ok(devisService.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    ResponseEntity<?> findAllBySte(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(devisService.findAllByIdSte(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("article/{id}")
    public ResponseEntity<?> findAllByArticle(@PathVariable("id")Integer id){
        try {
            return ResponseEntity.ok(devisService.findAllByIdClient(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Devis devis){
        try {
            return ResponseEntity.ok(devisService.save(devis));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            devisService.delete(id);
            return ResponseEntity.ok("DEVIS DELETED");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
