package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Jury;
import Gestion_scolaire.Models.Teachers;
import lombok.Data;

@Data
public class JuryDto {

    private long id;
    private String role;
    private long idTeacher;
    private Teachers teachers;
//    private

    public static JuryDto toJury(Jury jury){
        JuryDto dto = new JuryDto();
        dto.setRole(jury.getRole());
        dto.setIdTeacher(jury.getId());
        return dto;
    }
}
