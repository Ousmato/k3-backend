package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Jury;
import lombok.Data;

import java.util.List;

@Data
public class ProgramSoutenanceDto {
    private SoutenanceDTO soutenance;
    private List<JuryDto> jurys;
}
