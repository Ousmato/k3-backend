package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "L'adresse email est obligatoire.\n")
    @Email(message = "L'adresse email doit être valide.\n")
    private String email;

    @NotNull
    private boolean active = true;

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

    @ManyToOne
    private UsersGrade usersGrade;


    private String nomBanque;

    private String  compteBanque;



}
