package Gestion_scolaire.Teachers.entity;

import Gestion_scolaire.Administrators.entity.Admin;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Specialites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Le nom de la spécialité  est obligatoire")
    private String nom;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Admin idAdmin;
}
