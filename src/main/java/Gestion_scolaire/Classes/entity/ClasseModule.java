package Gestion_scolaire.Classes.entity;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Entity
public class ClasseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull
    @ManyToOne
    private NiveauFilieres idNiveauFiliere;

    @NotNull
    @ManyToOne
    private UE idUE;

    @NotNull
    @ManyToOne
    private Semestres idSemestre;
}
