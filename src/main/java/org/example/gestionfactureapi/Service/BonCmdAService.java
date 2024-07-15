package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Repository.BonCmdARepository;
import org.example.gestionfactureapi.Repository.BonLivARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonCmdAService {
    private final BonCmdARepository bonCmdARepository;
    private final BonLivARepository bonLivARepository;

    public BonCmdA save(BonCmdA b){
        return bonCmdARepository.saveAndFlush(b);
    }

    public List<BonCmdA> findAll(){
        return bonCmdARepository.findAll();
    }
    public List<BonCmdA> findAllByIdSte(Integer id){
        return bonCmdARepository.findAllByIdSte(id);
    }
    public BonCmdA findById(Integer id){
        return bonCmdARepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Bon de commande n'existe pas")
        );
    }
    @Transactional
    public void delete(Integer id) {
        bonLivARepository.deleteBonLivAByBonCmdA_Id(id);
        bonCmdARepository.deleteById(id);
    }
}
