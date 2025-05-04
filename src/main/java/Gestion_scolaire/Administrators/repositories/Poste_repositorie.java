package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Poste_repositorie extends JpaRepository<Postes, Long> {

    Postes findByNomAndTypeFiliere(String name, TypeFiliere typeFiliere);

    Postes findById(long id);

    Postes findByNom(String nom);

}
