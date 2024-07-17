package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Niveau_repositorie extends JpaRepository<Niveau, Long> {
    Niveau findByNom(String nom);
    Niveau findById(long idNiveau);
//    Niveau findById(long idNiv);

//   long findById(long id);
}
