package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface Paiement_repositorie extends JpaRepository<Paiement, Long> {

    Paiement findByDateDePaiementAndIdInscriptionId(LocalDate dateDePaiement, long idInscription);

}
