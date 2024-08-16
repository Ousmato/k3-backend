package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Seance_type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class SeanceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    private Participant idParticipant;

    @NotNull
    private LocalTime heureDebut;

    @NotNull
    private LocalTime heureFin;

    @NotNull
    @ManyToOne
    private Seances idSeance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Seance_type seanceType;

}
