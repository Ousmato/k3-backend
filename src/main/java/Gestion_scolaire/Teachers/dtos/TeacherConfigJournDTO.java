package Gestion_scolaire.Teachers.dtos;

import Gestion_scolaire.EnumClasse.Seance_type;
import lombok.Data;

import java.util.List;

@Data
public class TeacherConfigJournDTO {
    private long id;
    private String nom;
    private String prenom;
    private String salle;
    private String groupe;
    private long idGroupe;
    private String seanceType;
}
