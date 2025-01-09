package Gestion_scolaire.Administrators.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nom du role est obligatoire")
    private String nom;

    @NotNull(message = "L'admin DG est Obligatoire")
    private long idAdminDg;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date = LocalDate.now();
}
