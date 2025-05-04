package Gestion_scolaire.Teachers.entity;

import Gestion_scolaire.Teachers.enumClasse.Diplomes;
import Gestion_scolaire.Teachers.enumClasse.Teachers_status;
import Gestion_scolaire.Models.Personne;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Teachers extends Personne {

    @NotNull(message = "Le status de l'enseignant est obligatoire")
    @Enumerated(EnumType.STRING)
    private Teachers_status status;

    @NotNull(message = "Diplome obligatoire")
    @Enumerated(EnumType.STRING)
    private Diplomes diplome;

    //@NotNull()
    private String dateNaissance;

}
