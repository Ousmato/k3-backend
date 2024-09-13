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
    private Documents idDoc;

    @NotNull
    @ManyToOne
    private Teachers idTeacher;

    @NotNull
//    @ManyToOne
    private String idJury;

    @NotNull
    @ManyToOne
    private Salles idSalle;



}
