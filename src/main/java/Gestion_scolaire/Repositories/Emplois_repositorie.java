package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Emplois;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Emplois_repositorie extends JpaRepository<Emplois, Long> {
    Emplois findByIdClasseIdAndDateDebutAndDateFin(long idclasse, LocalDate dateDebit, LocalDate dateFin);
//    Emplois findByIdClasseId(long idClass);
    Emplois findById(long id);
    @Query("select e from Emplois AS e where e.dateFin > :date")
    List<Emplois> findAllEmploisActif(LocalDate date);

    @Query("select e from Emplois AS e where e.dateFin < :date")
    Emplois findEmploisActif(LocalDate date);

    @Query("select e from Emplois AS e where e.dateFin > :date AND e.idClasse.id = :idClasse")
    List<Emplois> findEmploisActifByIdClass(LocalDate date, long idClasse);

    Emplois getEmploisByDateFinAfterAndId(LocalDate date, long id);

    Emplois findByIdClasseId(long idClasse);

    List<Emplois> getByIdSemestreId(long idSemestre);

    boolean existsByIdClasseIdAndDateFinIsAfter(long id, LocalDate dateFin);

    Emplois findByIdClasseIdAndDateFinIsAfter(long id, LocalDate dateFin);
}
