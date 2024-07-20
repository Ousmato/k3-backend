package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Models.UE;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DTO_ClassModule {
    private StudentsClasse idStudentClasse;
    private List<UE> idUE;



}
