package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface Poste_repositorie extends JpaRepository<Poste, Long> {

    List<Poste> findByCurrentAdminIdAdministra(long idAdmin);
}
