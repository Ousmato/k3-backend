package Gestion_scolaire.Administrators.entity;

import Gestion_scolaire.Models.UsersAbstract;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Admin extends UsersAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long idAdministra;

    @NotNull(message = "Le role est obligatoire")


//    @Enumerated(EnumType.STRING)
//    private Admin_role role;

    @NotNull(message = "Le role est obligatoire")
    @ManyToOne
    private Roles idRole;

    @NotNull
    private LocalDate updateDate = LocalDate.now();



}

