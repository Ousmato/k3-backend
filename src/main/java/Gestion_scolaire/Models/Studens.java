package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Type_status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Studens extends UsersAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEtudiant;

    @NotNull
    private double scolarite = 0;

    @NotNull
    private boolean payer = false;

    @NotNull
    private String matricule;

    @NotNull
    private LocalDate date;

    @NotNull
    private String lieuNaissance;

    @NotNull
    private LocalDate dateNaissance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type_status status;


    @ManyToOne
    private Admin idAdmin;

    @NotNull
    @ManyToOne
    private  StudentsClasse idClasse;

}
