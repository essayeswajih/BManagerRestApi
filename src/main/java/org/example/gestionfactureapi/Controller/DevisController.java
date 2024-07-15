package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.DevisDTO;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.Devis;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Repository.ItemRepository;
import org.example.gestionfactureapi.Service.DevisService;
import org.example.gestionfactureapi.Service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/devis")
public class DevisController {
    private final DevisService devisService;
    private final ItemRepository itemRepository;
    private final FileService fileService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            return ResponseEntity.ok(devisService.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findAllBySte(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(devisService.findAllByIdSte(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("client/{id}")
    public ResponseEntity<?> findAllByArticle(@PathVariable("id")Integer id){
        try {
            return ResponseEntity.ok(devisService.findAllByIdClient(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Devis devis){
        System.out.println(devis.getClient().getIdClient());
        try {
            System.out.println(devis);
            Devis newDevis = new Devis(null,devis.getClient(),null,devis.getDateCreation(),devis.getSte(),false);
            newDevis = devisService.save(newDevis);
            List<Item> newItems = itemRepository.saveAllAndFlush(devis.getItems());
            newDevis.setItems(newItems);

            //devisService.save(devis);
            return ResponseEntity.ok(devisService.save(newDevis));
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
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody Devis devis) throws DocumentException, IOException, URISyntaxException {
        fileService.createAndSavePDF(devis);
        return ResponseEntity.ok("created");
    }
}
