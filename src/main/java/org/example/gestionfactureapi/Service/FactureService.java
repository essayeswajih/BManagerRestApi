package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Facture;
import org.example.gestionfactureapi.Repository.FactureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureService {
    private final FactureRepository factureRepository;
    public Facture findById(Integer id){
        return factureRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Facture Not Found")
        );
    }
    public List<Facture> findAll(){
        return factureRepository.findAll();
    }
    public Facture save(Facture facture){
        return factureRepository.saveAndFlush(facture);
    }
    public void delete(Integer id){
        factureRepository.deleteById(id);
    }
}
