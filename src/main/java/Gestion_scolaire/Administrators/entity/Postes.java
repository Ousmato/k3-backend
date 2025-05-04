package Gestion_scolaire.Administrators.entity;

import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.EnumClasse.RoleTypes;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Postes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nom du role est obligatoire")
    private String nom;

    @Enumerated(EnumType.STRING)
    private RoleTypes roleType;

    @Enumerated(EnumType.STRING)
    private Facultes faculte;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date = LocalDate.now();

//    @NotNull(message = "Le type de filière est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeFiliere typeFiliere;
}
