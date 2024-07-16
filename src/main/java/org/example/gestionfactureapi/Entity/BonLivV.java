package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonLivV {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "divs_id",nullable = false)
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "facture_id")
    private FactureA facture;
}
