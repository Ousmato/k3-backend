package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Seance_type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Journee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    private Participant idParticipant;

    @NotNull(message = "Heure de debut est invalide")
    private LocalTime heureDebut;

    @NotNull(message = "Heure de fin est invalide")
    private LocalTime heureFin;

    @NotNull
    private LocalDate date;

    @NotNull(message = "veillez choisir un emploi du temps")
    @ManyToOne
    private  Emplois idEmplois;

    @NotNull
    @ManyToOne
    private Teachers idTeacher;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Seance_type seanceType;

    @NotNull(message = "La salle est obligatoire")
    @ManyToOne
    private Salles idSalle;
}
