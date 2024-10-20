package org.example.gestionfactureapi.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.StockDTO;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.Stock;
import org.example.gestionfactureapi.Service.ArticleService;
import org.example.gestionfactureapi.Service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {
    private final StockService stockService;
    private final ArticleService articleService;
    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            return ResponseEntity.ok(stockService.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("ste/{id}")
    public ResponseEntity<?> findAllByIdSte(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(stockService.findAllByIdSte(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(stockService.findById(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("addToQte")
    public ResponseEntity<?> addToQte(@RequestBody Stock stock){
        if(stock.getArticle().getIdArticle() == null){
            try {
                stockService.addToQteByIdStock(stock.getId(),stock.getQte());
                return ResponseEntity.ok("qtt added");
            }catch (EntityNotFoundException e){
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        }else {
            try {
                stockService.addToQteByIdArticle(stock.getArticle().getIdArticle(),stock.getQte());
                return ResponseEntity.ok("qtt added");
            }catch (Exception e1){
                return ResponseEntity.internalServerError().body(e1.getMessage());
            }
        }
    }

    @PostMapping("removeFromQte")
    public ResponseEntity<?> removeFromQte(@RequestBody Stock stock) {
        if (stock.getArticle().getIdArticle() == null) {
            try {
                stockService.addToQteByIdStock(stock.getId(), stock.getQte());
                return ResponseEntity.ok("qtt added");
            } catch (EntityNotFoundException e) {
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        } else {
            try {
                stockService.removeFromQteByIdArticle(stock.getArticle().getIdArticle(), stock.getQte());
                return ResponseEntity.ok("qtt added");
            } catch (Exception e1) {
                return ResponseEntity.internalServerError().body(e1.getMessage());
            }
        }
    }

    @PostMapping("setStockInitial")
    public ResponseEntity<?> setStockInitial(@RequestBody StockDTO stock){
        try {
            Article article = articleService.findById(stock.getIdArticle());
            return ResponseEntity.ok(stockService.save(new Stock(null,article, stock.getStockInitiale(),article.getSte())));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("saveAll")
    public ResponseEntity<?>saveAll(@RequestBody List<Stock> stocks){
        try {
            return ResponseEntity.ok(stockService.saveAll(stocks));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
