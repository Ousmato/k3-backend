package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Studens;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Student_DTO {

    private String nom;
    private String prenom;
    private ClasseDTO idClasse;

    public static Student_DTO toStudent(Studens studens){
        Student_DTO dto = new Student_DTO();
        dto.setNom(studens.getNom());
        dto.setPrenom(studens.getPrenom());
        dto.setIdClasse(ClasseDTO.toClasseDTO(studens.getIdClasse()));
        return dto;
    }

    public static List<Student_DTO> toStudent(List<Studens> studensList) {
        List<Student_DTO> dtos = new ArrayList<>();
        for (Studens studens : studensList) {
            dtos.add(toStudent(studens)); // Convert each Studens object to Student_DTO and add it to the list
        }
        return dtos;
    }
}
