package org.example.gestionfactureapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BonLivDTO {
    private BonCmdA bon;
    private Date date;
}
