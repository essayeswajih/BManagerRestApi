package org.example.gestionfactureapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestionfactureapi.Entity.Fournisseur;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Entity.Ste;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonCmdADTO {
    private Fournisseur fournisseur;
    private List<Item> items;
    private Date dateCreation;
    private Ste ste;
}
