package Gestion_scolaire.Dto_classe;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentsNotesDTO {
    private List<NoteDTO> noteDTO;
    private String nom;
    private String prenom;
    private LocalDate date_naissance;
    private String lieuNaissance;
}
