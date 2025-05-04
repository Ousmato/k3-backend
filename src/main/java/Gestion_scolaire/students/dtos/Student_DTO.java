package Gestion_scolaire.students.dtos;

import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.enumClass.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Student_DTO {

    private String telephone;
    private String urlPhoto;
    private String sexe;
    private long idEtudiant;
    private String nom;
    private String prenom;
    private String email;
    private String matricule;
    private String lieuNaissance;
    private String dateNaissance;
    private String lastNameFather;
    private String motherName;
    private String commNaissance;
    private String cercleNaissance;
    private String nationalite;
    private String residenceParent;
    private String diplome;
    private String academies;
    private String series;
    private String status;
    private int numeroPlace;
    private int anneeObtention;
    private String quartier;

    public static Student_DTO toDTO(Students student) {
        Student_DTO dto = new Student_DTO();
        dto.setTelephone(student.getTelephone());
        dto.setUrlPhoto(student.getUrlPhoto());
        dto.setSexe(student.getSexe());
        dto.setIdEtudiant(student.getId());
        dto.setNom(student.getNom());
        dto.setPrenom(student.getPrenom());
        dto.setEmail(student.getEmail());
        if(student.getAcademies() != null) {
            dto.setAcademies(student.getAcademies().toString().replace("_", " ").toUpperCase());

        }
        dto.setMatricule(student.getMatricule());
        dto.setLieuNaissance(student.getLieuNaissance());
        dto.setDateNaissance(student.getDateNaissance());
        dto.setLastNameFather(student.getLastNameFather());
        dto.setMotherName(student.getMotherName());
        dto.setCommNaissance(student.getCommNaissance());
        dto.setCercleNaissance(student.getCercleNaissance());
        dto.setNationalite(student.getNationalite());
        dto.setResidenceParent(student.getResidenceParent());
        if (student.getDiplome() != null) {
            dto.setDiplome(student.getDiplome().toString().replace("_", " ").toUpperCase());

        }
        if (student.getSeries() != null) {
            dto.setSeries(student.getSeries().toString().replace("_", " ").toUpperCase());
        }
       if (student.getStatus() != null) {
           dto.setStatus(student.getStatus().toString().replace("_", " ").toUpperCase());
       }

        dto.setNumeroPlace(student.getNumeroPlace());
        dto.setAnneeObtention(student.getAnneeObtention());
        if(student.getQuartier() != null) {
            dto.setQuartier(student.getQuartier().toString());
        }
        return dto;

    }

}
