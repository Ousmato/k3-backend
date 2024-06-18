package Gestion_scolaire.Models;

import jakarta.persistence.*;
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

    @NotNull
    private LocalTime heureDebut;

    @NotNull
    private LocalTime heureFin;

    @NotNull
    private String jour;

    @NotNull
    private LocalDate date;

    @ManyToOne
    private  Emplois idEmplois;


    @ManyToOne
    private Teachers idTeacher;
}
