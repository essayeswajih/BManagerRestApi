package org.example.gestionfactureapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idClient;
    //@Column(unique = true,nullable = false)
    //@NonNull
    private String name;
    private String tel;
    //@Column(unique = true,nullable = false)
    //@NonNull
    private String matriculeFiscale;
    private String adresse;
    private String fax;
    private String email;
    private boolean exonere;
    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Devis> devisList;
}