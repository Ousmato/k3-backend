package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Semestres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface Semestre_repositorie extends JpaRepository<Semestres, Long> {
    Semestres findById(long id);
    Semestres findByNomSemetreAndIdAnneeScolaireId(String name, long idAnneeScolaire);

    @Query("SELECT s FROM Semestres s WHERE (:dateDebut BETWEEN s.dateDebut AND s.datFin OR :dateFin BETWEEN s.dateDebut AND s.datFin)")
    Semestres getByDateRangeOverlap(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);


    @Query("SELECT s FROM Semestres s WHERE :date BETWEEN s.dateDebut AND s.datFin")
    Semestres getCurrentSemestre(@Param("date") LocalDate date);

    Semestres getByDatFinBetweenAndIdAnneeScolaireId(LocalDate start, LocalDate end, Long id);
}
