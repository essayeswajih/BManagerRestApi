package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivVRepository extends JpaRepository<BonLivV,Integer> {
    public BonLivV findBonLivVByDevis (Devis devis);
    public List<BonLivV> findAllBySte(Ste ste);
    void deleteBonLivVByDevis_Id(Integer id);
    public BonLivV findByIdAndSteIdSte(Integer idSte,Integer id);
}
