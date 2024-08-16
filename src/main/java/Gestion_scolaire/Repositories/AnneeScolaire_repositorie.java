package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AnneeScolaire_repositorie extends JpaRepository<AnneeScolaire, Long> {

    AnneeScolaire findByFinAnneeBetween(LocalDate from, LocalDate to);

    AnneeScolaire findById(long id);
}
