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
    private int id;
    private Date date;
    private int Input;
    private int Output;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
    private Integer docId;
    private String docName;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
