package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepositorie extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByAdminIdAdministra(long id);

    RefreshToken findByToken(String token);

    RefreshToken findByAdminEmail(String email);
}
