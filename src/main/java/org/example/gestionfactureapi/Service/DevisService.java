package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Devis;
import org.example.gestionfactureapi.Repository.BonLivVRepository;
import org.example.gestionfactureapi.Repository.DevisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevisService {
    private final DevisRepository devisRepository;
    private final BonLivVRepository bonLivVRepository;
    public List<Devis> findAllByIdSte(Integer id){
        return devisRepository.findAllBySte_IdSteOrderByDateCreation(id);
    }
    public List<Devis> findAllByIdClient(Integer id){
        return devisRepository.findAllByClient_IdClientOrderByDateCreation(id);
    }
    public List<Devis> findAll(){
        return devisRepository.findAll();
    }
    public Devis findById(Integer id){
        return devisRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Devis Not Found")
        );
    }
    public Devis save(Devis devis){
        return devisRepository.saveAndFlush(devis);
    }
    @Transactional
    public void delete(Integer id){
        bonLivVRepository.deleteBonLivVByDevis_Id(id);
        devisRepository.deleteById(id);
    }

}
