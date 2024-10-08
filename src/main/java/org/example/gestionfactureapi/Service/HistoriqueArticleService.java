package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.example.gestionfactureapi.Repository.HistoriqueArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoriqueArticleService {
    private final HistoriqueArticleRepository historiqueArticleRepository;
    public List<HistoriqueArticle> findByArticle(Integer id){
        return historiqueArticleRepository.findAllByArticle_IdArticleOrderByDateCreation(id);
    }
    public HistoriqueArticle save(HistoriqueArticle historiqueArticle){
        return historiqueArticleRepository.save(historiqueArticle);
    }

    public void deletAll(List<HistoriqueArticle> byArticle) {
        this.historiqueArticleRepository.deleteAll(byArticle);
    }
}
