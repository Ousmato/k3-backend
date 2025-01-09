package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Students_repositorie extends JpaRepository<Students, Long> {
   Students findByEmailAndPassword(String email, String password);
   Students findByEmail(String password);

//   Studens findByIdClasseIdAndMatriculeAndNom(long idClasseId, String matricule, String nom);
   Students findByMatriculeAndTelephone(String matricule, String telephone);

   Students findByTelephone(String telephone);
//
//   Page<Studens> getByIdClasseIdAnneeScolaireId(long anneeScolaireId, Pageable pageable);
//
//   List<Studens> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long anneeScolaireId, long classeId);

   Students findByIdEtudiant (long id);
//
//   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) AND e.idClasse.id = :idClasse AND e.active = :isActive")
//   List<Studens> findByIdClasseIdAndActive(@Param("idClasse") long idClasse, @Param("isActive") boolean isActive);
//
//
//   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) AND e.idClasse.id = :idClasse AND e.payer = :isPayer")
//   List<Studens> getByIdClasseIdAndPayer(@Param("idClasse") long idClasse, @Param("isPayer") boolean isPayer);

//
//   @Query("SELECT e FROM Studens e WHERE  e.idClasse.id = :idClasse")
//   Page<Studens> findByIdClasseId(@Param("idClasse") long idClasse, Pageable pageable);
//
//   @Query("SELECT SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
//   double sumScolariteForCurrentYear();
//
//   @Query("SELECT SUM(e.idClasse.idFiliere.scolarite) -SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
//   double getReliquatForCurrentYear();

//   @Query("SELECT SUM(e.scolarite) FROM Studens e WHERE YEAR (e.date) = YEAR (current_date )")
//   double cunt();
//
//   int countAllByPayer(boolean payer);
//   int countAllByIdClasseIdAndIdClasseIdAnneeScolaireId(long idClasseId, long idAnneeScolaireId);

//
//   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE)")
//   Page<Studens> findStudentOfCurrentYear( Pageable pageable);
//
//   @Query("SELECT e FROM Studens e WHERE YEAR(e.date) = YEAR(CURRENT_DATE) and e.payer =:isActive")
//   Page<Studens> findStudentOfCurrentYearByEtat( Pageable pageable, boolean isActive);
//
//   Page<Studens> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long idAnneeScolaireId, long idClasseId, Pageable pageable);

}
