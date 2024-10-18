package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.FactureA;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.FactureARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureAService {
    private final FactureARepository factureARepository;
    public List<FactureA> findAllBySte(Ste ste){
        return factureARepository.findAllBySteOrderByDateCreation(ste);
    }
    public FactureA findById(Integer id){
        return factureARepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Facture Not Found")
        );
    }
    public void delete (Integer id){
        factureARepository.deleteById(id);
    }
    public FactureA save(FactureA f){
        return factureARepository.saveAndFlush(f);
    }

    public List<FactureA> findAll() {
        return factureARepository.findAll();
    }

    public FactureA update(FactureA factureA) {
        return factureARepository.saveAndFlush(factureA);
    }
}
