package org.example.gestionfactureapi.Controller;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Service.HistoriqueArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/historiqueArticle")
public class HistoriqueArticleController {
    private final HistoriqueArticleService historiqueArticleService;
    @GetMapping("/{idArticle}")
    public ResponseEntity<?> findByIdArticle(@PathVariable("idArticle") Integer id){
        try {
            return ResponseEntity.ok(historiqueArticleService.findByArticle(id));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
