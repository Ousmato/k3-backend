package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface Soutenance_repositorie extends JpaRepository<Soutenance, Long> {

   Soutenance getByHeureDebutAndHeureFinAndDate(LocalTime heureDebut, LocalTime heureFin, LocalDate date);

   List<Soutenance> getByDateBetween(LocalDate start, LocalDate end);

   @Query(value = "SELECT s from  Soutenance s WHERE s.date >= :currentDate")
   List<Soutenance> getByDate(@Param("currentDate") LocalDate currentDate);

   List<Soutenance> findByDate(LocalDate date);

   Soutenance findById(long idSoutenance);

   Soutenance findByIdDocId(long idDoc);

   Soutenance getByIdDocIdDocumentId(long idDoc);

//   Soutenance getByDateAndIdDocIdAndIdTeacherIdEnseignant(LocalDate date, long idDocId, long idTeacherId);
}
