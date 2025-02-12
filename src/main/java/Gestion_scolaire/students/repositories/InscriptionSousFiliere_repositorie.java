package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.InscriptionSousFilieres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionSousFiliere_repositorie extends JpaRepository<InscriptionSousFilieres, Long> {

    List<InscriptionSousFilieres> findByIdSousFiliereId(long idSousFiliere);

    InscriptionSousFilieres findByIdSousFiliereIdAndIdInscriptionId(long idSousFiliereId, long idInscription);
}
