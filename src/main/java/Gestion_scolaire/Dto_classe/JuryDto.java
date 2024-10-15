package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Teachers;
import lombok.Data;

@Data
public class JuryDto {

    private long id;
    private String role;
    private long idTeacher;
    private Teachers teachers;
//    private
}
