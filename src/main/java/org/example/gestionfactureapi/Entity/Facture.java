package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idFacture;

    private LocalDate date;
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "ste_id")
    private Ste ste;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bielle> bielles;
}
