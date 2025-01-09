package Gestion_scolaire.students.entity;

import Gestion_scolaire.students.enumClass.Type_status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TranchePaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de l'étudiant est obligatoire")
    private Type_status typeStudent;

    @NotNull(message = "Le montant de la tranche de paiement est obligatoire")
    private double montantPaiement = 0.0;

    public static List<TranchePaiement> init(List<Type_status> typeStatuses) {
        List<TranchePaiement> tranchePaiements = new ArrayList<>();

        for (Type_status t : typeStatuses) {
            TranchePaiement tp = new TranchePaiement();

            switch (t) {
                case PROFESSIONNEL_PRIVEE:
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(50000.0);
                    break;

                case REGULIER:
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(6000.0);
                    break;

                case CANDIDAT_LIBRE:
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(60000.0);
                    break;

                case PROFESSIONNEL_ETAT:
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(25000.0);
                    break;

                case PROFESSIONNEL_COLLECTIVITE:
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(10000.0);
                    break;

                case FORMATION_CONTINUE: // Nouveau cas ajouté
                    tp.setTypeStudent(t);
                    tp.setMontantPaiement(70000.0); // Exemple de montant
                    break;

                default:
                    throw new IllegalArgumentException("Statut non pris en charge : " + t);
            }

            tranchePaiements.add(tp);
        }

        return tranchePaiements;
    }


}
