package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.*;

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
