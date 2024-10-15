package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.Participant;
import lombok.Data;

import java.util.List;

@Data
public class TeacherConfigJournDTO {
    private long id;
    private String nom;
    private String prenom;
    private String salle;
    private String groupe;
    private List<Seance_type> seanceType;
}
