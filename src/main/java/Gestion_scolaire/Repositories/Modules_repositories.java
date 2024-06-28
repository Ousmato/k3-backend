package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Modules_repositories extends JpaRepository<Modules, Long> {
    List<Modules> findByIdUeId(long id);
    Modules findModulesByIdUeId(long idUe);
    Modules findByIdUeAndNomModule(UE idUe, String nom);
    List<Modules> findAllById(long idClasse);
}
