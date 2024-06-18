package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Emplois;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Emplois_repositorie extends JpaRepository<Emplois, Long> {
    Emplois findByIdClasseModuleAndDateDebutAndDateFin(ClasseModule idclasse, LocalDate dateDebit, LocalDate dateFin);
    Emplois findByIdClasseModule(ClasseModule idClasse);
    Emplois findById(long id);
    @Query("select e from Emplois AS e where e.dateFin < :date")
    List<Emplois> findAllEmploisActif(LocalDate date);

    @Query("select e from Emplois AS e where e.dateFin < :date")
    Emplois findEmploisActif(LocalDate date);

    @Query("select e from Emplois AS e where e.dateFin < :date AND e.idClasseModule.idStudentClasse.id = :idClasse")
    Emplois findEmploisActifByIdClass(LocalDate date, long idClasse);

    List<Emplois> findByIdClasseModuleIdStudentClasseId(long idClasse);

}
