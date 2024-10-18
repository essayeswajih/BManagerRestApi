package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.FactureV;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.FactureVRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureVService {
    private final FactureVRepository factureVRepository;
    public List<FactureV> findAllBySte(Ste ste){
        return factureVRepository.findAllBySteOrderByDateCreation(ste);
    }
    public FactureV findById(Integer id){
        return factureVRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Facture Not Found")
        );
    }
    public void delete (Integer id){
        factureVRepository.deleteById(id);
    }
    public FactureV save(FactureV f){
        return factureVRepository.saveAndFlush(f);
    }
    public List<FactureV> findAll() {
        return factureVRepository.findAll();
    }

    public FactureV update(FactureV factureV) {
        return factureVRepository.saveAndFlush(factureV);
    }
}
