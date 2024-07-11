package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bielle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBielle;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;
}
