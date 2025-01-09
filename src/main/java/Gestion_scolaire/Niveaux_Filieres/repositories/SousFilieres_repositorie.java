package Gestion_scolaire.Niveaux_Filieres.repositories;

import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface SousFilieres_repositorie extends JpaRepository<SousFilieres, Long> {

    List<SousFilieres> findByIdClasseId(long idClasseId);

}
