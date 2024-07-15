package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idUser;
    private String name;
    private String tel;
    private String adresse;
    private String fax;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;
}
