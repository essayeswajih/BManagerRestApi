package org.example.gestionfactureapi.Repository;

import org.example.gestionfactureapi.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String filename);
}
