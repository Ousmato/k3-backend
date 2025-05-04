package Gestion_scolaire.Classes.entity;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.students.entity.StudentsClasse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 70, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String nomUE;

    @NotBlank(message = "Le champ code UE ne doit pas être nul ou vide.\n")
    private String codeUE;


    private boolean active = true;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private AdministrationUsers idAdministrationUsers;

    @NotNull(message = "La classe est obligatoire")
    @ManyToOne
    private StudentsClasse idClasse;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Semestres idSemestre;

}
