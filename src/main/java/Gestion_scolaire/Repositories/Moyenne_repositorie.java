package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Moyenne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Moyenne_repositorie extends JpaRepository<Moyenne, Long> {
}
