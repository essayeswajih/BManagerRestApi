package org.example.gestionfactureapi.Controller;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Depot;
import org.example.gestionfactureapi.Service.DepotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/depot")
public class DepotController {
    private final DepotService depotService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(depotService.findAll());
    }
    @GetMapping("/ste/{id}")
    public ResponseEntity<?> findAllByIdSte(@PathVariable("id") Integer id){
        return ResponseEntity.ok(depotService.findByIdSte(id));
    }
    @PostMapping("/save")
    public ResponseEntity<?> save (@RequestBody Depot depot){

        return ResponseEntity.ok().body(depotService.save(depot));
    }
    @PostMapping("saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<Depot> depots){
        try {
            return ResponseEntity.ok(depotService.saveAll(depots));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity <?> delete(@PathVariable("id") Integer id){
        try{
            depotService.delete(id);
            return ResponseEntity.ok().body("deleted succ");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
