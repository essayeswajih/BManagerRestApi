package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilleRepository extends JpaRepository<Famille,Integer> {
    @Query("SELECT f FROM Famille f WHERE f.ste.idSte = :idSte")
    List<Famille> findAllByIdSte(@Param("idSte") Integer idSte);
}
