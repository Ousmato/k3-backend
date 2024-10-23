package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.Models.StudentDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentDoc_repositorie extends JpaRepository<StudentDoc, Long> {

    List<StudentDoc> findAllByIdInscriptionIdClasseId(long idClasse);

    StudentDoc findById(long id);

    Page<StudentDoc> getByIdDocumentDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    List<StudentDoc> findByIdDocumentId(long idDocument);

    Page<StudentDoc> getAllByIdInscriptionIdClasseIdAnneeScolaireId(long idAnnee, Pageable pageable);

    StudentDoc findByIdDocumentDocTypeAndIdInscriptionIdEtudiantIdEtudiant(DocType docType, long idEtudiant);

}
