package Gestion_scolaire.students.entity;

import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    // Liste des spécialités pour cette classe (vide si aucune spécialité)
    @OneToMany(mappedBy = "idClasse", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SousFilieres> specialites = new ArrayList<>();
}
