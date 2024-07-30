package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    void deleteAllByArticle_IdArticle(Integer id);

    void deleteByArticleId(Integer id);
}
