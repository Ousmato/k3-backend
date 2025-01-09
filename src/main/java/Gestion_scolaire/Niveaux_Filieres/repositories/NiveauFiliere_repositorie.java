package Gestion_scolaire.Niveaux_Filieres.repositories;

import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NiveauFiliere_repositorie extends JpaRepository<NiveauFilieres, Long> {
    NiveauFilieres findByIdFiliereAndIdNiveau(Filiere idFiliere, Niveau idNiveau);
    NiveauFilieres findByIdFiliereIdAndIdNiveauId(long idFiliere, long idNiveau);

    List<NiveauFilieres> findByIdFiliereId(long idNiveau);

    NiveauFilieres findById(long idNivFiliere);

    List<NiveauFilieres> getAllByIdFiliereIdAndIdNiveauId(long idFiliere, long idNiveau);
//    List<NiveauFilieres> getAllBy
}
