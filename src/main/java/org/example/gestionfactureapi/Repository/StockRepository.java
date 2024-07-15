package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock,Integer> {
    public List<Stock> findAllBySte_IdSteOrderByArticle(Integer id);
    public Stock findStockByArticle_IdArticle(Integer id);
}
