package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Semestres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nomSemetre;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate datFin;

    @NotNull
    @ManyToOne
    private AnneeScolaire idAnneeScolaire;

}
