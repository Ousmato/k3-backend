package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.Journee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface Journee_repositorie extends JpaRepository<Journee, Long> {
    List<Journee> findByIdEmploisId(long id);


    boolean existsByIdEmploisId(long idEmplois);

    Journee getById(long idJournee);

    List<Journee> getAllByDate(LocalDate date);

    Journee findByDateAndIdEmploisIdAndIdTeacherIdEnseignantAndHeureFin(LocalDate date, long idEmplois, long idTeacher, LocalTime heureFin);


    List<Journee> findAllByIdInAndIdTeacherIdEnseignantIn(Collection<Long> idSeance_id, Collection<Long> idTeacher_idEnseignant);

    Journee getByHeureDebutAndHeureFinAndDateAndIdEmploisId(LocalTime heureDebut, LocalTime heureFin, LocalDate date, long idEmploi);

    Journee findByIdTeacherIdEnseignantAndSeanceTypeAndDate( long idTeacherId, Seance_type seanceType, LocalDate date);

    @Query("SELECT j FROM Journee j WHERE (j.seanceType = :exam OR j.seanceType = :session) AND j.idEmplois.dateFin > :currentDate")
    List<Journee> findBySeanceTypeAndDate(@Param("exam") Seance_type exam, @Param("session") Seance_type session, @Param("currentDate") LocalDate currentDate);

    Journee getByHeureDebutAndHeureFinAndId(LocalTime heureDebut, LocalTime heureFin, long Id);

    @Query("SELECT j FROM Journee j WHERE j.idTeacher.idEnseignant = :idTeacher AND j.idEmplois.dateFin > :currentDate")
    List<Journee> findAll_ByIdTeacher(@Param("idTeacher") long idTeacher, @Param("currentDate") LocalDate currentDate);


    List<Journee> findByIdEmploisIdAndIdTeacherIdEnseignant(long idEmplois, long idTeacher);

    List<Journee> findByIdEmploisIdAndIdTeacherIdEnseignantAndSeanceType(long idEmplois, long idTeacher, Seance_type seanceType);
    List<Journee>  getAllByDateAndIdTeacherIdEnseignant(LocalDate date, long idTeacher);

    List<Journee> getByIdEmploisIdModuleId(long idModule);


    @Query("SELECT j FROM Journee j WHERE j.idSalle.id = :idSalle AND j.date = :currentDate AND j.heureDebut <= :hActuel AND j.heureFin >= :hActuel")
    List<Journee> getAllByIdSalle_Id(@Param("idSalle") long idSalle, @Param("currentDate") LocalDate currentDate, @Param("hActuel") LocalTime hActuel);

    @Query("SELECT j FROM Journee j WHERE j.idSalle.id = :idSalle AND j.date = :currentDate")
    List<Journee> getJoureeActif(@Param("idSalle") long idSalle, @Param("currentDate") LocalDate currentDate);
}
