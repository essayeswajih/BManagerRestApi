package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Repository.BonCmdARepository;
import org.example.gestionfactureapi.Repository.BonLivARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonLivAService {
    private final BonLivARepository bonLivARepository;
    public List<BonLivA> findAll(){
        return bonLivARepository.findAll();
    }
    public BonLivA findBonLivAByBonCmdA(BonCmdA b1){
        return bonLivARepository.findBonLivAByBonCmdA(b1);
    }
    public BonLivA save(BonCmdA b1){
        return bonLivARepository.saveAndFlush(new BonLivA(b1.getId(),b1));
    }
    public void delete(Integer id){
        bonLivARepository.deleteById(id);
    }
}
