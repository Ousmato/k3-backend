package Gestion_scolaire.Emplois.repositorie;

import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.EnumClasse.Seance_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Emplois_repositorie extends JpaRepository<Emplois, Long> {
    Emplois findById(long id);


    @Query("select e from Emplois AS e where e.idClasse.id =:idClass and e.idSemestre.id =:idSemestre and e.dateFin > :date")
    List<Emplois> findAllOldEmploisOfClassBySemestre( @Param("idClass") long idClass, @Param("idSemestre") long idSemestre, @Param("date") LocalDate date);

    @Query("select e from Emplois AS e where e.idClasse.id =:idClasse")
    List<Emplois> findEmploisInActif( @Param("idClasse") long idClasse);

    @Query("select e from Emplois AS e where e.dateFin > :date AND e.idClasse.id = :idClasse")
    List<Emplois> findEmploisActifByIdClass(LocalDate date, long idClasse);

    @Query("SELECT e from Emplois e inner JOIN Journee j on e.id =j.idEmplois.id and j.intervenant.id =:idTeacher and e.idClasse.idAnneeScolaire.id =:idAnnee")
    List<Emplois> getAllEmploiByOfTeacherAndIdAnnee(@Param("idAnnee") long idAnnee, @Param("idTeacher") long idTeacher);

    @Query("SELECT e from Emplois e inner JOIN Journee j on e.id =j.idEmplois.id and e.idClasse.idAnneeScolaire.id =:idAnnee")
    List<Emplois> getAllEmploiByOfTeachersByIdAnnee(@Param("idAnnee") long idAnnee);


    Emplois findByIdClasseId(long idClasse);
    @Query("select e from Emplois e inner JOIN Journee j on j.idEmplois.id = e.id where j.intervenant.id =:idTeacher")
    List<Emplois> getByAllEmploiByIdTeacher(@Param("idTeacher") long idTeacher);

    @Query("select e from Emplois e where not exists " +
            "(select j from Journee j where j.idEmplois.id = e.id and j.seanceType =:exam) " +
            "and exists (select jr from Journee jr where jr.idEmplois.id = e.id)and e.dateDebut > current_date ")
    List<Emplois> findEmploisActifWithouExam(@Param("exam") Seance_type exam);

    @Query("select e from Emplois e where exists (select j from Journee j where j.idEmplois.id = e.id)")
    List<Emplois> findEmploisWithAtLeastOneJournee();

    @Query("select e from Emplois e where not exists (select j from Journee j where j.idEmplois.id = e.id)")
    List<Emplois> findEmploisWithoutJournee();

}
