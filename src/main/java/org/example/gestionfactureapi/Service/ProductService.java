package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Product;
import org.example.gestionfactureapi.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductRepository productRepository;
    public Product findById(Integer id){
        return productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product Not Found")
        );
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public Product save(Product product){
        return productRepository.saveAndFlush(product);
    }
    public void delete(Integer id){
        productRepository.deleteById(id);
    }
}
