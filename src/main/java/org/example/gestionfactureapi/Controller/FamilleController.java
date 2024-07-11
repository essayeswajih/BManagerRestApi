package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Famille;
import org.example.gestionfactureapi.Service.FamilleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/famille")
@CrossOrigin(origins = "http://localhost:4200")
public class FamilleController {
    private final FamilleService familleService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(familleService.findAll());
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findAllBySte(@PathVariable("id") Integer id){
        return ResponseEntity.ok(familleService.findAllBySte(id));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(familleService.findById(id));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            familleService.delete(id);
            return ResponseEntity.ok().body("Famille Deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Famille famille){
        try {
            return ResponseEntity.ok(familleService.save(famille));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<Famille> familles){
        try {
            return ResponseEntity.ok(familleService.saveAll(familles));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
