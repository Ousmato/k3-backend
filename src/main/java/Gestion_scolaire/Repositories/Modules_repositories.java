package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Modules_repositories extends JpaRepository<Modules, Long> {
    List<Modules> findByIdUeId(long id);

    Modules findByIdUeIdAndId(long idUe, long id);
    Modules findByIdUeAndNomModule(UE idUe, String nom);



    Modules findById(long id);

    @Query("SELECT DISTINCT m FROM Modules m INNER JOIN Emplois e ON e.idModule.id = m.id WHERE e.idClasse.id =:idClasse AND e.idSemestre.id =:idSemestre AND e.dateFin <= :date")
    List<Modules> findModulesWithEmplois(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre, @Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Modules m WHERE m.id NOT IN  " + "(SELECT  e.idModule.id FROM Emplois e WHERE e.idSemestre.id = :idSemestre AND e.idClasse.id = :idClasse and m.id = e.idModule.id)")
    List<Modules> allModulesHasNotProgram(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre);


    @Query("select distinct m from Modules m INNER JOIN Notes n ON n.idModule.id = m.id where n.idInscription.id =:idStudent and n.idSemestre.id =:idSemestre")
    List<Modules> allModuleWithNote(@Param("idStudent") long idStudent, @Param("idSemestre") long idSemestre);

    @Query("SELECT DISTINCT m FROM Modules m INNER JOIN Emplois e ON  e.idClasse.idFiliere.id = :idClasse where e.idModule.id = m.id")
    List<Modules> testQuery(@Param("idClasse") long idClasse);

    @Query("SELECT DISTINCT m from Modules m INNER JOIN ClasseModule cm ON  cm.idUE.id = m.idUe.id WHERE cm.idSemestre.id =:idSemestre and cm.idNiveauFiliere.id =:idNivFiliere")
    List<Modules> allModulesOfClassBySemestre(@Param("idSemestre") long idSemestre, @Param("idNivFiliere") long idNivFiliere);


}
