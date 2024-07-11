package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Bielle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BielleRepository extends JpaRepository<Bielle,Integer> {
}
