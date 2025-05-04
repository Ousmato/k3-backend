package Gestion_scolaire.Teachers.repositories;

import Gestion_scolaire.Teachers.entity.Surveillants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Surveillants_repositorie extends JpaRepository<Surveillants, Long> {

    List<Surveillants> findAllByActive(boolean active);
    Surveillants findById(long id);
    Surveillants findByNomAndPrenomAndTelephone(String nom, String prenom, String telephone);
}
