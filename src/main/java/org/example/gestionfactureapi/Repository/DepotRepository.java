package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface DepotRepository extends JpaRepository<Depot,Integer> {
    @Query("SELECT d FROM Depot d WHERE d.ste.idSte = :idSte")
    List<Depot> findAllByIdSte(@Param("idSte") Integer idSte);
}
