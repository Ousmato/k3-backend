package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseModule_repositorie extends JpaRepository<ClasseModule, Long> {
    List<ClasseModule> findAllByIdStudentClasseId(long id);
    ClasseModule findByIdStudentClasseIdAndIdUEId(long idClasse, long idUE);
    @Query("SELECT cm FROM ClasseModule cm   WHERE cm.idStudentClasse.id = :id")
    ClasseModule findStudentsClasseWithUEsById(@Param("id") long id);

    List<ClasseModule> findByIdStudentClasseId(long idClasse);

    List<ClasseModule> getClasseModuleByIdUEId(long idUE);


}
