package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Depot {    @Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private Integer idDepot;

    private String nom;
    private String adresse;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @JsonIgnore
    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL)
    private List<Famille> familles;}
