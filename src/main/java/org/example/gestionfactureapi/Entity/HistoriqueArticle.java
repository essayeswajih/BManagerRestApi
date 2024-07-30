package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueArticle {
    @Id
    @GeneratedValue
    private Integer id;
    private Date date;
    private int Input;
    private int Output;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
    private Integer qteReel;
    private Integer docId;
    private String docName;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
