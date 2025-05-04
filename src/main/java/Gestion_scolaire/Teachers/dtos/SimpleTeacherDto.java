package Gestion_scolaire.Teachers.dtos;

import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.enumClasse.Teachers_status;
import lombok.Data;

@Data
public class SimpleTeacherDto {
    private long id;
    private String nom;
    private Teachers_status status;
    private String prenom;
    private String email;
    private int telephone;

    public static SimpleTeacherDto toDto(Teachers teacher) {
        SimpleTeacherDto dto = new SimpleTeacherDto();
        dto.setId(teacher.getId());
        dto.setNom(teacher.getNom());
        dto.setStatus(teacher.getStatus());
        dto.setPrenom(teacher.getPrenom());
        dto.setEmail(teacher.getEmail());
        dto.setTelephone(dto.getTelephone());
        return dto;
    }
}
