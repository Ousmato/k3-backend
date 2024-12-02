package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Max(value = 20, message = "La note de classe ne doit pas être supérieur à 20.")
    private double classeNote = 0.0;


    @Max(value = 20, message = "La note d'examen ne doit pas être supérieur à 20.")
    private double examNote = 0.0;

    @NotNull(message = "L'étudiant est obligatoire")
    @ManyToOne
    private Inscription idInscription;

    @NotNull(message = "Le module est obligatoire")
    @ManyToOne
    private Modules idModule;

//    @NotNull(message = "La note du module est obligatoire")
    private double noteModule = 0.0;

    @NotNull(message = "Le semestre est obligatoire")
    @ManyToOne
    private Semestres idSemestre;

    @NotNull(message = "L'administrateur est obligatoire")
    @ManyToOne
    private Admin idAdmin;
}
