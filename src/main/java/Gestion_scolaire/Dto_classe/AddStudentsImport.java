package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Studens;
import lombok.Data;

import java.util.List;

@Data
public class  AddStudentsImport{

    private long idClasse;
    private long idAnnee;
    private List<Studens> students;
}
