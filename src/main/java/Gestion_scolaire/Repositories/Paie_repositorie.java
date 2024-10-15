package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Paie;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface Paie_repositorie extends JpaRepository<Paie, Long> {

    List<Paie> findByDateBetween(LocalDate debut, LocalDate fin);

    @Query("SELECT SUM(p.nbreHeures) FROM Paie p WHERE p.journee.idTeacher.idEnseignant = :idTeacher")
    int findTotalHoursByTeacherId(@Param("idTeacher") long idTeacher);

    Page<Paie> findByDateBetween(LocalDate debut, LocalDate fin, Pageable pageable);

    List<Paie> getByDateBetweenAndJourneeIdTeacherIdEnseignant(LocalDate debut, LocalDate fin, long id);

}
