package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.Ste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivARepository extends JpaRepository<BonLivA,Integer> {
    public BonLivA findBonLivAByBonCmdA(BonCmdA bonCmdA);
    public List<BonLivA> findAllBySte(Ste ste);

    void deleteBonLivAByBonCmdA_Id(Integer id);
}
