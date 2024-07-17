package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.InfoSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoSchool_repositorie extends JpaRepository<InfoSchool, Long> {
    InfoSchool findByEmailAndTelephone(String email, int telephone);

    InfoSchool findById(long idSchool);

}
