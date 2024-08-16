package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.SeanceConfig;
import Gestion_scolaire.Models.Seances;
import lombok.Data;

import java.util.List;

@Data
public class SurveillanceDTO {

    private List<Seances> seancesList;

    private List<SeanceConfig> configList;
}
