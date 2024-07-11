package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Integer> {
}
