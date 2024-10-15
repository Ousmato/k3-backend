package Gestion_scolaire.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@MappedSuperclass
@Data
public abstract class UsersAbstract {

    @NotNull
    private String email;

    @NotNull
    private boolean active = true;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
