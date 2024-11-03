package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Teachers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Teacher_repositorie extends JpaRepository<Teachers, Long> {
    Teachers findByEmailAndPassword(String email, String password);
    Teachers findByEmail(String email);
    Teachers findByIdEnseignantAndActive(long id, boolean isActive);

    Teachers findByIdEnseignant(long id);
    List<Teachers> findByActive(boolean isActive);

//    List<Teachers> findByIdUeId(long idUe);

    int countByActive(boolean isActive);

    Teachers getAllByTelephone(int telephone);

    List<Teachers> findByNomContaining(String nom);

}
