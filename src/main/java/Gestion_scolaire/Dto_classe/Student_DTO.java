package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Studens;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Student_DTO {

    private Studens student;
    private long idClasse;
    private long idAdmin;
    private double scolarite;

}
