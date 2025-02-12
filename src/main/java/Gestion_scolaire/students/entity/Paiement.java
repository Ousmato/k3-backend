package Gestion_scolaire.students.entity;

import Gestion_scolaire.Administrators.entity.Admin;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @NotNull(message = "Le tranche de paiement est obligatoire")
//    @ManyToOne
//    private TranchePaiement idTranchePaiement;

    private double montant;

    @NotNull(message = "L'inscription est obligatoire")
    @ManyToOne
    private Inscription idInscription;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Admin idAdmin;

    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate dateDePaiement;

    private LocalDate updateDate = LocalDate.now();
}
