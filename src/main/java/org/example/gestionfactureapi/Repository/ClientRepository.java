package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {
    @Query("SELECT c FROM Client c WHERE c.ste.idSte = :idSte")
    List<Client> findAllByIdSte(@Param("idSte") Integer idSte);
}
