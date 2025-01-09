package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import lombok.Data;

@Data
public class GetInputNoteInscritDTO {
    private long idNote;
    private long idModule;
    private double examNote;
    private double classeNote;
    private  double noteUe;
    private String validate;
    private double sessionNote;
    private int nbreSession;
    private InscriptionNoteDTO inscriptions;
    private AnneeScolaire anneeScolaire;
    private String nomClasse;
    private String semestre;
    private String nomModule;


}
