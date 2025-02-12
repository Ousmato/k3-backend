package Gestion_scolaire.students.dtos;

import lombok.Data;

@Data
public class FiliereStudentDTO {
    private long idInscrit;
    private String nom;
    private String prenom;
    private String sexe;
    private String status;
    private String lieuNaissance;
    private String dateNaissance;
    private String classe;
    private double payer;
    private double reliquat;
    private double seuil;
}
