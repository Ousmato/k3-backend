package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Jury_repositorie extends JpaRepository<Jury, Long> {

    Jury findById(long id);

    List<Jury> findByIdSoutenanceId(long idSoutenance);
}
