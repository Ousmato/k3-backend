package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.ClasseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseModule_repositorie extends JpaRepository<ClasseModule, Long> {
    List<ClasseModule> findAllByIdStudentClasseId(long id);
    ClasseModule findByIdStudentClasseIdAndIdUEId(long idClasse, long idUE);
}
