package Gestion_scolaire.Teachers.dtos;

import Gestion_scolaire.Emplois.dtos.TeacherEmploiDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeacherSemaineDTO {
    private String periode; // Ex: "Semaine du 01/01 au 07/01"
    private List<TeacherEmploiDTO> emplois;
}
