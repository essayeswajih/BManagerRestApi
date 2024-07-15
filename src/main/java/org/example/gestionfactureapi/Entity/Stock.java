package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Stock {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Article article;

    private Integer Qte;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;
}
