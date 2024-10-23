package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Emplois {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate dateDebut;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    private LocalDate dateFin;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    @ManyToOne
    private StudentsClasse idClasse;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    @ManyToOne
    private Semestres idSemestre;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    @ManyToOne
    private Modules idModule;

    private boolean valid = false;

    @NotNull(message = "Le champ ne doit pas être nul ou vide.\n")
    @ManyToOne
    private Admin idAdmin;


}
