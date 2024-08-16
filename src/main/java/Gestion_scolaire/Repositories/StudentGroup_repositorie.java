package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.StudentGroupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGroup_repositorie extends JpaRepository<StudentGroupe, Long> {

    StudentGroupe findByIdEmploiIdAndNom(long emploiId, String nom);

    List<StudentGroupe> getByIdEmploiId(long idEmplois);

//    List<StudentGroupe> getBy
}
