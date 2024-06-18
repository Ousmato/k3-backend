package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Studens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Students_repositorie extends JpaRepository<Studens, Long> {
   Studens findByEmailAndPassword(String email, String password);
   Studens findByEmail(String password);
   Studens findByMatricule(String matricule);
   Studens findByIdEtudiant (long id);
//   List<Studens> findByDeleted(boolean t);
   List<Studens> findByIdClasseIdAndActive(long idClasse, boolean isActive);
   List<Studens> findByActive(boolean Active);

}
