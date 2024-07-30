package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Repository.ArticleRepository;
import org.example.gestionfactureapi.Repository.HistoriqueArticleRepository;
import org.example.gestionfactureapi.Repository.ItemRepository;
import org.example.gestionfactureapi.Repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ItemRepository itemRepository;
    private final HistoriqueArticleRepository historiqueArticleRepository;
    private final StockRepository stockRepository;
    public Article findById(Integer id){
        return articleRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Article Not Found")
        );
    }
    public List<Article> findAll(){
        return articleRepository.findAll();
    }
    public Article save(Article article){
        return articleRepository.saveAndFlush(article);
    }
    @Transactional
    public void delete(Integer id) {
        // Check if the article exists
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found for id: " + id));

        // Delete related historiqueArticles
        historiqueArticleRepository.deleteByArticleId(id);

        // Check and delete items if necessary
        itemRepository.deleteByArticleId(id);

        stockRepository.deleteByArticleId(id);
        // Delete the article itself
        articleRepository.delete(article);
    }

    public List<Article> findAllByIdSte(Integer id) {
        return articleRepository.findAllByIdSte(id);
    }

    public List<Article> saveAll(List<Article> articles) {
        return articleRepository.saveAllAndFlush(articles);
    }
}
