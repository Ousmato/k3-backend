package Gestion_scolaire.students.dtos;

import Gestion_scolaire.students.entity.Students;
import lombok.Data;

import java.util.List;

@Data
public class  AddStudentsImport{

    private long idClasse;
    private long idAnnee;
    private long idAdmin;
    private List<Students> students;
}
