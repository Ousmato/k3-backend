package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentsPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface StudentsPresene_repositorie extends JpaRepository<StudentsPresence, Long> {
    StudentsPresence findByIdSeanceAndIdStudentsAndStatus(Seances idSeance, Studens idStudent, boolean isOb);
   List<StudentsPresence> findByStatusAndDateBetween(boolean isPre, LocalDate debut, LocalDate fin);
    List<StudentsPresence> findByIdSeanceId(long idSeance);
    StudentsPresence findByIdStudentsIdEtudiant(long id);

}
