package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Documents;
import Gestion_scolaire.Models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Notification_repositorie extends JpaRepository<Notifications, Long> {
    List<Notifications> findByIdEmploisId(long id);
    Notifications findById(long id);
    Notifications findByIdTeachersIdEnseignantAndDateAndIdEmploisId(long idTeacher, LocalDate date, long idEmplois);
    Notifications findByIdDoc(Documents idDoc);

    Notifications findByIdAdminIdAdministraAndDateAndIdEmploisIdClasseId(long idAdmin, LocalDate date, long idClasse);

    Notifications findByIdAdminIdAdministraAndDateAndTitre(long idAmin, LocalDate date, String titre);
//    List<Notifications> getByDateBetween
}
