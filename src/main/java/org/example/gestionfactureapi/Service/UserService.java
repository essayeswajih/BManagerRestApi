package org.example.gestionfactureapi.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.User;
import org.example.gestionfactureapi.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User findById(Integer id){
        return userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("User Not Found")
        );
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User save(User u){
        return userRepository.saveAndFlush(u);
    }
    public void delete(Integer id){
        userRepository.deleteById(id);
    }
}
