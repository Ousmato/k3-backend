package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull(message = "L'étudiant est obligatoire")
    @ManyToOne
    private Studens idEtudiant;

    @NotNull(message = "La classe est Obligatoire")
    @ManyToOne
    private StudentsClasse idClasse;

    @NotNull(message = "L'administrateur est invalide")
    @ManyToOne
    private Admin idAdmin;

    private LocalDate date = LocalDate.now();

    private boolean active = true;

    private boolean payer = false;

    @NotNull(message = "Le paiement est Obligatoire")
    private double scolarite = 0;

    private long adminPaye;


}
