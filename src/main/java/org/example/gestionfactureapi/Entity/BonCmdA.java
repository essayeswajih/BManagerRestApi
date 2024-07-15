package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BonCmdA {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bon_cmd_a_id")
    private List<Item> items;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @Column(nullable = false)
    private Boolean trans = false;

}

