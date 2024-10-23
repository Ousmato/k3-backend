package Gestion_scolaire.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@MappedSuperclass
@Data
public abstract class UsersAbstract {

    @NotBlank(message = "L'adresse email est obligatoire.\n")
    @Email(message = "L'adresse email doit être valide.\n")
    private String email;

    @NotNull
    private boolean active = true;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&^_-]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un symbole.")
    private String password;

    @NotBlank(message = "Le champ nom ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String nom;

    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ doit contenir entre 3 et 20 caractères.\n")
    private String prenom;

    @NotNull(message = "Le numéro de téléphone est obligatoire.\n")
    @Min(value = 10000000, message = "Le numéro de téléphone doit contenir exactement 8 chiffres.\n")
    @Max(value = 99999999, message = "Le numéro de téléphone doit contenir exactement 8 chiffres.\n")
    private int telephone;

    private String urlPhoto;

    @NotBlank(message = "Le champ nom ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String sexe;


}
