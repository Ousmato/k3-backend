package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Seances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Heure de debut est invalide")
    private LocalTime heureDebut;

    @NotNull(message = "Heure de fin est invalide")
    private LocalTime heureFin;

    @NotNull
    private LocalDate date;

    @NotNull(message = "veillez choisir un emploi du temps")
    @ManyToOne
    private  Emplois idEmplois;


//    @NotNull(message = "Le module est obligatoire")
//    @ManyToOne
//    private Modules idModule;


//    @NotNull(message = "La salle est obligatoire")
//    @ManyToOne
//    private Salles idSalle;

}
