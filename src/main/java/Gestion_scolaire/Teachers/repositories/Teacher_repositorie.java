package Gestion_scolaire.Teachers.repositories;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Teachers.entity.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Teacher_repositorie extends JpaRepository<Teachers, Long> {
    Teachers findByEmail(String email);
    Teachers findByIdAndActive(long id, boolean isActive);

    Teachers findById(long id);

    int countByActive(boolean isActive);

    Teachers getAllByTelephone(String telephone);

    Teachers getTeachersByEmailAndTelephone(String email, String telephone);

    List<Teachers> findByNomContaining(String nom);

    @Query("SELECT j.intervenant FROM Journee j WHERE j.idEmplois.id = :idEmploi AND j.seanceType = :cm AND j.intervenant IS NOT NULL ORDER BY j.id ASC")
    List<Teachers> getPrincipalProf(@Param("idEmploi") long idEmploi, @Param("cm") Seance_type cm);

    @Query("SELECT DISTINCT j.intervenant FROM Journee j WHERE j.idEmplois.idClasse.idAnneeScolaire.id =:idAnnee and j.idEmplois.idSemestre.id =:idSemestre")
    List<Teachers> geAllTeacherHaveJournees(@Param("idAnnee") long idAnnee, @Param("idSemestre") long idSemestre);


}
