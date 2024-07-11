package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Facture;
import org.example.gestionfactureapi.Service.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/facture")
@RequiredArgsConstructor
public class FactureController {
    private final FactureService factureService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(factureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(factureService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Facture facture) {
        try {
            return ResponseEntity.ok().body(factureService.save(facture));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            factureService.delete(factureService.findById(id).getIdFacture());
            return ResponseEntity.ok().body("Facture with id: '" + id + "' deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
