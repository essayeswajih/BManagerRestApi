package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Famille {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idFamille;
    private String nomFamille;
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "depot_id")
    private Depot depot;


    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;
}
