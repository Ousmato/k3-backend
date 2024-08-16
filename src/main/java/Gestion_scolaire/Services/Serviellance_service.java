package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.ListPresenceTeacher_repositorie;
import Gestion_scolaire.Repositories.SeancType_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Serviellance_service {

    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
   private Common_service common_service;

    @Autowired
    private ListPresenceTeacher_repositorie listPresenceTeacher_repositorie;

    @Autowired
    private SeancType_repositorie seancType_repositorie;

    public Object addSurveillance(List<Seances> seancesList, List<SeanceConfig> config){
        for (Seances seances : seancesList) {
            if(seances.getHeureFin().equals(seances.getHeureDebut())){
                throw new NoteFundException("Invalid, verifier les heures");
            }
            if(seances.getHeureFin().isBefore(seances.getHeureDebut())){
                throw new NoteFundException("Invalid, l'heure de fin est inférieur a heure de début");
            }

            Emplois emploisExist = emplois_repositorie.findById(seances.getIdEmplois().getId());
            LocalDate dateSeance = seances.getDate();

            if (dateSeance.isBefore(emploisExist.getDateDebut()) || dateSeance.isAfter(emploisExist.getDateFin())) {
                throw new RuntimeException("La date de la séance doit être comprise entre la date de début et de fin de l'emploi du temps");
            }

            // Vérifie l'existence d'une séance similaire
            Seances seances1 = seance_repositorie.getByDateAndIdModuleIdAndIdEmploisId(
                    seances.getDate(), seances.getIdModule().getId(), seances.getIdEmplois().getId());
            if(seances1 != null){
                throw new RuntimeException("Ce module existe deja pour cette date" );

            }

            List<Seances> existingSeances = seance_repositorie.getAllByDateAndIdTeacherIdEnseignant(seances.getDate(),seances.getIdTeacher().getIdEnseignant());

            for (Seances existingSeance : existingSeances) {
                if (common_service.isOverlapping(seances, existingSeance)) {
                    throw new RuntimeException("La séance chevauche une séance existante pour cet enseignant");
                }
            }
            List<Salles> sallesList = common_service.salle_occuper_toDay(seances.getDate());
            if(!sallesList.isEmpty()){
                for (Salles salle : sallesList) {
                    if (salle.getId() == seances.getIdSalle().getId()) {
                        throw new NoteFundException("Cette salle est déjà occupée");
                    }
                }
            }

            List<Seances> listExist = seance_repositorie.getAllByDate(seances.getDate());
            if (!listExist.isEmpty()) {
                for (Seances sc : listExist) {
                    boolean hasHeureIntervale = true;
                    if (seances.getHeureFin().isAfter(sc.getHeureDebut()) && seances.getHeureFin().isBefore(sc.getHeureFin())) {
                        hasHeureIntervale = false;
                        throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                    }
                }
            }

          Seances saved =  seance_repositorie.save(seances);


            boolean hasConfig = false;
            for (SeanceConfig sc : config) {
                SeanceConfig seanceConfig = new SeanceConfig();
                sc.setIdSeance(saved);
                SeanceConfig confiExist = seancType_repositorie.getByHeureDebutAndHeureFinAndIdSeanceId(sc.getHeureDebut(), sc.getHeureFin(),
                        saved.getId());
                if (confiExist == null) {;
                    seancType_repositorie.save(sc);
                    hasConfig = true;

                }

            }

//            save teacher
            TeachersPresence tp = new TeachersPresence();
            tp.setDate(saved.getDate());
            tp.setIdSeance(saved);
            tp.setObservation(false);
            listPresenceTeacher_repositorie.save(tp);

            if (hasConfig) {
                return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
            } else {
                throw new NoteFundException("Une configuration existante est trouver");
            }

        }
        return DTO_response_string.fromMessage("Ajoute effectué avec succès",200);
    }

}
