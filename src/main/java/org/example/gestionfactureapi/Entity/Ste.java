package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ste {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idSte;
    @NonNull
    @Column(nullable = false)
    private String name;
    private String tel;
    private String adresse;
    private String fax;
    private String cachet;
    private String logoUrl;
    @Email
    private String email;
    private String formeJuridique;
    private String matriculeFiscale;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fournisseur> fournisseurs;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Depot> depots;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Famille> famille;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients;
    @JsonIgnore
    @OneToMany(mappedBy = "ste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BonCmdA> bonCmdsA;
}
