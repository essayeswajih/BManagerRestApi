package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.FactureA;
import org.example.gestionfactureapi.Entity.Ste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureARepository extends JpaRepository<FactureA ,Integer> {
    public List<FactureA> findAllBySteOrderByDateCreation(Ste ste);
}
