package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Semestres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private String nomSemetre;


    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate dateDebut;


    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate datFin;

    @NotNull
    @ManyToOne
    private AnneeScolaire idAnneeScolaire;

}
