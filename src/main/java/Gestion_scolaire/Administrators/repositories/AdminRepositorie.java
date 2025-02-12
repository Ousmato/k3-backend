package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepositorie extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);

    Optional<Admin> getAdminByEmailAndActive(String email, boolean isActive);

    Admin findByIdRoleIdAndActiveAndEmailAndIdRoleTypeFiliere(long idRole, boolean isActive, String email, TypeFiliere typeFiliere);

    List<Admin> findAllByActive(boolean isActif);

    Admin findByIdAdministra(long id);

    Admin findByEmailAndActive(String email, boolean isActive);

    Admin getByIdAdministraAndActive(long idAdministra, boolean isActive);

    Admin findByIdRoleIdAndActive(long idRole, boolean isActive);

}
