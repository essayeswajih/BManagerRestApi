package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.SteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SteService {
    private final SteRepository steRepository;
    public Ste findById(Integer id){
        return steRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Ste Not Exist")
        );
    }
    public List<Ste> findAll(){
        return steRepository.findAll();
    }
    public Ste Save (Ste ste){
        return steRepository.saveAndFlush(ste);
    }

    public void delete (Integer id){
        steRepository.deleteById(id);
    }
}
