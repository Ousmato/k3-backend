package Gestion_scolaire.students.repositories;

import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.students.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Doc_repositorie extends JpaRepository<Documents, Long> {

   Documents findById(long idDoc);

   int countByDocType(DocType docType);

}
