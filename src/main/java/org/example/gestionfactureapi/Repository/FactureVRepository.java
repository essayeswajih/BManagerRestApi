package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.FactureV;
import org.example.gestionfactureapi.Entity.Ste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureVRepository extends JpaRepository<FactureV,Integer> {
    public List<FactureV> findAllBySteOrderByDateCreation(Ste ste);
}
