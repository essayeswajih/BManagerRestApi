package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.example.gestionfactureapi.Entity.Stock;
import org.example.gestionfactureapi.Repository.ItemRepository;
import org.example.gestionfactureapi.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final StockService stockService;
    private final HistoriqueArticleService historiqueArticleService;
    private final ItemRepository itemRepository;
    private final FileService fileService;
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
    public ResponseEntity<String> deleteArticle(@PathVariable("id") Integer id) {
        try {
            articleService.delete(id);
            return ResponseEntity.ok().body("Article with id " + id + " deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the article: " + e.getMessage());
        }
    }
    @PostMapping("/toInventaire")
    public ResponseEntity<?> toInventaire(@RequestBody List<Article> articles){
        try {
            List<Stock> stockList =new ArrayList<>();
            for(Article a : articles){
                stockList.add(stockService.findStockByIdArticle(a.getIdArticle()));
            }
            String filename = fileService.createAndSavePDF(stockList);
            return ResponseEntity.status(HttpStatus.OK).body(filename);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
