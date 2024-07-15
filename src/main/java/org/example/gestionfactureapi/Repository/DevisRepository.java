package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevisRepository extends JpaRepository<Devis,Integer> {
    List<Devis> findAllBySte_IdSteOrderByDateCreation(Integer id);
    List<Devis> findAllByClient_IdClientOrderByDateCreation(Integer Id);
}
