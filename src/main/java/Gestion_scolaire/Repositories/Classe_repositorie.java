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

    List<StudentsClasse> findByIdFiliereId(long idFiliere);

    StudentsClasse findByIdFiliereIdAndIdAnneeScolaireId(long idFiliereId, long idAnneeScolaireId);

    @Query("SELECT c  FROM StudentsClasse c WHERE c.idAnneeScolaire.finAnnee > :date")
    List<StudentsClasse> getClasseForCurrentYear(@Param("date") LocalDate date);

}
