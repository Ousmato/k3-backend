package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Teachers;
import lombok.Data;

import java.util.List;

// DTO classes
@Data
public class TeacherSeancesDTO {
    private TeacherDTO teacher;
    private List<EmploisDTO> emplois;
    private List<ClasseDTO> classRoom;
    private List<SeanceDTO> seances;


}
