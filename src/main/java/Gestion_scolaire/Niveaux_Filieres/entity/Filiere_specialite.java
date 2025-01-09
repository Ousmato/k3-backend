package Gestion_scolaire.Niveaux_Filieres.entity;

import Gestion_scolaire.Teachers.entity.Specialites;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Filiere_specialite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "La filiere est obligatoire")
    @ManyToOne
    private Filiere idFiliere;

    @NotNull(message = "La spécialité est obligatoire")
    @ManyToOne
    private Specialites idSpecialite;
}
