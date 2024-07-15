package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Devis;
import org.example.gestionfactureapi.Repository.DevisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevisService {
    private final DevisRepository devisRepository;
    List<Devis> findAllByIdSte(Integer id){
        return devisRepository.findAllBySte_IdSteOrderByDateCreation(id);
    }
    List<Devis> findAllByIdClient(Integer id){
        return devisRepository.findAllByClient_IdClientOrderByDateCreation(id);
    }
    List<Devis> findAll(){
        return devisRepository.findAll();
    }
    Devis findById(Integer id){
        return devisRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Devis Not Found")
        );
    }
    Devis save(Devis devis){
        return devisRepository.saveAndFlush(devis);
    }
    void delete(Integer id){
        devisRepository.deleteById(id);
    }

}
