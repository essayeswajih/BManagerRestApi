package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.BonCmdADTO;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.FileRepository;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achat/bonCmd")
public class BonCmdAController {
    private final BonCmdAService bonCmdAService;
    private final FournisseurService fournisseurService;
    private final SteService steService;
    private final ArticleService articleService;
    private final ItemService itemService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(bonCmdAService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonCmdAService.findById(id));
    }
    @GetMapping("/ste/{id}")
    public ResponseEntity<?> findAllByIdSte(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bonCmdAService.findAllByIdSte(id));
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BonCmdADTO b) {
        if (b == null || b.getFournisseur() == null || b.getSte() == null || b.getItems() == null || b.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        BonCmdA b1 = new BonCmdA();
        b1.setDateCreation(b.getDateCreation());

        try {
            b1.setFournisseur(fournisseurService.findById(b.getFournisseur().getIdFournisseur()));
            Ste ste = steService.findById(b.getSte().getIdSte());
            ste.setBcn(ste.getBcn()+1);
            steService.Save(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getBcn(),7).toString();
            b1.setRef(newRef);
            b1.setSte(ste);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        List<Item> items = new ArrayList<>();
        for (Item i : b.getItems()) {
            try {
                Article a = articleService.findById(i.getArticle().getIdArticle());
                i.setArticle(a);
                items.add(itemService.save(i));
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        b1.setItems(items);

        BonCmdA savedBonCmdA = bonCmdAService.save(b1);
        return ResponseEntity.ok(savedBonCmdA);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            bonCmdAService.delete(id);
            return ResponseEntity.ok("Deleted");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(value = "toPdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toPdF(@RequestBody BonCmdA bon) throws DocumentException, IOException, URISyntaxException {

        fileService.createAndSavePDF(bon);
        return ResponseEntity.ok("created");
    }
    private StringBuilder adjustNumber(Integer idSte, int i) {
        StringBuilder id = new StringBuilder(idSte.toString());
        int length = id.length();
        // Correct the loop condition to append zeros until length equals i
        while (length < i) {
            id.insert(0, "0"); // Insert zeros at the beginning
            length++;
        }
        return id;
    }

}
