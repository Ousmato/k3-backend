package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.ClasseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseModule_repositorie extends JpaRepository<ClasseModule, Long> {
    List<ClasseModule> findAllByIdNiveauFiliereId(long id);
    ClasseModule findByIdNiveauFiliereIdAndIdUEId(long idClasse, long idUE);

    @Query("SELECT cm FROM ClasseModule cm   WHERE cm.idNiveauFiliere.id = :id")
    ClasseModule findStudentsClasseWithUEsById(@Param("id") long id);

    List<ClasseModule> findByIdNiveauFiliereId(long idClasse);

    List<ClasseModule> getClasseModuleByIdUEId(long idUE);

    ClasseModule  getClasseModuleByIdNiveauFiliereId(long idStudentClasse);

   // List<ClasseModule> getAllByI

}
