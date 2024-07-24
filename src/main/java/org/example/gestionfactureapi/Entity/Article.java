package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"ste"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idArticle;


    @Column(unique = true,nullable = false)
    private String refArticle;

    @Column(unique = true,nullable = false)
    private String refFournisseur;

    @NotNull
    @Column(nullable = false)
    private String designation;
    private String model;

    @Min(0)
    private Double achatHT;
    private Double marge;
    private Double montantMarge;

    private Double venteHT;

    private Boolean fodec;
    private Integer tva;
    private Double timbre;

    private Double venteTTC;
    private String unite;
    private String devise;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<HistoriqueArticle> historiqueArticles;

}
