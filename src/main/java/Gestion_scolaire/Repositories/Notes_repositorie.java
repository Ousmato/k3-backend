package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Notes_repositorie extends JpaRepository<Notes, Long> {
    Notes findByIdStudentsIdEtudiantAndIdModuleIdAndIdSemestreId(long idStudent, long idModule,long idSemestre);
    List<Notes> findByIdStudentsIdEtudiantAndIdSemestreId(long idStudent, long idSemestre);

    @Query(value = "SELECT SUM(n.coefficient) FROM Notes n WHERE n.idStudents.idEtudiant = :idStudent", nativeQuery = true)
    int findTotalCoefByIdStudent(@Param("idStudent") long idStudent);

    @Query(value = "SELECT SUM(n.examNote) FROM Notes n WHERE n.idStudents.idEtudiant = :idStudent",nativeQuery = true)
    int findTotalNoteByIdStudent(@Param("idStudent") long idStudent);

    @Query(value = "SELECT SUM(n.classeNote) FROM Notes n WHERE n.idStudents.idEtudiant = :idStudent", nativeQuery = true)
    int findTotalClassByIdStudent(@Param("idStudent") long idStudent);

    Notes findById(long idNote);

    List<Notes> findByIdSemestreIdAndClasseNoteAndExamNote(long idSemestre, double classNote, double examenNote);

    List<Notes> findByIdSemestreId(long idSemeatre);
    List<Notes> getByIdSemestreIdAndIdStudentsIdEtudiant(long idSemestre, long idStudent);

    List<Notes> getByIdSemestreIdAndIdStudentsIdClasseId(long idSemestre, long idClass);

    List<Notes> getByIdSemestreIdAndIdModuleId(long idSemestre, long idModule);

    Page<Notes> getByIdSemestreIdAndIdStudentsIdClasseId(long idSemestre, long idStudent, Pageable pageable);
}
