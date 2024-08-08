package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.GetBonLiv;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.Repository.BonLivVRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonLivVService {
    private final BonLivVRepository bonLivVRepository;
    public List<BonLivV> findAll(){
        return bonLivVRepository.findAll();
    }
    public BonLivV findBonLivVByDevis(Devis devis){
        return bonLivVRepository.findBonLivVByDevis(devis);
    }
    public List<BonLivV> findAllBySte(Ste ste){
        return bonLivVRepository.findAllBySte(ste);
    }
    public BonLivV save(BonLivV b1){
        return bonLivVRepository.saveAndFlush(b1);
    }
    public void delete(Integer id){
        bonLivVRepository.deleteById(id);
    }

    public BonLivV saveAndFlush(BonLivV b) {
        return bonLivVRepository.saveAndFlush(b);
    }

    public BonLivV getById(GetBonLiv bonliv) {
        return bonLivVRepository.findByIdAndSteIdSte(bonliv.getIdSte(), bonliv.getIdBon());
    }
}
