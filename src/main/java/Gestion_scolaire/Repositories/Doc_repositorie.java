package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Documents;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Doc_repositorie extends JpaRepository<Documents, Long> {
    Documents findByTitreAndDateAndIdSeanceId(String titre, LocalDate date, long idSeance);
    Documents findByIdSeanceId(long id);
   List<Documents> findByIdSeanceIdEmploisDateFin( LocalDate dafin);
   Documents findById(long idDoc);
}
