package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.SeanceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeancType_repositorie extends JpaRepository<SeanceConfig, Long> {

    SeanceConfig findByIdSeanceId(long idSeanceId);

    List<SeanceConfig> getByIdSeanceIdEmploisId(long idEmploi);

    SeanceConfig findById(long idConfig);

    @Query("SELECT c FROM SeanceConfig c WHERE (c.seanceType = :exam OR c.seanceType = :session) AND c.idSeance.idEmplois.dateFin > :currentDate")
    List<SeanceConfig> findBySeanceTypeAndDate(@Param("exam") Seance_type exam, @Param("session") Seance_type session, @Param("currentDate") LocalDate currentDate);

    SeanceConfig getByHeureDebutAndHeureFinAndIdSeanceId(LocalTime heureDebut, LocalTime heureFin, long idSeanceId);

}
