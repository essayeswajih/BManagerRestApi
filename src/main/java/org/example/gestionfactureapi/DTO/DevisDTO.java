package org.example.gestionfactureapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestionfactureapi.Entity.Client;
import org.example.gestionfactureapi.Entity.Item;
import org.example.gestionfactureapi.Entity.Ste;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevisDTO {
    private Client client;
    private List<Item> items;
    private Date dateCreation;
    private Ste ste;
}
