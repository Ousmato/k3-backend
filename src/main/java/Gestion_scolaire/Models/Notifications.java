package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String titre;

    @NotNull
    private String description;

    @ManyToOne
    private Admin idAdmin;

    @ManyToOne
    private Emplois idEmplois;

    @ManyToOne
    private Teachers idTeachers;

    @ManyToOne
    private Documents idDoc;



}
