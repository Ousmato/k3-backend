package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.DocType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotNull
    @Enumerated(EnumType.STRING)
    private DocType docType;


    @NotNull
    private boolean deleted = false;

    @NotNull
    private LocalDate date = LocalDate.now();


    @NotNull
    @ManyToOne
    private Teachers idEncadrant;

    @NotNull
    private boolean soutenue = false;


}
