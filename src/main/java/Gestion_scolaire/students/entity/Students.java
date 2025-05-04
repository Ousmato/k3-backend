package Gestion_scolaire.students.entity;

import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.Models.Personne;
import Gestion_scolaire.students.enumClass.StudentDiplome;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "L'adresse email est obligatoire.\n")
    @Email(message = "L'adresse email doit être valide.\n")
    private String email;

    @NotNull
    private boolean active = true;

    //    @NotNull(message = "Mot de passe est obligatoire")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&^_-]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un symbole.")
    private String password;

    @NotBlank(message = "Le champ nom ne doit pas être nul ou vide.\n")
    @Size(min = 2, max = 30, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String nom;

    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 2, max = 30, message = "Le champ prenom doit contenir entre 3 et 20 caractères.\n")
    private String prenom;

    //    @NotNull(message = "Le numéro de téléphone est obligatoire.\n")
    @Pattern(regexp = "^(Neant|[0-9]{8})$", message = "Le numéro de téléphone doit contenir exactement 8 chiffres ou être 'Neant'.")
    private String telephone;

    private String urlPhoto;

    @NotBlank(message = "Le champ nom ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ sexe doit contenir entre 3 et 20 caractères.\n")
    private String sexe;

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

    private String academies;

//    @NotNull(message = "La serie est obligatoire.\n")

    private String series;

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

    private String quartier;

}
