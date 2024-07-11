package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bielle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBielle;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> articles;

    private Double unitPrice;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;
}
