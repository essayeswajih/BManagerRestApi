package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonLivARepository extends JpaRepository<BonLivA,Integer> {
    public BonLivA findBonLivAByBonCmdA(BonCmdA bonCmdA);
}
