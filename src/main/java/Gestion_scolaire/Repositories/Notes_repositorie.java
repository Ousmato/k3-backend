package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Notes_repositorie extends JpaRepository<Notes, Long> {
    Notes findByIdInscriptionIdEtudiantIdEtudiantAndIdModuleIdAndIdSemestreId(long idStudent, long idModule, long idSemestre);

    List<Notes> findByIdInscriptionIdEtudiantIdEtudiantAndIdSemestreId(long idStudent, long idSemestre);

//    @Query(value = "SELECT SUM(n.coefficient) FROM Notes n WHERE n.idStudents.idEtudiant = :idStudent", nativeQuery = true)
//    int findTotalCoefByIdStudent(@Param("idStudent") long idStudent);

//    @Query(value = "SELECT SUM(n.examNote) FROM Notes n WHERE n.idInscription.idEtudiant.idEtudiant = :idStudent",nativeQuery = true)
//    int findTotalNoteByIdStudent(@Param("idStudent") long idStudent);
//
//    @Query(value = "SELECT SUM(n.classeNote) FROM Notes n WHERE n.idStudents.idEtudiant = :idStudent", nativeQuery = true)
//    int findTotalClassByIdStudent(@Param("idStudent") long idStudent);

    Notes findById(long idNote);

    List<Notes> findByIdSemestreIdAndClasseNoteAndExamNote(long idSemestre, double classNote, double examenNote);

    List<Notes> findByIdSemestreId(long idSemeatre);
    List<Notes> getByIdSemestreIdAndIdInscriptionId(long idSemestre, long idStudent);

//    Notes findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(long idSemestre,long idModule, long idStudent);

    Notes findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(long idSemestre, long idStudent, long idModule);
//    List<Notes> getByIdSemestreIdAndIdStudentsIdClasseId(long idSemestre, long idClass);

//    Notes findByIdSemestreIdAndIdModuleId(long idSemestre, long idModule)
    List<Notes> getByIdSemestreIdAndIdModuleId(long idSemestre, long idModule);

//    Page<Notes> getByIdSemestreIdAndIdStudentsIdClasseId(long idSemestre, long idStudent, Pageable pageable);
}
