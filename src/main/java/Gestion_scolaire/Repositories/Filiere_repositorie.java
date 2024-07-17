package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Filiere_repositorie extends JpaRepository<Filiere, Long> {
    Filiere findByNomFiliere(String nom);
    Filiere findById(long idFiliere);
//    Filiere findByIdAndIdNiveau(long id, Niveau idNiveau);
}
