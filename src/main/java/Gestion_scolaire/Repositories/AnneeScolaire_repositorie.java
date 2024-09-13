package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnneeScolaire_repositorie extends JpaRepository<AnneeScolaire, Long> {

    List<AnneeScolaire> findByFinAnneeBetween(LocalDate from, LocalDate to);

    AnneeScolaire findById(long id);

    @Query("SELECT a FROM AnneeScolaire a WHERE (:debutAnnee < a.finAnnee AND :finAnnee > a.debutAnnee)")
    List<AnneeScolaire> findOverlappingYears(@Param("debutAnnee") LocalDate debutAnnee, @Param("finAnnee") LocalDate finAnnee);

}
