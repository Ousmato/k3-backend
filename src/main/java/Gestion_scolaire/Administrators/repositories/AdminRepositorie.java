package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepositorie extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);

    Optional<Admin> getAdminByEmailAndActive(String email, boolean isActive);

    Admin findByIdRoleIdAndActiveAndEmail(long idRole, boolean isActive, String email);

    List<Admin> findAllByActive(boolean isActif);

    Admin findByIdAdministra(long id);

    Admin findByEmailAndActive(String email, boolean isActive);

    Admin getByIdAdministraAndActive(long idAdministra, boolean isActive);

    Admin findByIdRoleIdAndActive(long idRole, boolean isActive);

}
