package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
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
    public void delete(Integer id){
        articleRepository.deleteById(id);
    }

    public List<Article> findAllByIdSte(Integer id) {
        return articleRepository.findAllByIdSte(id);
    }

    public List<Article> saveAll(List<Article> articles) {
        return articleRepository.saveAllAndFlush(articles);
    }
}
