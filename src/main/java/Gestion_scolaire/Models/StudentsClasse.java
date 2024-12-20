package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class StudentsClasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private AnneeScolaire idAnneeScolaire;

    @NotNull
    private int effectifs = 0;


    @NotNull
    private boolean fermer;

    @ManyToOne
    private NiveauFilieres idFiliere;
}
