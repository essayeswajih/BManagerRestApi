package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


    @Query("""
        select t from Token t inner join User u on t.user.idUser = u.idUser
        where t.user.idUser = :userId and t.loggedOut = false""")
    List<Token> findAllAccessTokensByUser(Integer userId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token > findByRefreshToken(String token);
}