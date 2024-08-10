package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.Repository.ItemRepository;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vente/devis")
public class DevisController {
    private final DevisService devisService;
    private final ItemRepository itemRepository;
    private final FileService fileService;
    private final ClientService clientService;
    private final SteService steService;
    private final ArticleService articleService;
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
        try {

            Client client = clientService.findById(devis.getClient().getIdClient());
            Ste ste = steService.findById(devis.getSte().getIdSte());
            ste.setDn(ste.getDn()+1);
            steService.Save(ste);
            devis.setSte(ste);
            String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getDn(),7).toString();
            devis.setRef(newRef);
            List<Item> newItems = itemRepository.saveAllAndFlush(devis.getItems());
            Devis newDevis = new Devis(null, client, newItems, devis.getDateCreation(), ste, false, devis.getRef());
            newDevis = devisService.save(newDevis);

            for(Item i:newItems){
                Article a = articleService.findById(i.getArticle().getIdArticle());
                i.setArticle(a);
            }

            toPdF(newDevis);
            return ResponseEntity.ok(newDevis);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private StringBuilder adjustNumber(Integer idSte, int i) {
        StringBuilder id = new StringBuilder(idSte.toString());
        for(int x = id.length();x<i;i++){
            id.append("0");
        }
        return id;
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
