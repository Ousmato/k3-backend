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

    List<StudentDoc> findAllByIdInscriptionIdClasseIdAndIdDocumentDeleted(long idClasse, boolean isNodelete);

    StudentDoc findById(long id);

    Page<StudentDoc> getByIdDocumentDeletedAndIdDocumentDateBetween(boolean noDeleted, LocalDate start, LocalDate end, Pageable pageable);

    List<StudentDoc> findByIdDocumentId(long idDocument);

    List<StudentDoc> findByIdDocumentIdAndIdDocumentProgrammer(long idDocument, boolean isNoProgram);

    StudentDoc findByIdInscriptionIdAndIdDocumentDeleted(long idInscription, boolean isDeleted);

    Page<StudentDoc> getAllByIdInscriptionIdClasseIdAnneeScolaireIdAndIdDocumentDeleted(long idAnnee, Pageable pageable, boolean noDeleted);

    StudentDoc findByIdDocumentDocTypeAndIdInscriptionId(DocType docType, long idInscription);

}
