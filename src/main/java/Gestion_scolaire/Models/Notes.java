package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private double classeNote;

    @NotNull
    private double examNote;

    @NotNull
    private int coefficient;

    @ManyToOne
    private Studens idStudents;

    @ManyToOne
    private Modules idModule;

    @ManyToOne
    private Semestres idSemestre;
}
