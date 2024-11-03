package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.StudentsClasse;
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

    @Query("SELECT c  FROM ClasseModule c WHERE YEAR (c.idSemestre.datFin ) = :year")
    List<ClasseModule> getCurrentClasseModule(@Param("year") int year);


    List<ClasseModule> findByIdNiveauFiliereId(long idNivFil);

    List<ClasseModule> getClasseModuleByIdUEId(long idUE);

    ClasseModule  getClasseModuleByIdNiveauFiliereIdAndIdSemestreId(long idStudentClasse, long idSemestre);

//    List

    List<ClasseModule> getAllByIdNiveauFiliereIdAndIdSemestreId(long idNiveauFiliere, long idSemestre);

}
