package Gestion_scolaire.students.repositories;

import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.students.entity.Inscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Inscription_repositorie extends JpaRepository<Inscription, Long> {
    Page<Inscription> getByIdClasseIdAnneeScolaireId(long anneeScolaireId, Pageable pageable);

    Inscription findById(long id);

    Inscription getByIdAndIdClasseId(long id, long classeId);

    List<Inscription> findByIdEtudiantId(long etudiantId);


    List<Inscription> findByIdClasseIdAnneeScolaireId(long anneeScolaireId);

    @Query("select i from Inscription i WHERE :currentDate BETWEEN  i.idClasse.idAnneeScolaire.debutAnnee and i.idClasse.idAnneeScolaire.finAnnee and i.idEtudiant.id =:idStudent")
    Inscription getInscriptionByCurrentYearAndIdStudent(@Param("currentDate") LocalDate currentDate, @Param("idStudent") long idStudent);

    @Query("select count(*)  from Inscription i where i.idClasse.id =:idClasse")
    int cuntByIdClasse(@Param("idClasse") long idClasse);

    @Query("SELECT DISTINCT i FROM Inscription i WHERE i.idClasse.idFiliere.idNiveau.nom =:nomNiveau and year (i.idClasse.idAnneeScolaire.finAnnee) =:year and i.idClasse.idFiliere.idFiliere.nomFiliere =:nomFiliere")
    List<Inscription> getListInscritByNiveauNameAndIdAnnee(@Param("nomNiveau") String nomNiveau, @Param("year") int year, @Param("nomFiliere") String nomFiliere);

    List<Inscription> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long anneeScolaireId, long classeId);

    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE) AND i.idClasse.id = :idClasse AND i.active = :isActive")
    List<Inscription> findByIdClasseIdAndActive(@Param("idClasse") long idClasse, @Param("isActive") boolean isActive);

    @Query("SELECT i FROM Inscription i WHERE i.idClasse.idAnneeScolaire.id =:idAnnee AND i.idClasse.id = :idClasse AND i.payer = :isPayer")
    List<Inscription> getByIdClasseIdAndPayer(@Param("idAnnee") long idAnnee ,@Param("idClasse") long idClasse, @Param("isPayer") boolean isPayer);

    @Query("SELECT i FROM Inscription i WHERE  i.idClasse.id = :idClasse")
    Page<Inscription> findByIdClasseId(@Param("idClasse") long idClasse, Pageable pageable);

//    @Query("""
//    SELECT DISTINCT i FROM Inscription i
//    JOIN i.idClasse c
//    JOIN ClasseModule cm ON cm.idNiveauFiliere.id = c.idFiliere.id
//    JOIN cm.idUE u
//    JOIN Modules m ON m.idUe.id = u.id
//    WHERE c.id = :idClasse
//      AND NOT EXISTS (
//          SELECT 1 FROM Modules m2
//          JOIN ClasseModule cm2 ON cm2.idUE.id = m2.idUe.id
//          WHERE cm2.idNiveauFiliere.id = c.idFiliere.id
//            AND NOT EXISTS (
//                SELECT 1 FROM Notes n
//                WHERE n.idModule.id = m2.id
//                  AND n.idInscription.id = i.id and n.idSemestre.id =:semestreId
//            )
//      )
//""")
//    @Query(value = "SELECT  DISTINCT i.* FROM inscription as i, notes as n, semestres as s, modules as m WHERE i.id_classe_id =:idClasse AND n.id_semestre_id =:semestreId", nativeQuery = true)
//    Page<Inscription> findStudentsWithNotesOnAllModules(@Param("idClasse") long idClasse, @Param("semestreId") long semestreId, Pageable pageable);



    @Query("SELECT i FROM Inscription i WHERE  i.idClasse.id = :idClasse")
    List<Inscription> findByIdClasse(@Param("idClasse") long idClasse);


//    @Query("SELECT SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date ) and i.idEtudiant.status =:professionnel")
//    double sumScolariteForCurrentYearPro(@Param("professionnel") Type_status professionnel);
//
//    @Query("SELECT SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date ) and i.idEtudiant.status =:reg")
//    double sumScolariteForCurrentYearReg(@Param("reg") Type_status reg);
//
//    @Query("SELECT SUM(i.idClasse.idFiliere.scolarite) -SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date ) and i.idEtudiant.status =:professionnel")
//    double getReliquatForCurrentYear(@Param("professionnel") Type_status professionnel);
//
//    @Query("SELECT COUNT(i) * 6000.00 -SUM(i.scolarite) FROM Inscription i WHERE YEAR (i.date) = YEAR (current_date ) and i.idEtudiant.status =:reg")
//    double getReliquatForCurrentYearREG(@Param("reg") Type_status reg);

    @Query("SELECT COUNT(c) from Inscription c WHERE YEAR(c.date) = YEAR(current_date) and c.payer =:payer")
    int countAllByPayer(@Param("payer") boolean payer);

    @Query("select count(i) from Inscription i where i.idClasse.idFiliere.idFiliere.id =:idFiliere and i.totalPayer =:isPay and i.idClasse.idAnneeScolaire.id =:idAnnee")
    Integer cuntByFiliere(@Param("idFiliere") long idFiliere, @Param("isPay") boolean isPay, @Param("idAnnee") long idAnnee);

    @Query("select count(i) from  Inscription i where  i.idEtudiant.status =:status and i.payer =:isPay and i.totalPayer =:isTotal and i.idClasse.idAnneeScolaire.id =:idAnnee")
    Integer cuntByStatus(@Param("status") Type_status status, @Param("isPay") boolean isPay, @Param("isTotal") boolean isTotal,  @Param("idAnnee") long idAnnee);

    @Query("select i from  Inscription i where  i.idEtudiant.status =:status and i.payer =:isPay and i.totalPayer =:isTotal and i.idClasse.idAnneeScolaire.id =:idAnnee")
    List<Inscription> getInscriptionByStatusAndPaye(@Param("status") Type_status status, @Param("isPay") boolean isPay, @Param("isTotal") boolean isTotal,  @Param("idAnnee") long idAnnee);


    @Query("select count(i) from  Inscription i where  i.idClasse.idAnneeScolaire.id =:idAnnee and i.payer =:isPay and i.totalPayer =:isTotal")
    Double cuntByIdAnnee(@Param("idAnnee") long idAnnee, @Param("isPay") boolean isPay, @Param("isTotal") boolean isTotal);

    Double countByIdClasseIdAnneeScolaireId(long idAnnee);

    @Query("select i from Inscription i where i.idClasse.idFiliere.idFiliere.id =:idFiliere and i.idClasse.idAnneeScolaire.id =:idAnnee and i.payer =:isPaye")
    List<Inscription> getInscriptionByFilieresAndPaye(@Param("idFiliere") long idFiliere, @Param("idAnnee") long idAnnee, @Param("isPaye") boolean isPaye);

    @Query("SELECT i FROM Inscription i WHERE YEAR(i.date) = YEAR(CURRENT_DATE)")
    Page<Inscription> findStudentOfCurrentYear( Pageable pageable);


    @Query("SELECT i FROM Inscription i WHERE :currentDate BETWEEN i.idClasse.idAnneeScolaire.debutAnnee and i.idClasse.idAnneeScolaire.finAnnee and i.payer =:etat")
    Page<Inscription> findStudentByEtat(@Param("currentDate") LocalDate currentDate, @Param("etat") boolean etat, Pageable pageable);

    Page<Inscription> findByIdClasseIdAnneeScolaireIdAndIdClasseId(long idAnneeScolaireId, long idClasseId, Pageable pageable);

}
