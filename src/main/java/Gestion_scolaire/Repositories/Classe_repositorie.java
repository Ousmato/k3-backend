package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.StudentsClasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Classe_repositorie extends JpaRepository<StudentsClasse, Long> {

    StudentsClasse findById(long id);
    StudentsClasse findByScolarite(double scolarite);
    StudentsClasse findByIdFiliereIdAndIdFiliereIdNiveauId(long idFiliereId, long idFiliereIdNiveauId);
    List<StudentsClasse> findStudentsClasseById(long idclasse);

}
