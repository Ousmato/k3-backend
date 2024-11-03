package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Diplomes;
import Gestion_scolaire.EnumClasse.Teachers_status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Teachers extends UsersAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEnseignant;


    @NotNull(message = "Le status de l'enseignant est obligatoire")
    @Enumerated(EnumType.STRING)
    private Teachers_status status;

    @NotNull(message = "Diplome obligatoire")
    @Enumerated(EnumType.STRING)
    private Diplomes diplome;


}
