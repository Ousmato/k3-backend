package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class AnneeScolaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "La date du début d'année est obligatoire.\n")
    private LocalDate debutAnnee;

    @NotNull(message = "La date de fin d'année est obligatoire.\n")
    private LocalDate finAnnee;

    @NotNull(message = "Admin est obligatoire")
    @ManyToOne
    private Admin idAdmin;
}
