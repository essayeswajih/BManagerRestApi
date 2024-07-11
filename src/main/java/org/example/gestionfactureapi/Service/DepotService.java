package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Depot;
import org.example.gestionfactureapi.Repository.DepotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepotService {
    private final DepotRepository depotRepository;
    public List<Depot> findAll(){
        return depotRepository.findAll();
    }
    public List<Depot> findByIdSte(Integer id){
        return depotRepository.findAllByIdSte(id);
    }
    public Depot findById(Integer id){
        return depotRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Depot Not Found")
        );
    }
    public Depot save(Depot d){
        return depotRepository.saveAndFlush(d);
    }
    public void delete(Integer id){
        depotRepository.deleteById(id);
    }

    public List<Depot> saveAll(List<Depot> depots){
        return depotRepository.saveAllAndFlush(depots);
    }
}

