package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class StudentsClasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private int effectifs = 0;

    @NotNull
    private double scolarite;

    @NotNull
    private boolean fermer = false;

    @OneToOne
    private NiveauFilieres idFiliere;
}
