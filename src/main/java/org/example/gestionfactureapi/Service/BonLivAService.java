package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.BonLivDTO;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.FactureA;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.BonCmdARepository;
import org.example.gestionfactureapi.Repository.BonLivARepository;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public List<BonLivA> findAllBySte(Ste ste){
        return bonLivARepository.findAllBySte(ste);
    }
    public BonLivA save(BonLivDTO b1){
        return bonLivARepository.saveAndFlush(new BonLivA(b1.getBon().getId(),b1.getBon(),b1.getBon().getSte(),b1.getDate(),null));
    }
    public void delete(Integer id){
        bonLivARepository.deleteById(id);
    }

    public BonLivA saveAndFlush(BonLivA b) {
        return bonLivARepository.saveAndFlush(b);
    }
}
