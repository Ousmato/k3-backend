package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Participant_repositorie extends JpaRepository<Participant, Long> {

    Participant findByIdInscriptionIdAndIdStudentGroupId(long IdInscription, long idStudentGroupId);

    List<Participant> findByIdStudentGroupId(long idStudentGroupId);

    List<Participant> getByIdStudentGroupIdEmploiIdClasseId(long idClasse);

    Participant findByIdInscriptionIdAndIdStudentGroupIdEmploiId(long IdInscription, long idEmploi);

    List<Participant> getAllByIdStudentGroupIdEmploiId(long idEmploi);

}
