package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Models.UE;
import lombok.Data;

@Data
public class AddUeDTO {
    private UE idUe;
    private StudentsClasse classe;
    private Semestres semestre;

}
