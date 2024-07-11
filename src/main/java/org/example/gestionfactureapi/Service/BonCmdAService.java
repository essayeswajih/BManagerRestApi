package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Repository.BonCmdARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonCmdAService {
    private final BonCmdARepository bonCmdARepository;

    public BonCmdA save(BonCmdA b){
        return bonCmdARepository.saveAndFlush(b);
    }
    public void delete(Integer id){
        bonCmdARepository.deleteById(id);
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
}
