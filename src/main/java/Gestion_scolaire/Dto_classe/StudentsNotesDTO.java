package Gestion_scolaire.Dto_classe;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentsNotesDTO {
    private long id;
    private List<GetNoteDTO> noteDTO;
    private String nom;
    private String prenom;
    private double moyenGeneral;
    private String date_naissance;
    private String lieuNaissance;
    private String sexe;
}
