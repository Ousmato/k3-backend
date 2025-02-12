package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Paiement_repositorie extends JpaRepository<Paiement, Long> {

    Paiement findByDateDePaiementAndIdInscriptionId(LocalDate dateDePaiement, long idInscription);

    @Query("select sum(p.montant) from  Paiement p where p.idInscription.id =:idInscrit")
    Double sumMontant(@Param("idInscrit") long idInscrit);

    List<Paiement> findByIdInscriptionId(long idInscrit);

    Paiement getByIdInscriptionId(long idInscrit);

    Paiement findById(long id);

}
