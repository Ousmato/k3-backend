package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.Participant;
import Gestion_scolaire.Models.SeanceConfig;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Models.Studens;
import lombok.Data;

import java.util.List;

@Data
public class DTO_Config {
    private long id;
    private Seances idSeance;
    private Seance_type seanceType;
    private String plageHoraire;
    private Participant idParticipant;



    public static DTO_Config toConfig(SeanceConfig dtoConfig) {
        DTO_Config dto = new DTO_Config();
        dto.setId(dtoConfig.getId());
        dto.setIdParticipant(dtoConfig.getIdParticipant());
        dto.setIdSeance(dtoConfig.getIdSeance());
        dto.setSeanceType(dtoConfig.getSeanceType());
        return dto;
    }
}
