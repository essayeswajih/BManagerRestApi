package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Famille;
import org.example.gestionfactureapi.Repository.FamilleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilleService {
    private final FamilleRepository familleRepository;

    public List<Famille> findAllBySte(Integer idSte){
        return familleRepository.findAllByIdSte(idSte);
    }
    public List<Famille> findAll(){
        return familleRepository.findAll();
    }
    public Famille save(Famille famille){
        return familleRepository.saveAndFlush(famille);
    }
    public List<Famille> saveAll (List<Famille> familles){
        return familleRepository.saveAllAndFlush(familles);
    }
    public void delete(Integer id){
        familleRepository.deleteById(id);
    }

    public Famille findById(Integer id) {
        return familleRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Famille Not Found")
        );
    }
}
