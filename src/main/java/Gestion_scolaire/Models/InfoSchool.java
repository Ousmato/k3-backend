package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class InfoSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;



    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 40, message = "Le champ doit contenir entre 3 et 40 caractères.\n")
    private String nomSchool;

    @NotBlank(message = "L'adresse email est obligatoire.\n")
    @Email(message = "L'adresse email doit être valide.\n")
    private String email;

    @NotNull(message = "Le numéro de téléphone est obligatoire.\n")
    @Min(value = 10000000, message = "Le numéro de téléphone doit contenir exactement 8 chiffres.\n")
    @Max(value = 99999999, message = "Le numéro de téléphone doit contenir exactement 8 chiffres.\n")
    private int telephone;

    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 40, message = "Le champ doit contenir entre 3 et 40 caractères.\n")
    private String localite;

    @NotNull
    private String urlPhoto;


}
