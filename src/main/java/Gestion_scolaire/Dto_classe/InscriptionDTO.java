package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentsClasse;
import lombok.Data;

@Data
public class InscriptionDTO {
    private long id;
    private StudentsClasse idClasse;
    private Admin idAdmin;
    private Studens idEtudiant;
}
