package Gestion_scolaire.Teachers.dtos;

import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.enumClasse.Teachers_status;
import Gestion_scolaire.Teachers.entity.Teachers;
import lombok.Data;

import java.util.List;

@Data
public class TeacherDTO {
    private String email;
//    private boolean active;
    private String nom;
    private String prenom;
    private String diplome;
    private String telephone;
    private String urlPhoto;
    private String sexe;
    private long idEnseignant;
    private Teachers_status status;
    private List<Specialites> specialitesList;

    public static TeacherDTO toTeacherDTO(Teachers teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setEmail(teacher.getEmail());
        dto.setDiplome(teacher.getDiplome().toString());
        dto.setNom(teacher.getNom());
        dto.setPrenom(teacher.getPrenom());
        dto.setTelephone(teacher.getTelephone());
        dto.setUrlPhoto(teacher.getUrlPhoto());
        dto.setSexe(teacher.getSexe());
        dto.setIdEnseignant(teacher.getIdEnseignant());
        dto.setStatus(teacher.getStatus());
        return dto;
    }
}
