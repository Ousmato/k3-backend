package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.StudentsClasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Classe_repositorie extends JpaRepository<StudentsClasse, Long> {

    StudentsClasse findById(long id);
    @Query("select  count(c) from StudentsClasse c where  YEAR (c.idAnneeScolaire.finAnnee) =:year and c.fermer =:isFermer")
    int countAllByFermer(@Param("year") int year, @Param("isFermer") boolean isFermer);

    StudentsClasse findByIdFiliereIdAndIdFiliereIdNiveauId(long idFiliereId, long idFiliereIdNiveauId);
    List<StudentsClasse> findStudentsClasseById(long idclasse);

    List<StudentsClasse> findByIdFiliereIdNiveauNom(String nom);

    List<StudentsClasse> findByIdAnneeScolaireId(long idanneeScolaireId);

    List<StudentsClasse>  findByIdFiliereIdNiveauId(long idNiveau);

    @Query("SELECT c FROM StudentsClasse c WHERE YEAR(c.idAnneeScolaire.finAnnee) = :year AND c.idFiliere.idFiliere.id = :idFiliereId AND c.idFiliere.idNiveau.id = :idNiveau")
   List<StudentsClasse> findByIdFiliereIdFiliereIdAndNextYear(@Param("year") int year, @Param("idFiliereId") long idFiliereId, @Param("idNiveau") long idNiveau);

    List<StudentsClasse> findByIdFiliereId(long idFiliere);

    StudentsClasse findByIdFiliereIdAndIdAnneeScolaireId(long idFiliereId, long idAnneeScolaireId);

    @Query("SELECT c  FROM StudentsClasse c WHERE YEAR (c.idAnneeScolaire.finAnnee ) = :year")
    List<StudentsClasse> getClasseForCurrentYear(@Param("year") int year);


    @Query("SELECT c  FROM StudentsClasse c WHERE YEAR (c.idAnneeScolaire.finAnnee ) < :year and c.idFiliere.id =:idClasse")
    List<StudentsClasse> getAllArchivesByIdClasse(@Param("year") int year, @Param("idClasse") long idClasse);

    StudentsClasse getByIdAndIdFiliereIdNiveauId(long idClasse, long idFiliereIdNiveauId);
}
