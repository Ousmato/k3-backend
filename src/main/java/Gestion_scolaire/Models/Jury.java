package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Jury_role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Jury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String role;

    @NotNull
    @ManyToOne
    private Teachers idTeacher;

    @NotNull
    @ManyToOne
    private Soutenance idSoutenance;
}
