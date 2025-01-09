package Gestion_scolaire.students.entity;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull(message = "L'étudiant est obligatoire")
    @ManyToOne
    private Students idEtudiant;

    @NotNull(message = "La classe est Obligatoire")
    @ManyToOne
    private StudentsClasse idClasse;

    @NotNull(message = "L'administrateur est invalide")
    @ManyToOne
    private Admin idAdmin;

    private LocalDate date = LocalDate.now();

    private boolean active = true;

    private boolean payer = false;

    @NotNull(message = "Le numero de l'inscription est obligatoire")
    private String numeroInscrit;

    // Lien Many-to-Many avec SousFilieres
    @ManyToMany
    @JoinTable(
            name = "inscription_sous_filieres",
            joinColumns = @JoinColumn(name = "inscription_id"),
            inverseJoinColumns = @JoinColumn(name = "sous_filiere_id")
    )
    private List<SousFilieres> sousFilieres = new ArrayList<>();


}
