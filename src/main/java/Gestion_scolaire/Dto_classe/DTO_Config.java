package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class DTO_Config {
    private long id;
    private Teachers idTeacher;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Seance_type seanceType;
    private String plageHoraire;
    private Participant idParticipant;
    private List<Participant> id_Participant;


}
