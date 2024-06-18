package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Notifications;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Notification_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Seance_service {
    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private Notification_repositorie notification_repositorie;

    List<Seances> seancesListSave = new ArrayList<>();
    @Transactional
    public List<Seances> addMultiple(List<Seances> seancesList) {
        List<Seances> savedSeances = new ArrayList<>();

        for (Seances seances : seancesList) {
            // Vérifie l'existence de l'emploi associé
            Emplois emploisExist = emplois_repositorie.findById(seances.getIdEmplois().getId());
           if ( !seances.getDate().isAfter(emploisExist.getDateDebut())  && !seances.getDate().isBefore(emploisExist.getDateFin())){
               throw new RuntimeException("la date dois etre comprise entre date de l'emplois du temps");
           }
            // Vérifie l'existence d'une séance similaire
            Seances seanceExist = seance_repositorie.findByHeureDebutAndHeureFinAndJour(
                    seances.getHeureDebut(), seances.getHeureFin(), seances.getJour()
            );

            // Vérifie si la séance existe déjà
            if (seanceExist != null) {
                throw new RuntimeException("Attention, la séance existe déjà : " + seances);
            }

            LocalTime heureDebut = seances.getHeureDebut();
            LocalTime heureFin = seances.getHeureFin();

            // Vérifie les heures et l'existence de l'emploi
            if (heureDebut.isBefore(heureFin)) {
                savedSeances.add(seance_repositorie.save(seances));
            } else {
                throw new RuntimeException("Veuillez vérifier les heures ou l'existence de l'emploi pour la séance : " + seances);
            }
        }
        Notifications notifications = new Notifications();
        notifications.setDate(LocalDate.now());
        notifications.setIdEmplois(savedSeances.getLast().getIdEmplois());
        notifications.setDescription("Un Emplois a ete ajouter pour cette classe "+savedSeances.getLast().getIdEmplois());
        notification_repositorie.save(notifications);
        seancesListSave = savedSeances;
        return savedSeances;
    }


//    ----------------------------------------------------appels tout les seances par id---------------
    public List<Seances> readByIdEmplois(long idEmplois){

        Emplois emploisExist = emplois_repositorie.findById(idEmplois);
        List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploisExist.getId());
        if (!seancesList.isEmpty()) {
            if (emploisExist.getDateFin().isBefore(LocalDate.now())) {
                return seancesList;
            }
            throw new RuntimeException("Auccun emplois pour aujourd'hui");
        }
        return seancesList;
    }


//    -----------------------------------------methode pour calculer le nbre d'heure d'un enseignant par seance-------
    public  int nbreHeure(Teachers idTeacher){
       return seance_repositorie.findTotalHoursByTeacher(idTeacher.getIdEnseignant());
    }

//    ----------------------------------nombre d'heure d'un enseignant present par seance d'un emplois-------------------
    public List<Integer> getNbreBySeance(long idTeacher){
        return seance_repositorie.findNbreHeureBySeanceIdTeacher(idTeacher,LocalDate.now().getMonthValue());
    }
//-----------------------------method get seance by Id teacher-----------------------------
    public List<Seances> readSeanceByIdTeacher(long idTeacher){
        List<Seances> listExist = seance_repositorie.findAll_ByIdTeacher(idTeacher,LocalDate.now());
        if (listExist.isEmpty()){
            return new ArrayList<>();
        }
        return listExist;
    }

}

