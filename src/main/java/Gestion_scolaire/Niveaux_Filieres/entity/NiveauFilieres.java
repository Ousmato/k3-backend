package Gestion_scolaire.Niveaux_Filieres.entity;

import Gestion_scolaire.EnumClasse.Facultes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class NiveauFilieres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "La filière est obligatoire")
    @ManyToOne
    private Filiere idFiliere;

    @NotNull(message = "Le niveau est obligatoire")
    @ManyToOne
    private Niveau idNiveau;




}
