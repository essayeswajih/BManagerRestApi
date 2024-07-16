package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivVRepository extends JpaRepository<BonLivV,Integer> {
    public BonLivV findBonLivVByDevis (Devis devis);
    public List<BonLivV> findAllBySte(Ste ste);

    void deleteBonLivAByDevis_Id(Integer id);
}
