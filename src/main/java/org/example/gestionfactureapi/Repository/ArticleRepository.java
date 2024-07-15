package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    @Query("SELECT a FROM Article a WHERE a.ste.idSte = :idSte")
    List<Article> findAllByIdSte(@Param("idSte") Integer idSte);

}
