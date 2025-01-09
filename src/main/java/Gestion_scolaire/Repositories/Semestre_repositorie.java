package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Semestres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Semestre_repositorie extends JpaRepository<Semestres, Long> {
    Semestres findById(long id);
//    @Query("SELECT DISTINCT  s FROM Semestres s INNER  JOIN StudentsClasse sc on sc.idAnneeScolaire.id =:idAnneeScolaire where s.nomSemetre =:name")
//    Semestres findByNomSemetreAndIdAnneeScolaireId(@Param("name") String name, @Param("idAnneeScolaire") long idAnneeScolaire);

//    @Query("SELECT s FROM Semestres s WHERE (:dateDebut BETWEEN s.dateDebut AND s.datFin OR :dateFin BETWEEN s.dateDebut AND s.datFin)")
//    Semestres getByDateRangeOverlap(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    Semestres findByNomSemetre(String nomSemetre);

    @Query("select DISTINCT s from Semestres s inner join UE ue on ue.idSemestre.id = s.id where ue.idClasse.id =:idClasse")
    List<Semestres> getByIdClasse(@Param("idClasse") long idClasse);

//    @Query("SELECT s FROM Semestres s WHERE :date BETWEEN s.dateDebut AND s.datFin")
//    Semestres getCurrentSemestre(@Param("date") LocalDate date);
//
//    @Query("SELECT s FROM Semestres s WHERE YEAR (s.datFin) =:year")
//    List<Semestres> getCurrentSemestreOfYer(@Param("year") int year);

//    Semestres getByDatFinBetweenAndIdAnneeScolaireId(LocalDate start, LocalDate end, Long id);
}
