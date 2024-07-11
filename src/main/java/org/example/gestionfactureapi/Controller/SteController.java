package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Service.SteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ste")
@RequiredArgsConstructor
public class SteController {
    private final SteService steService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(steService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(steService.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Ste ste){
        try {
            return ResponseEntity.ok().body(steService.Save(ste));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            steService.delete(steService.findById(id).getIdSte());
            return ResponseEntity.ok().body("Ste with id:'"+id+"' deleted");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
