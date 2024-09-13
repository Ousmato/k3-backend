package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Paie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull
    private double coutHeure;

    @NotNull
    private int nbreHeures;


    @NotNull
    private LocalDate date;

    @ManyToOne
    private Journee journee;
}
