package Gestion_scolaire.Niveaux_Filieres.repositories;

import Gestion_scolaire.Niveaux_Filieres.entity.Filiere_specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereSpecialiste_repositorie extends JpaRepository<Filiere_specialite, Long> {

    Filiere_specialite getByIdFiliereIdAndIdSpecialiteId(long idFiliereId, long idSpecialiteId);


}
