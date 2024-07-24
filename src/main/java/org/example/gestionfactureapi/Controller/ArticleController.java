package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(articleService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(articleService.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findAllByIdSte(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok().body(articleService.findAllByIdSte(id));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Article article){
        System.out.println("id"+article.getIdArticle());
        System.out.println("designation"+article.getDesignation());
        System.out.println("Tva"+article.getTva());
        System.out.println("MontantMarge"+article.getMontantMarge());
        System.out.println("AchatHT"+article.getAchatHT());
        System.out.println("fodec"+article.getFodec());
        System.out.println("Article"+article);
        try {
            return ResponseEntity.ok().body(articleService.save(article));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<Article> articles){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articleService.saveAll(articles));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        try {
            articleService.delete(articleService.findById(id).getIdArticle());
            return ResponseEntity.ok().body("Article with id :"+id+" Deleted");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
