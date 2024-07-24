package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoriqueArticleRepository extends JpaRepository<HistoriqueArticle,Integer> {
    HistoriqueArticle findByArticle_IdArticle(Integer id);
}
