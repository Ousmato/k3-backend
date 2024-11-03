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

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalTime heureDebut;


    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalTime heureFin;


    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate date;

    @NotNull(message = "L'emploi du temps est obligatoire")
    @ManyToOne
    private  Emplois idEmplois;

    @NotNull(message = "Choisir au moins un enseignant")
    @ManyToOne
    private Teachers idTeacher;

    @NotNull(message = "Choisir au moins un type : CM; TD")
    @Enumerated(EnumType.STRING)
    private Seance_type seanceType;

    @NotNull(message = "La salle est obligatoire")
    @ManyToOne
    private Salles idSalle;
}
