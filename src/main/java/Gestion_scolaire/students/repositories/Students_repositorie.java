package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Students_repositorie extends JpaRepository<Students, Long> {
   Students findByEmailAndPassword(String email, String password);
   Students findByEmail(String password);

   @Query("SELECT i FROM Students i WHERE i.idEtudiant IN :ids")
   List<Students> getStudentsNotInscribed(@Param("ids") List<Long> ids);
   Students findByMatriculeAndTelephone(String matricule, String telephone);

   Students findByTelephone(String telephone);

   Students findByIdEtudiant (long id);
}
