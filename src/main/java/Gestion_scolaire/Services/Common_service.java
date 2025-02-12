package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Inscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Common_service {

    @Autowired
    private Shared_services shared_services;

    @Autowired
    private Shared_repositories shared_repositories;


    public List<Salles> salle_occuper(LocalDate date, LocalTime time){
        List<Salles> salles = shared_repositories.getSalles_repositorie().findAll();
        List<Salles> salles_occuper = new ArrayList<>();

        for (Salles salle : salles) {
            List<Journee> seancesActif =  shared_repositories.getJournee_repositorie().getAllByIdSalle_Id(salle.getId(), date, time);
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
        List<Journee> seancesList =  shared_repositories.getJournee_repositorie().getAllByIdSalle_Id(idSalle, LocalDate.now(), LocalTime.now());
        if (seancesList.isEmpty()) {
            return new ArrayList<>();
        }
        return seancesList;
    }

//    --------------------

//    liste des salles occuper a la meme date
    public List<Salles> salle_occuper_toDay( LocalDate date){
        List<Salles> salles = shared_repositories.getSalles_repositorie().findAll();
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


    //    ------------------------------------------------------------------------------
    public void validateSeance(Journee seances) {

        Duration duration = Duration.between(seances.getHeureDebut(), seances.getHeureFin());
        if(duration.toHours() > 10){
            throw new NoteFundException("Invalide la durée maximum est 10 hours");
        }
        Journee jourExist = shared_repositories.getJournee_repositorie().findByDateAndIdEmploisIdAndIdTeacherIdEnseignantAndHeureFin(
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


        Emplois emploisExist = shared_repositories.getEmplois_repositorie().findById(seances.getIdEmplois().getId());
        if(emploisExist == null){
            throw new NoteFundException("L'emploi du temps n'existe pas");
        }
        LocalDate dateSeance = seances.getDate();

        if (dateSeance.isBefore(emploisExist.getDateDebut()) || dateSeance.isAfter(emploisExist.getDateFin())) {
            throw new RuntimeException("La date de la séance doit être comprise entre la date de début et de fin de l'emploi du temps");
        }
    }
//-----------------------------------------create paie for config


    //get ids of students have note for all modules
    public List<Long> getIdsOfStudents(long idSemestre, long idClasse) {
        // Récupérer tous les modules de la classe pour le semestre
        List<Modules> modulesList = shared_services.getModules_service().allModulesOfClassByIdSemestre(idSemestre, idClasse);

        // Récupérer toutes les notes pour le semestre et la classe
        List<Notes> notes = shared_repositories.getNotes_repositorie().getByIdSemestreIdAndIdClasseId(idSemestre, idClasse);

//        System.out.println("----------modules size--------------" + modulesList.size());

        // Grouper les notes par ID d'inscription
        Map<Long, List<Notes>> inscriptionsGroupedByStudent = notes.stream()
                .collect(Collectors.groupingBy(note -> note.getIdInscription().getId()));

        // Filtrer pour garder uniquement les étudiants ayant des notes pour tous les modules actifs
        return inscriptionsGroupedByStudent.entrySet().stream()
                .filter(entry -> entry.getValue().size() == modulesList.size())
                .map(Map.Entry::getKey)
                .toList();
    }

    //Récupérer tous les ids des etudiants qui ont une moyenne generale
    public List<Inscription> getStudentsHaveMoyenByClassId(long idClasse) {
        List<Moyenne> moyennes = shared_repositories.getMoyenne_repositorie().getAllMoyennesByClasseId(idClasse);
        List<Inscription> inscriptionList = new ArrayList<>();
        if (moyennes.isEmpty()) {
            return  new ArrayList<>();
        }
        for (Moyenne moyenne : moyennes) {
            inscriptionList.add(moyenne.getIdInscription());
        }
        return inscriptionList;

    }






}
