package Gestion_scolaire.students.entity;

import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InscriptionSousFilieres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Inscription idInscription;

    @ManyToOne
    private SousFilieres idSousFiliere;
}
