package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class  Moyenne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "La note de l'unité d'enseignement est obligatoire")
    private double noteUe = 0.0;

    @NotNull(message = "L'étudiant est obligatoire")
    @ManyToOne
    private  Inscription idInscription;

    @NotNull(message = "Le semestre ne dois pas etre null ou dupliqué")
    @OneToOne
    private Semestres idSemestre;

    @NotNull(message = "La note du module est obligatoire")
    private double moyenGenerale = 0.0;

}
