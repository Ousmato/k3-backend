package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Notifications;
import Gestion_scolaire.Repositories.Notification_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Notification_service {

    @Autowired
    private Notification_repositorie notification_repositorie;

    public List<Notifications> getListNotification(){
        return notification_repositorie.findAll();
    }
//    -------------------------------------------------methode pour supprimer une notification--------------
    public String deleteNotification(long id){
        Notifications notifiExist = notification_repositorie.findById(id);
        if (notifiExist != null){
            notification_repositorie.delete(notifiExist);
            return "success";
        }
        throw new RuntimeException("cette notification n'exist pas");
    }
//    ----------------------------method add notification for Teachers-----------------------------------
    public Notifications addTeacher(Notifications notifications){
        notifications.setDate(LocalDate.now());
        Notifications notifiExist = notification_repositorie.findByIdTeachersIdEnseignantAndDateAndIdEmploisId(
                notifications.getIdTeachers().getIdEnseignant(),notifications.getDate(), notifications.getIdEmplois().getId());
        if(notifiExist != null){
            throw new RuntimeException("desoler vous avez deja une");
        }
        return notification_repositorie.save(notifications);
    }
//    -------------------------------------------------method add notification for admin-------------------------
    public Notifications addAmin(Notifications notifications){
        notifications.setDate(LocalDate.now());
        Notifications notifiExist = notification_repositorie.findByIdAdminIdAdministraAndDateAndIdEmploisIdClasseModuleIdStudentClasseId(
                notifications.getIdAdmin().getIdAdministra(),notifications.getDate(),notifications.getIdEmplois().getIdClasseModule().getIdStudentClasse().getId());
        if (notifiExist != null){
            throw new RuntimeException("notification avec cette date pour cette classe exist deja");
        }
        return notification_repositorie.save(notifications);
    }
//    --------------------------method to update notification-------------------------------
    public Notifications update(Notifications notifications){
        Notifications notifiExist = notification_repositorie.findById(notifications.getId());

        if (notifiExist.getIdTeachers().equals(notifications.getIdTeachers())){
            notifiExist.setIdEmplois(notifications.getIdEmplois());
            notifiExist.setTitre(notifications.getTitre());
            notifiExist.setDate(LocalDate.now());
            notifiExist.setIdDoc(notifications.getIdDoc());
            notifiExist.setDescription(notifications.getDescription());
            return notification_repositorie.save(notifiExist);
        }else if (notifiExist.getIdAdmin().equals(notifications.getIdAdmin())){
                notifiExist.setDate(LocalDate.now());
                notifiExist.setDescription(notifications.getDescription());
                notifiExist.setTitre(notifications.getTitre());
            return notification_repositorie.save(notifiExist);
            }
        throw new RuntimeException("cette notification n'exist pas");
        }

}
