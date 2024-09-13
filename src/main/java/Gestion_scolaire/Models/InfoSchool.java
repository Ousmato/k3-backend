package Gestion_scolaire.Models;

import jakarta.persistence.*;
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
