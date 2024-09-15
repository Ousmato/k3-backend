package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Soutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime heureDebut;

    @NotNull
    private LocalTime heureFin;

    @NotNull
    @OneToOne
    private StudentDoc idDoc;

    private int note = 0;
    @NotNull
    @OneToOne
    private Salles idSalle;



}
