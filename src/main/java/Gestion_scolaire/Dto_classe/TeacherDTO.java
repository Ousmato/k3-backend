package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.Teachers_status;
import Gestion_scolaire.Models.Teachers;
import lombok.Data;

@Data
public class TeacherDTO {
    private String email;
//    private boolean active;
    private String nom;
    private String prenom;
    private int telephone;
    private String urlPhoto;
//    private String sexe;
    private long idEnseignant;
//    private Teachers_status status;

    public static TeacherDTO toTeacherDTO(Teachers teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setEmail(teacher.getEmail());
//        dto.setActive(teacher.isActive());
        dto.setNom(teacher.getNom());
        dto.setPrenom(teacher.getPrenom());
        dto.setTelephone(teacher.getTelephone());
        dto.setUrlPhoto(teacher.getUrlPhoto());
//        dto.setSexe(teacher.getSexe());
        dto.setIdEnseignant(teacher.getIdEnseignant());
//        dto.setStatus(teacher.getStatus());
        return dto;
    }
}
