package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Emplois {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    @ManyToOne
    private StudentsClasse idClasse;

    @NotNull
    @ManyToOne
    private Semestres idSemestre;

    @NotNull
    @ManyToOne
    private Modules idModule;

    private boolean valid = false;


}
