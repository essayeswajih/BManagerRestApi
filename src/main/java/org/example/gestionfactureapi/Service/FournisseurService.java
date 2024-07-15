package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Fournisseur;
import org.example.gestionfactureapi.Repository.FournisseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    @Transactional(readOnly = true)
    public Fournisseur findById(Integer id) {
        return fournisseurRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Fournisseur Not Found")
        );
    }

    public List<Fournisseur> findByIdSte(Integer id){
        return fournisseurRepository.findAllByIdSte(id);
    }

    @Transactional(readOnly = true)
    public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }

    @Transactional
    public Fournisseur save(Fournisseur fournisseur) {
        return fournisseurRepository.saveAndFlush(fournisseur);
    }

    @Transactional
    public void delete(Integer id) {
        fournisseurRepository.deleteById(id);
    }

    public List<Fournisseur> saveAll(List<Fournisseur> fournisseurs) {
        return fournisseurRepository.saveAllAndFlush(fournisseurs);
    }
}
