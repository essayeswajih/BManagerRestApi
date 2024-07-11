package org.example.gestionfactureapi.Controller;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Service.BonCmdAService;
import org.example.gestionfactureapi.Service.BonLivAService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/bonLiv")
public class BonLivAController {
    private final BonLivAService bonLivAService;
    private final BonCmdAService bonCmdAService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(bonLivAService.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonLivAService.findBonLivAByBonCmdA(bonCmdAService.findById(id)));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try{
            bonLivAService.delete(id);
            return ResponseEntity.ok("Deleted");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody BonCmdA b1){
        try {
            return ResponseEntity.ok(bonLivAService.save(b1));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
