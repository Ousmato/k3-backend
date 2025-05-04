package Gestion_scolaire.Niveaux_Filieres.entity;

import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 45, message = "Le champ doit contenir entre 3 et 40 caractères.\n")
    private String nomFiliere;

    @Enumerated(EnumType.STRING)
    private TypeFiliere typeFiliere;

    @NotNull(message = "La faculté est obligatoire")
    @Enumerated(EnumType.STRING)
    private Facultes faculte;

}
