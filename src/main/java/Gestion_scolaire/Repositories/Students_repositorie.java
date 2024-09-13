package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Studens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Students_repositorie extends JpaRepository<Studens, Long> {
   Studens findByEmailAndPassword(String email, String password);
   Studens findByEmail(String password);

   Studens findByMatriculeAndTelephone(String matricule, int telephone);

   Page<Studens> getByIdClasseIdAnneeScolaireId(long anneeScolaireId, Pageable pageable);

   List<Studens> getByIdClasseIdAnneeScolaireId(long anneeScolaireId);

   Studens findByIdEtudiant (long id);

   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) AND e.idClasse.id = :idClasse AND e.active = :isActive")
   List<Studens> findByIdClasseIdAndActive(@Param("idClasse") long idClasse, @Param("isActive") boolean isActive);


   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) AND e.idClasse.id = :idClasse AND e.payer = :isPayer")
   List<Studens> getByIdClasseIdAndPayer(@Param("idClasse") long idClasse, @Param("isPayer") boolean isPayer);


   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) AND e.idClasse.id = :idClasse")
   Page<Studens> findByIdClasseId(@Param("idClasse") long idClasse, Pageable pageable);

   @Query("SELECT SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
   double sumScolariteForCurrentYear();

   @Query("SELECT SUM(e.idClasse.scolarite) -SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
   double getReliquatForCurrentYear();

   @Query("SELECT SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
   double cunt();

   int countAllByPayer(boolean payer);


   Page<Studens> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long idAnneeScolaireId, long idClasseId, Pageable pageable);

}
