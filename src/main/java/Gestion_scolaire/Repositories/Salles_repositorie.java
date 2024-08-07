package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Salles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Salles_repositorie extends JpaRepository<Salles, Long> {

    Salles findByNom(String name);


}
