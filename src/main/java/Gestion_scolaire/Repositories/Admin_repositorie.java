package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Admin_repositorie extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPassword(String email, String password);
    Admin findByEmail(String email);
}
