package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private double coutHeure = 0.0;

    @NotNull
    private int nbreHeures = 0;



    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate date;

    @ManyToOne
    private Journee journee;
}
