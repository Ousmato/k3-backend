package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Moyenne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Moyenne_repositorie extends JpaRepository<Moyenne, Long> {

    Moyenne findByIdSemestreIdAndIdInscriptionId(long idSemestreId, long idInscriptionId);

    @Query("SELECT m from Moyenne m WHERE m.idSemestre.id =:idSemestre and m.moyenGenerale > 10")
    List<Moyenne> findAllAjournees(@Param("idSemestre") long idSemestre);

    @Query("select distinct m  from Moyenne m where m.idInscription.idClasse.id =:idClasseId")
    List<Moyenne> getAllMoyennesByClasseId(@Param("idClasseId") long idClasseId);

    @Query("SELECT m from Moyenne m WHERE m.idSemestre.id =:idSemestre and m.idInscription.id =:idInscrit and m.moyenGenerale > 10")
    Moyenne findAllAjourneesByStudent(@Param("idSemestre") long idSemestre, @Param("idInscrit") long idInscrit);
}
