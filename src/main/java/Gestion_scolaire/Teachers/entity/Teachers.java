package Gestion_scolaire.Teachers.entity;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Teachers.enumClasse.Diplomes;
import Gestion_scolaire.Teachers.enumClasse.Teachers_status;
import Gestion_scolaire.Models.UsersAbstract;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Teachers extends UsersAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEnseignant;


    @NotNull(message = "Le status de l'enseignant est obligatoire")
    @Enumerated(EnumType.STRING)
    private Teachers_status status;

    @NotNull(message = "Diplome obligatoire")
    @Enumerated(EnumType.STRING)
    private Diplomes diplome;

//    @NotNull()
    private String dateNaissance;

    private String grade;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Admin admin;


}
