package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FactureV {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    private double timbre = 1;
    private Double baseTVA19 = (double) 0;
    private Double baseTVA13 = (double) 0;
    private Double baseTVA7 = (double) 0;
    private Double montTVA19 = (double) 0;
    private Double montTVA13 = (double) 0;
    private Double montTVA7 = (double) 0;
    private Double total = (double) 0;
    private Double totalTTC = (double) 0;


    @OneToMany(mappedBy = "facture")
    private List<BonLivV> bonLivVS;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "facture_v_id")
    private List<Item> items;
}
