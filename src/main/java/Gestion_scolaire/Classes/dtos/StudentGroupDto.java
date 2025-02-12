package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import lombok.Data;

import java.util.List;

@Data
public class StudentGroupDto {

    private  long id;
    private  String nom;
    private  String nomModule;
    private  String classe;
    private AnneeScolaire annee;
    private String semestre;
    private List<InscriptionNoteDTO> inscriptions;
}
