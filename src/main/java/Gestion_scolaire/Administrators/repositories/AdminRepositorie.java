package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepositorie extends JpaRepository<AdministrationUsers, Long> {

    AdministrationUsers findByEmail(String email);

    Optional<AdministrationUsers> getAdminByEmailAndActive(String email, boolean isActive);

    List<AdministrationUsers> getAdminByActive(boolean active);

    AdministrationUsers findById(long id);

    AdministrationUsers findByEmailAndActive(String email, boolean isActive);

    AdministrationUsers getByIdAndActive(long idAdministra, boolean isActive);

    boolean existsByNom(String username);

    AdministrationUsers findByIdPosteIdAndActiveAndEmailAndIdPosteTypeFiliere(long idPosteId, boolean isActive, String email, TypeFiliere typeFiliere);
}
