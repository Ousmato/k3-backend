package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Seances;
import lombok.Data;

import java.util.List;

@Data
public class DTO {
    private List<Seances> seance;
//    private List<SeanceConfig> config;
}
