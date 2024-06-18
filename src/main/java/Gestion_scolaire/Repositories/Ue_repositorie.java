package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ue_repositorie extends JpaRepository<UE, Long> {
    UE findByNomUE(String nomUe);
    UE findById(long idUe);
}
