package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface Seance_repositorie extends JpaRepository<Seances, Long> {
    List<Seances> findByIdEmploisId(long id);
    Seances findByIdEmplois(Emplois idEmplois);

    boolean existsByIdEmploisId(long idEmplois);

    Seances getById(long idSeance);

//    Seances getByDateAndIdModuleIdAndIdEmploisId(LocalDate date, long idModule, long idEmpl);

    Seances findByHeureDebutAndHeureFinAndDate(LocalTime debut, LocalTime fin, LocalDate date);

//    @Query("SELECT s FROM Seances s WHERE s.idTeacher.idEnseignant = :idTeacher AND s.idEmplois.dateFin > :currentDate")
//    List<Seances> findAll_ByIdTeacher(@Param("idTeacher") long idTeacher, @Param("currentDate") LocalDate currentDate);

//    @Query( value = "SELECT TIMESTAMPDIFF(MINUTE, s.heure_debut, s.heure_fin)/60 FROM seances AS s WHERE s.id_teacher_id_enseignant =:idT AND s.id =:idS;", nativeQuery = true)
//    int findTotalHoursByTeacher(@Param("idT") long idT, @Param("idS") long idS);
//
//    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, s.heureDebut, s.heureFin) / 60.0) FROM Seances AS s WHERE s.idTeacher.idEnseignant = :idTeacher AND MONTH(s.date) = :month", nativeQuery = true)
//    List<Integer> findNbreHeureBySeanceIdTeacher(@Param("idTeacher") long idTeacher, @Param("month") int month);
//
//
//    List<Seances>  getAllByDateAndIdTeacherIdEnseignant(LocalDate date, long idTeacher);

    List<Seances> getAllByDate(LocalDate date);

    Seances findByDateAndIdEmploisId(LocalDate date, long idEmplois);

//    @Query("SELECT s FROM Seances s WHERE s.idSalle.id = :idSalle AND s.date = :currentDate")
//    List<Seances> getAllByIdSalle_Id(@Param("idSalle") long idSalle, LocalDate currentDate);

}
