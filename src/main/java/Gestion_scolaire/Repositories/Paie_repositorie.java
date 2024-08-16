package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Paie;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface Paie_repositorie extends JpaRepository<Paie, Long> {

    Paie getByIdPresenceTeachersId(long idPresenceTeachers);

    Paie findByDateAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(LocalDate date, long idTeacher);
    List<Paie> findAllByIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(long id);

    List<Paie> findByDateBetween(LocalDate debut, LocalDate fin);

    Page<Paie> findByDateBetween(LocalDate debut, LocalDate fin, Pageable pageable);

    List<Paie> getByDateBetweenAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(LocalDate debut, LocalDate fin, long id);



}
