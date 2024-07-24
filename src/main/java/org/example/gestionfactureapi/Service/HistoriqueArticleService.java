package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.example.gestionfactureapi.Repository.HistoriqueArticleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoriqueArticleService {
    private final HistoriqueArticleRepository historiqueArticleRepository;
    HistoriqueArticle findByArticle(Integer id){
        return historiqueArticleRepository.findByArticle_IdArticle(id);
    }
    HistoriqueArticle save(HistoriqueArticle historiqueArticle){
        return historiqueArticleRepository.save(historiqueArticle);
    }
}
