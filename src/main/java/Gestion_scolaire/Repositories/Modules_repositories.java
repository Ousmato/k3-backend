package Gestion_scolaire.Repositories;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Modules_repositories extends JpaRepository<Modules, Long> {

    List<Modules> findByIdUeIdAndActive(long id, boolean active);

    Modules getByIdUeIdAndNomModuleAndActive(long idUe, String nomModule, boolean active);
    Modules findByIdUeAndNomModule(UE idUe, String nom);
    Modules findById(long id);

    @Query("SELECT DISTINCT m FROM Modules m INNER JOIN Emplois e ON e.idModule.id = m.id WHERE e.idClasse.id =:idClasse AND e.idSemestre.id =:idSemestre AND e.dateFin <= :date")
    List<Modules> findModulesWithEmplois(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre, @Param("date") LocalDate date);



    @Query("SELECT DISTINCT m FROM Modules m WHERE m.id NOT IN  " + "(SELECT  e.idModule.id FROM Emplois e WHERE e.idSemestre.id = :idSemestre AND e.idClasse.id = :idClasse and m.id = e.idModule.id and m.active = true )")
    List<Modules> allModulesHasNotProgram(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre);


    @Query("select distinct m from Modules m INNER JOIN Notes n ON n.idModule.id = m.id where n.idInscription.id =:idStudent and n.idSemestre.id =:idSemestre and m.active = true ")
    List<Modules> allModuleWithNote(@Param("idStudent") long idStudent, @Param("idSemestre") long idSemestre);

    @Query("SELECT DISTINCT m from Modules m INNER JOIN UE ue ON  ue.id = m.idUe.id WHERE ue.idSemestre.id =:idSemestre and ue.idClasse.id =:idClasse and m.active = true")
    List<Modules> allModulesOfClassBySemestre(@Param("idSemestre") long idSemestre, @Param("idClasse") long idClasse);


}
