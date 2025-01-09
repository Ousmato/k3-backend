package Gestion_scolaire.Repositories;

import Gestion_scolaire.Classes.entity.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Ue_repositorie extends JpaRepository<UE, Long> {
    UE findByNomUE(String nomUe);
    UE findById(long idUe);

    @Query("SELECT  distinct  ue from UE ue where ue.idClasse.id =:idClasse and ue.idSemestre.id =:idSemestre")
    List<UE> findByIdClasse(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre);
}
