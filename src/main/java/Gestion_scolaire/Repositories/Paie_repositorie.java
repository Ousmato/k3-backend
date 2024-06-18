package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Paie;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface Paie_repositorie extends JpaRepository<Paie, Long> {
    Paie findByDateAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(LocalDate date, long idTeacher);
    List<Paie> findAllByIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(long id);

    List<Paie> findByDateBetween(LocalDate debut, LocalDate fin);



}
