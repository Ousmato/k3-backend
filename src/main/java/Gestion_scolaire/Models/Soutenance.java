package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate date;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalTime heureDebut;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalTime heureFin;

    @NotNull
    @ManyToOne
    private StudentDoc idDoc;


    @NotNull
    @OneToOne
    private Salles idSalle;

    @NotNull
    @ManyToOne
    private Admin idAdmin;



}
