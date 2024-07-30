package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.example.gestionfactureapi.Service.ArticleService;
import org.example.gestionfactureapi.Service.HistoriqueArticleService;
import org.example.gestionfactureapi.Service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final StockService stockService;
    private final HistoriqueArticleService historiqueArticleService;
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
            historiqueArticleService.deletAll(historiqueArticleService.findByArticle(id));
            stockService.delete(stockService.findStockByIdArticle(id).getId());
            articleService.delete(articleService.findById(id).getIdArticle());
            return ResponseEntity.ok().body("Article with id :"+id+" Deleted");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
