package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Admin_role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Admin extends UsersAbstract{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long idAdministra;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Admin_role role;

    @NotNull
    private LocalDate updateDate = LocalDate.now();
}

