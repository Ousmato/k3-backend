package Gestion_scolaire.Services;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Common_service {

    @Autowired
    Salles_repositorie sallesRepositorie;


    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private  Journee_repositorie journee_repositorie;

    @Autowired
    private Paie_repositorie paie_repositorie;

    public List<Salles> salle_occuper(LocalDate date, LocalTime time){
        List<Salles> salles = sallesRepositorie.findAll();
        List<Salles> salles_occuper = new ArrayList<>();

        for (Salles salle : salles) {
            List<Journee> seancesActif = journee_repositorie.getAllByIdSalle_Id(salle.getId(), date, time);
            boolean hasSeanceActif = false;
            for (Journee jour : seancesActif) {

                if(jour.getIdSalle().getId() == salle.getId()){
                    hasSeanceActif = true;
                    break;

                }
            }
            if (hasSeanceActif) {
                salles_occuper.add(salle);
            }

        }
        return salles_occuper;
    }

    //    -------------------------all seance active by id salle in to day and current time
    public List<Journee> getAllSeancesActive(long idSalle){
        List<Journee> seancesList = journee_repositorie.getAllByIdSalle_Id(idSalle, LocalDate.now(), LocalTime.now());
        if (seancesList.isEmpty()) {
            return new ArrayList<>();
        }
        return seancesList;
    }

//    --------------------

//    liste des salles occuper a la meme date
    public List<Salles> salle_occuper_toDay( LocalDate date){
        List<Salles> salles = sallesRepositorie.findAll();
        List<Salles> salles_occuper = new ArrayList<>();

        for (Salles salle : salles) {
            List<Journee> seancesActif = getAllSeancesActive(salle.getId());
            boolean hasSeanceActif = false;
            for (Journee jour : seancesActif) {

                if(jour.getDate().equals(date)){
                    hasSeanceActif = true;
                    break;

                }
            }
            if (hasSeanceActif) {
                salles_occuper.add(salle);
            }

        }
        return salles_occuper;
    }



//    --------------------------hours traitement
    public List<String> calculerPlagesHoraires(LocalTime heureDebut, LocalTime heureFin) {
        List<String> plagesHoraires = new ArrayList<>();

        while (heureDebut.isBefore(heureFin)) {
            LocalTime prochainHeureDebut = heureDebut.plusHours(2);
            if (prochainHeureDebut.isAfter(heureFin)) {
                prochainHeureDebut = heureFin;
            }
            plagesHoraires.add(heureDebut + " - " + prochainHeureDebut);
            heureDebut = prochainHeureDebut;
        }

        return plagesHoraires;
    }

//    //    ------------------------------methode pour eviter le chevauchement d'heure
//    public boolean isOverlapping(Seances newSeance, Seances existingSeance) {
//        LocalDateTime newStart = newSeance.getDate().atTime(newSeance.getHeureDebut());
//        LocalDateTime newEnd = newSeance.getDate().atTime(newSeance.getHeureFin());
//        LocalDateTime existingStart = existingSeance.getDate().atTime(existingSeance.getHeureDebut());
//        LocalDateTime existingEnd = existingSeance.getDate().atTime(existingSeance.getHeureFin());
//
//        // Vérifier si les intervalles se chevauchent
//        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
//    }


    //    ------------------------------------------------------------------------------
    public void validateSeance(Journee seances) {

        Duration duration = Duration.between(seances.getHeureDebut(), seances.getHeureFin());
        if(duration.toHours() > 10){
            throw new NoteFundException("Invalide la durée maximum est 10 hours");
        }
        Journee jourExist = journee_repositorie.findByDateAndIdEmploisIdAndIdTeacherIdEnseignantAndHeureFin(
                seances.getDate(), seances.getIdEmplois().getId(), seances.getIdTeacher().getIdEnseignant(), seances.getHeureFin());
        if(jourExist != null){
            throw new NoteFundException("Une seance existe déjà pour cette date : "  +  jourExist.getDate());
        }

        if (seances.getHeureFin().equals(seances.getHeureDebut())) {
            throw new NoteFundException("Invalid, vérifier les heures");
        }
        if (seances.getHeureFin().isBefore(seances.getHeureDebut())) {
            throw new NoteFundException("Invalid, l'heure de fin est inférieure à l'heure de début");
        }


        Emplois emploisExist = emplois_repositorie.findById(seances.getIdEmplois().getId());
        if(emploisExist == null){
            throw new NoteFundException("L'emploi du temps n'existe pas");
        }
        LocalDate dateSeance = seances.getDate();

        if (dateSeance.isBefore(emploisExist.getDateDebut()) || dateSeance.isAfter(emploisExist.getDateFin())) {
            throw new RuntimeException("La date de la séance doit être comprise entre la date de début et de fin de l'emploi du temps");
        }
    }
//-----------------------------------------create paie for config
    public void createPaieForConfig(Journee jour) {
        Duration duration = Duration.between(jour.getHeureDebut(), jour.getHeureFin());
        long heures = duration.toHours();

        if(heures < 1){
            throw new NoteFundException("la durée minimum  de payement est égale a 1 heure");
        }
        if(heures > 8){
           heures =  duration.toHours() - 2;

        }


        Paie paie = new Paie();
        paie.setJournee(jour);
        paie.setDate(jour.getDate());

        if (jour.getSeanceType().equals(Seance_type.examen) || jour.getSeanceType().equals(Seance_type.session)) {
            paie.setCoutHeure(5000);
        }else {
            paie.setCoutHeure(10000);
        }

        paie.setNbreHeures((int) heures);
        paie_repositorie.save(paie);
    }


}
