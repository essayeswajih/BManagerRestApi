package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Bielle;
import org.example.gestionfactureapi.Repository.BielleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BielleService {
    private final BielleRepository bielleRepository;
    public Bielle findById(Integer id){
        return bielleRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Bielle Not Found Exception")
        );
    }
    public List<Bielle> findAll(){
        return bielleRepository.findAll();
    }
    public Bielle save(Bielle bielle){
        return bielleRepository.saveAndFlush(bielle);
    }
    public void delete(Integer id){
        bielleRepository.deleteById(id);
    }
}
