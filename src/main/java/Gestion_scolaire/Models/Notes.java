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


    @Max(value = 6, message = "Le coefficient ne doit pas être supérieur à 6.")
    private double classeNote;


    @Max(value = 20, message = "Le coefficient ne doit pas être supérieur à 6.")
    private double examNote;

//    @NotNull
//    private int coefficient;

    @ManyToOne
    private Inscription idInscription;

    @ManyToOne
    private Modules idModule;

    @ManyToOne
    private Semestres idSemestre;

    @ManyToOne
    private Admin idAdmin;
}
