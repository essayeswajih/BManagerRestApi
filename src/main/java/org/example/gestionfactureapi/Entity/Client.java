package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idClient;
    private String name;
    private String tel;
    private String matriculeFiscale;
    private String adresse;
    private String fax;
    private String email;
    private boolean exonere;
    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;
}