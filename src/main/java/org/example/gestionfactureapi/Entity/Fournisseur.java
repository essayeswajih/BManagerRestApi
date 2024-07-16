package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"ste"})
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idFournisseur;
    @NonNull
    private String intitule;
    private String matriculeFiscale;
    private String formeJurudique;
    @Email
    private String email;
    private String adresse;
    private String tel;
    private String Fax;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;
    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<Article> articles;

}
