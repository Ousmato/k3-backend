package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Inscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Inscription_repositorie extends JpaRepository<Inscription, Long> {
    Page<Inscription> getByIdClasseIdAnneeScolaireId(long anneeScolaireId, Pageable pageable);

    Inscription findById(long id);

    Inscription getByIdAndIdClasseId(long id, long classeId);

    Inscription findByIdEtudiantIdEtudiant(long etudiantId);

    Inscription getByIdEtudiantIdEtudiantAndIdClasseId(long etudiantId, long idFiliere);

    List<Inscription> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long anneeScolaireId, long classeId);

    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE) AND i.idClasse.id = :idClasse AND i.active = :isActive")
    List<Inscription> findByIdClasseIdAndActive(@Param("idClasse") long idClasse, @Param("isActive") boolean isActive);

    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE) AND i.idClasse.id = :idClasse AND i.payer = :isPayer")
    List<Inscription> getByIdClasseIdAndPayer(@Param("idClasse") long idClasse, @Param("isPayer") boolean isPayer);

    @Query("SELECT i FROM Inscription i WHERE  i.idClasse.id = :idClasse")
    Page<Inscription> findByIdClasseId(@Param("idClasse") long idClasse, Pageable pageable);

    @Query("SELECT SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date )")
    double sumScolariteForCurrentYear();

    @Query("SELECT SUM(i.idClasse.idFiliere.scolarite) -SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date )")
    double getReliquatForCurrentYear();

    @Query("SELECT COUNT(c) from Inscription c WHERE YEAR(c.date) = YEAR(current_date) and c.payer =:payer")
    int countAllByPayer(@Param("payer") boolean payer);

    int countAllByIdClasseIdAndIdClasseIdAnneeScolaireId(long idClasseId, long idAnneeScolaireId);

    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE)")
    Page<Inscription> findStudentOfCurrentYear( Pageable pageable);


    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE) and i.payer =:isActive")
    Page<Inscription> findStudentOfCurrentYearByEtat( Pageable pageable, boolean isActive);

    Page<Inscription> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long idAnneeScolaireId, long idClasseId, Pageable pageable);

}
