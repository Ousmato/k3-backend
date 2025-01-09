package Gestion_scolaire.Niveaux_Filieres.repositories;

import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Niveau_repositorie extends JpaRepository<Niveau, Long> {
    Niveau findByNom(String nom);
    Niveau findById(long idNiveau);

}
