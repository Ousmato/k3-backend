package Gestion_scolaire.Emplois.dtos;

import Gestion_scolaire.Teachers.dtos.TeacherVolHoraireDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TeacherEmploiDTO {
    private String semestre;
    private String niveau;
    private String filiere;
    private String nomModule;

    private List<TeacherVolHoraireDTO> volHoraires;
    private String semaines;
}
