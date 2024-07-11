package org.example.gestionfactureapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonLivA{
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "bon_cmd_id")
    private BonCmdA bonCmdA;

}
