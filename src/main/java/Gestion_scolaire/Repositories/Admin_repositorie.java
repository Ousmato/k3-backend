package Gestion_scolaire.Repositories;

import Gestion_scolaire.EnumClasse.Admin_role;
import Gestion_scolaire.Models.Admin;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Admin_repositorie extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPassword(String email, String password);
    Admin findByEmail(String email);

    Admin findByRole(Admin_role role);
}
