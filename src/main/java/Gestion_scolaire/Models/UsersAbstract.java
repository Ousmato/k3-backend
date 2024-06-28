package Gestion_scolaire.Models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@MappedSuperclass
@Data
public abstract class UsersAbstract {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

    @NotNull
    private String email;

    @NotNull
    private boolean active = true;

    @NotNull
    private String password;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private int telephone;

    @NotNull
    private String urlPhoto;

    @NotNull
    private String sexe;
}
