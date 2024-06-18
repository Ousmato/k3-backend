package Gestion_scolaire.Models;

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
    private String titre;

    @NotNull
    private String description;

    @NotNull
    private String docUrl;

    @NotNull
    private boolean deleted = false;

    @NotNull
    private LocalDate date = LocalDate.now();

    @NotNull
    private double DocSize;

    @ManyToOne
    private Seances idSeance;

}
