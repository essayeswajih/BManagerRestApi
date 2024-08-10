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
public class Devis {
    @GeneratedValue
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "devis_id")
    private List<Item> items;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @Column(nullable = false)
    private Boolean trans = false;

    private String ref;
}
