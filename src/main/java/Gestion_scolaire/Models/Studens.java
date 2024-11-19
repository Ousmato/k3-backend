package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Type_status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Studens extends UsersAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEtudiant;



    @NotBlank(message = "Le champ matricule ne doit pas être nul ou vide.\n")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]+$", message = "Le matricule doit contenir des lettres et des chiffres, et ne doit pas être composé uniquement de chiffres.")
    private String matricule;


    @NotBlank(message = "Le champ matricule ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String lieuNaissance;

    @NotNull(message = "Le champ matricule ne doit pas être nul ou vide.\n")
    private LocalDate dateNaissance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type_status status;


}
