package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Models.UsersGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGrade_repositorie extends JpaRepository<UsersGrade, Long> {

    UsersGrade findByLibelle(String libelle);
}
