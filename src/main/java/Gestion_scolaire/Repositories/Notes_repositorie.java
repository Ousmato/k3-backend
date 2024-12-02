package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Notes getNotesByIdModuleIdAndIdInscriptionIdAndIdSemestreId(long idModule, long idInscrit, long idSemestre);

    @Query("SELECT COUNT(n) > 0 FROM Notes n WHERE n.idModule.idUe.id = :idUe")
    boolean existsNotesByUeId(@Param("idUe") long idUe);

    Notes findById(long idNote);

    List<Notes> findByIdSemestreIdAndClasseNoteAndExamNote(long idSemestre, double classNote, double examenNote);

    List<Notes> findByIdSemestreId(long idSemeatre);
    List<Notes> getByIdSemestreIdAndIdInscriptionId(long idSemestre, long idStudent);


//    Notes findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(long idSemestre,long idModule, long idStudent);

    Notes findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(long idSemestre, long idStudent, long idModule);

    @Query("SELECT n from Notes n WHERE n.idSemestre.id =:idSemestre and n.idInscription.idClasse.id =:idClass")
    List<Notes> getByIdSemestreIdAndIdClasseId(@Param("idSemestre") long idSemestre, @Param("idClass") long idClass);

    List<Notes> getByIdSemestreIdAndIdModuleId(long idSemestre, long idModule);

}
