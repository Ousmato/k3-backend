package Gestion_scolaire.students.entity;

import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.Models.UsersAbstract;
import Gestion_scolaire.students.enumClass.Academies;
import Gestion_scolaire.students.enumClass.InscriptionSeries;
import Gestion_scolaire.students.enumClass.QuartiersResidence;
import Gestion_scolaire.students.enumClass.StudentDiplome;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Students extends UsersAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEtudiant;



//    @NotBlank(message = "Le champ matricule ne doit pas être nul ou vide.\n")
//    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]+$", message = "Le matricule doit contenir des lettres et des chiffres, et ne doit pas être composé uniquement de chiffres.")
    private String matricule;


//    @NotBlank(message = "Le champ matricule ne doit pas être nul ou vide.\n")
//    @Size(min = 3, max = 20, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String lieuNaissance;

    @NotNull(message = "Le champ date de naissance ne doit pas être nul ou vide.\n")

    private String dateNaissance;

//    @NotNull(message = "Le Prénom du Pere est obligatoire.\n")
    private String lastNameFather;

//    @NotNull(message = "Le Nom est Prénom de la mère est obligatoire.\n")
//    @Size(min = 3, max = 20, message = "Le champ nom et prenom de la mere doit contenir entre 3 et 20 caractères.\n")
    private String motherName;

//    @NotNull(message = "La commune de naissance est obligatoire.\n")
//    @Size(min = 3, max = 30, message = "Le champ commune de naissance doit contenir entre 3 et 30 caractères.\n")
    private String commNaissance;

//    @NotNull(message = "Le cercle de naissance est obligatoire.\n")
//    @Size(min = 3, max = 20, message = "Le champ cercle de naissance doit contenir entre 3 et 20 caractères.\n")
    private String cercleNaissance;

//    @NotNull(message = "La nationalité est obligatoire.\n")
//    @Size(min = 3, max = 15, message = "Le champ nationalité doit contenir entre 3 et 25 caractères.\n")
    private String nationalite;

//    @NotNull(message = "La residence des parent est obligatoire.\n")
//    @Size(min = 3, max = 20, message = "Le champ résidence des parents doit contenir entre 3 et 20 caractères.\n")
    private String residenceParent;


//    @NotNull(message = "Le diplome est obligatoire.\n")
    @Enumerated(EnumType.STRING)
    private StudentDiplome diplome;

//    @NotNull(message = "L'académie est obligatoire.\n")
    @Enumerated(EnumType.STRING)
    private Academies academies;

//    @NotNull(message = "La serie est obligatoire.\n")
    @Enumerated(EnumType.STRING)
    private InscriptionSeries series;

    @NotNull(message = "Le status est obligatoire.\n")
    @Enumerated(EnumType.STRING)
    private Type_status status;


//    @NotNull(message = "Le numéro de place est obligatoire")
//    @Min(value = 1, message = "Le numéro de place doit être supérieur ou égal à 1")
//    @Max(value = 9999, message = "Le numéro de place ne doit pas dépasser 4 chiffres")
    private int numeroPlace;

//    @NotNull(message = "L'année d'obtention es obligatoire")
    private int anneeObtention;

//    @NotNull(message = "Le quartier de résidence  es obligatoire")
    @Enumerated(EnumType.STRING)
    private QuartiersResidence quartier;



}
