package Gestion_scolaire.Niveaux_Filieres.entity;

import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentsClasse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SousFilieres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Nom de sous filière est obligatoire")
    private String nomSousFiliere;

    @NotNull(message = "La classe est obligatoire")
    @ManyToOne
    @JsonBackReference
    private StudentsClasse idClasse;

}
