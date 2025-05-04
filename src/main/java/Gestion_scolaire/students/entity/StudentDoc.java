package Gestion_scolaire.students.entity;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class StudentDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Inscription idInscription;

    @NotNull
    @ManyToOne
    private Documents idDocument;

    @NotNull
    @ManyToOne
    private AdministrationUsers idAdministrationUsers;
}
