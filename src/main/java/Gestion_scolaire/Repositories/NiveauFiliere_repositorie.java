package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NiveauFiliere_repositorie extends JpaRepository<NiveauFilieres, Long> {
    NiveauFilieres findByIdFiliereAndIdNiveau(Filiere idFiliere, Niveau idNiveau);
    NiveauFilieres findByIdFiliereIdAndIdNiveauId(long idFiliere, long idNiveau);

    List<NiveauFilieres> findByIdFiliereId(long idNiveau);

    List<NiveauFilieres> getAllByIdFiliereIdAndIdNiveauId(long idFiliere, long idNiveau);
}
