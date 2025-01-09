package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Dto_classe.JuryDto;
import lombok.Data;

import java.util.List;

@Data
public class ProgramSoutenanceDto {
    private SoutenanceDTO soutenance;
    private List<JuryDto> jurys;
}
