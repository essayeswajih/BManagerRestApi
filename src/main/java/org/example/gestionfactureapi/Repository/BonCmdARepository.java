package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonCmdARepository extends JpaRepository<BonCmdA,Integer> {
    @Query("SELECT b FROM BonCmdA b WHERE b.ste.idSte = :idSte ORDER BY b.dateCreation")
    List<BonCmdA> findAllByIdSte(@Param("idSte") Integer idSte);
}
