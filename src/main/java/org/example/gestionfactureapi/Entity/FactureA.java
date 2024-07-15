package org.example.gestionfactureapi.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestionfactureapi.Entity.BonLivA;
import org.example.gestionfactureapi.Entity.Ste;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FactureA {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    private double timbre = 1;


    @OneToMany(mappedBy = "facture")

    private List<BonLivA> bonLivAS;
    // Constructors and ge
}