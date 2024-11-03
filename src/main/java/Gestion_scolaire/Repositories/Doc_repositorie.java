package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.Models.Documents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Doc_repositorie extends JpaRepository<Documents, Long> {

   Documents findById(long idDoc);

   int countByDocType(DocType docType);

}
