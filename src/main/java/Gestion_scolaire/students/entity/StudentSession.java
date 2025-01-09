package Gestion_scolaire.students.entity;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Models.Semestres;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class StudentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "L'Etudiant est obligatoire")
    @ManyToOne
    private Inscription idInscrit;

    @NotNull(message = "le semestre est obligatoire")
    @ManyToOne
    private Semestres idSemestre;

    @NotNull(message = "Le module es obligatoire")
    @ManyToOne
    private Modules idModule;

    private int nbreSession = 1;

    private double noteSession = 0.0;
}
