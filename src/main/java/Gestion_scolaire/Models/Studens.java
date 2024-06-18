package Gestion_scolaire.Models;

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
    private String sexe;


    @NotNull
    private double scolarite = 0;

    @NotNull
    private String matricule;

    @NotNull
    private LocalDate date;

    @NotNull
    private String lieuNaissance;

    @NotNull
    private String dateNaissance;


    @ManyToOne
    private Admin idAdmin;

    @ManyToOne
    private  StudentsClasse idClasse;
}
