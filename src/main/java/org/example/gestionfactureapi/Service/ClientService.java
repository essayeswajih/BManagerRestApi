package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.Client;
import org.example.gestionfactureapi.Repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client findById(Integer id){
        return clientRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Client Not Found")
        );
    }
    public List<Client> findAll(){
        return clientRepository.findAll();
    }
    public Client save(Client c){
        return clientRepository.save(c);
    }
    public void delete(Integer id){
        clientRepository.deleteById(id);
    }
    public List<Client> findByIdSte (Integer id){
        return clientRepository.findAllByIdSte(id);
    }

    public List<Client> saveAll(List<Client> clients) {
        return clientRepository.saveAllAndFlush(clients);
    }
}
