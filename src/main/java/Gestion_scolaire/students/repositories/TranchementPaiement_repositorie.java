package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.TranchePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranchementPaiement_repositorie extends JpaRepository<TranchePaiement, Long> {

}
