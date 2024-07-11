package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.Ste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SteRepository extends JpaRepository<Ste,Integer> {
}
