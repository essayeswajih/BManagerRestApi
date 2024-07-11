package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Depot;
import org.example.gestionfactureapi.Entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur,Integer> {
    @Query("SELECT f FROM Fournisseur f WHERE f.ste.idSte = :idSte")
    List<Fournisseur> findAllByIdSte(@Param("idSte") Integer idSte);
}
