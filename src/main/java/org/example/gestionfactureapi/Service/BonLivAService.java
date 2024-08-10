package org.example.gestionfactureapi.Service;

import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.DTO.BonLivDTO;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.Ste;
import org.example.gestionfactureapi.Repository.BonLivARepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BonLivAService {
    private final BonLivARepository bonLivARepository;
    private final SteService steService;
    public List<BonLivA> findAll(){
        return bonLivARepository.findAll();
    }
    public BonLivA findBonLivAByBonCmdA(BonCmdA b1){
        return bonLivARepository.findBonLivAByBonCmdA(b1);
    }
    public List<BonLivA> findAllBySte(Ste ste){
        return bonLivARepository.findAllBySte(ste);
    }
    public BonLivA save(BonLivDTO b1){
        BonLivA bonLivA = new BonLivA();
        bonLivA.setBonCmdA(b1.getBon());
        Ste ste = steService.findById(b1.getBon().getSte().getIdSte());
        ste.setBen(ste.getBen()+1);
        steService.Save(ste);
        String newRef = adjustNumber(ste.getIdSte(),7) + adjustNumber(ste.getBen(),7).toString();
        bonLivA.setRef(newRef);
        bonLivA.setSte(ste);
        bonLivA.setSte(b1.getBon().getSte());
        bonLivA.setDateCreation(b1.getDate());
        bonLivA.setItems(b1.getItems());
        return bonLivARepository.saveAndFlush(bonLivA);
    }
    public BonLivA save(BonLivA bonLivA){
        return bonLivARepository.saveAndFlush(bonLivA);
    }
    public void delete(Integer id){
        bonLivARepository.deleteById(id);
    }

    public BonLivA saveAndFlush(BonLivA b) {
        return bonLivARepository.saveAndFlush(b);
    }
    private StringBuilder adjustNumber(Integer idSte, int i) {
        StringBuilder id = new StringBuilder(idSte.toString());
        int length = id.length();
        // Correct the loop condition to append zeros until length equals i
        while (length < i) {
            id.insert(0, "0"); // Insert zeros at the beginning
            length++;
        }
        return id;
    }
}
