package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Stock;
import org.example.gestionfactureapi.Repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    public Stock findStockByIdArticle(Integer idArticle){
        return stockRepository.findStockByArticle_IdArticle(idArticle);
    }
    public List<Stock> findAllByIdSte(Integer idSte){
        return stockRepository.findAllBySte_IdSteOrderByArticle(idSte);
    }
    public Stock findById(Integer id){
        return stockRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Stock Not Found")
        );
    }
    public Stock save(Stock s){
        return  stockRepository.saveAndFlush(s);
    }
    public void delete(Integer idStock){
        stockRepository.deleteById(idStock);
    }
    public void addToQteByIdArticle (Integer idArticle,Integer qteToAdd){
        Stock stock =  stockRepository.findStockByArticle_IdArticle(idArticle);
        stock.setQte(stock.getQte()+qteToAdd);
        save(stock);
    }
    public void addToQteByIdStock (Integer idStock,Integer qteToAdd){
        Stock stock =  findById(idStock);
        stock.setQte(stock.getQte()+qteToAdd);
        save(stock);
    }
    public void removeFromQteByIdArticle (Integer idArticle,Integer qteToAdd){
        Stock stock =  stockRepository.findStockByArticle_IdArticle(idArticle);
        stock.setQte(stock.getQte()-qteToAdd);
        save(stock);
    }
    public void removeFromQteByIdStock (Integer idStock,Integer qteToAdd){
        Stock stock =  findById(idStock);
        stock.setQte(stock.getQte()-qteToAdd);
        save(stock);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public List<Stock> saveAll(List<Stock> stocks) {
        return stockRepository.saveAll(stocks);
    }
}
