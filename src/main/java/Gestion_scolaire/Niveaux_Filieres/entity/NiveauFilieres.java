package Gestion_scolaire.Niveaux_Filieres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class NiveauFilieres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Filiere idFiliere;

    @ManyToOne
    private Niveau idNiveau;


}
