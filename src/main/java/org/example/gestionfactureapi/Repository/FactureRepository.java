package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Integer> {
}
