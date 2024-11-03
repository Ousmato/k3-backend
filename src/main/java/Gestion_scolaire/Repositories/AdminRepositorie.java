package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.Admin_role;
import Gestion_scolaire.Models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepositorie extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPassword(String email, String password);
    Admin findByEmail(String email);
    Optional<Admin> getAdminByEmailAndActive(String email, boolean isActive);

    Admin findByRoleAndActive(Admin_role role, boolean isActive);
    List<Admin> findAllByActive(boolean isActif);

    Admin findByIdAdministra(long id);

}
