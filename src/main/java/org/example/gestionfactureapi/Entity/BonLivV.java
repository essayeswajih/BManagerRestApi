package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonLivV {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "divs_id")
    private Devis devis;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bon_liv_v_id")
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "facture__id")
    private FactureV facture;

    private Boolean trans = false;

    private String ref;
}
