package Gestion_scolaire.Administrators.entity;

import Gestion_scolaire.Models.Personne;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AdministrationUsers extends Personne {
//
    @NotNull(message = "Le poste est obligatoire")
    @ManyToOne
    private Postes idPoste;

    //    @NotNull(message = "Mot de passe est obligatoire")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*#?&^_-]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un symbole.")
    private String password;

    @NotNull
    private LocalDate updateDate = LocalDate.now();

}

