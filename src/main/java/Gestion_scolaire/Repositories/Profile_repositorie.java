package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Profile_repositorie extends JpaRepository<Profile, Long> {

    Profile findByIdFiliereIdAndIdTeacherIdEnseignant(Long idFiliereId, Long idTeacherId);

    List<Profile> getByIdTeacherIdEnseignant(Long idTeacherId);

    List<Profile> findByIdFiliereId(Long idFiliereId);
}
