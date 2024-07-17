package Gestion_scolaire.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class InfoSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate debutAnnee;

    @NotNull
    private LocalDate finAnnee;

    @NotNull
    private String nomSchool;

    @NotNull
    private String email;

    @NotNull
    private int telephone;

    @NotNull
    private String localite;

    @NotNull
    private String urlPhoto;

}
