package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.entity.StudentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Sessions_repositorie extends JpaRepository<StudentSession, Long> {

    StudentSession findByIdInscritIdAndIdSemestreIdAndIdModuleId(long idInscrit, long idSemestre, long idModule);

    StudentSession getByIdInscritIdAndIdSemestreIdAndIdModuleIdUeId(long idInscrit, long idSemestre, long idUe);

}
