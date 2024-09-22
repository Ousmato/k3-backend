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
    int countAllByFermer(boolean fermer);
    StudentsClasse findByIdFiliereIdAndIdFiliereIdNiveauId(long idFiliereId, long idFiliereIdNiveauId);
    List<StudentsClasse> findStudentsClasseById(long idclasse);

    List<StudentsClasse> findByIdAnneeScolaireId(long idanneeScolaireId);

    List<StudentsClasse>  findByIdFiliereIdNiveauId(long idNiveau);

    @Query("SELECT c FROM StudentsClasse c WHERE YEAR(c.idAnneeScolaire.finAnnee) = :year AND c.idFiliere.idFiliere.id = :idFiliereId")
    List<StudentsClasse> findByIdFiliereIdFiliereIdAndNextYear(@Param("year") int year, @Param("idFiliereId") long idFiliereId);

    List<StudentsClasse> findByIdFiliereId(long idFiliere);

    StudentsClasse findByIdFiliereIdAndIdAnneeScolaireId(long idFiliereId, long idAnneeScolaireId);

    @Query("SELECT c  FROM StudentsClasse c WHERE YEAR (c.idAnneeScolaire.finAnnee ) = :year")
    List<StudentsClasse> getClasseForCurrentYear(@Param("year") int year);


    @Query("SELECT c  FROM StudentsClasse c WHERE YEAR (c.idAnneeScolaire.finAnnee ) < :year and c.idFiliere.id =:idClasse")
    List<StudentsClasse> getAllArchivesByIdClasse(@Param("year") int year, @Param("idClasse") long idClasse);

}
