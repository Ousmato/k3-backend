package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.TeachersPresence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ListPresenceTeacher_repositorie extends JpaRepository<TeachersPresence, Long> {
    List<TeachersPresence> findByObservation(boolean isPresents);
    List<TeachersPresence> getByIdSeanceId(long idSeance);
    TeachersPresence findByIdSeanceId(long idSeance);

    List<TeachersPresence> findByObservationAndIdSeanceIdTeacher(boolean isP, Teachers idTeacher);

    //    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, s.heureDebut, s.heureFin) / 60.0) FROM Seances AS s JOIN s.idTeacher t WHERE t.idEnseignant = :idT", nativeQuery = true)
//    Double findTotalHoursByTeacher(@Param("idT") Long idT);
    TeachersPresence findByIdSeanceIdAndIdSeanceIdTeacherIdEnseignant(long idSeance, long idTeacher);

    List<TeachersPresence> findByObservationAndIdSeanceDateBetween(boolean isPre, LocalDate debut, LocalDate fin);
    Page<TeachersPresence> getByObservationAndIdSeanceDateBetween(boolean isPre, LocalDate debut, LocalDate fin, Pageable pageable);

    TeachersPresence findByIdSeanceIdAndIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(long idSeance,long idTeacher, LocalDate date, LocalTime heureFin);

    TeachersPresence findByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(long idTeacher, LocalDate date, LocalTime heufin);

   List<TeachersPresence> getByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateBetween(long idTeacher, LocalDate startDate, LocalDate endDate);

}

