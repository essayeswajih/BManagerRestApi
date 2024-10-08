package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.HistoriqueArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriqueArticleRepository extends JpaRepository<HistoriqueArticle,Integer> {
    List<HistoriqueArticle> findAllByArticle_IdArticleOrderByDateCreation(Integer id);

    void deleteAllByArticle_IdArticle(Integer id);
}
