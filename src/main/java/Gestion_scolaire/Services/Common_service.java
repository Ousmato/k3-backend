package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Salles;
import Gestion_scolaire.Models.SeanceConfig;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Repositories.Salles_repositorie;
import Gestion_scolaire.Repositories.SeancType_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
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
    private Seance_repositorie seance_repositorie;

    @Autowired
    SeancType_repositorie seancType_repositorie;

    public List<Salles> salle_occuper(){
        List<Salles> salles = sallesRepositorie.findAll();
        List<Salles> salles_occuper = new ArrayList<>();

        for (Salles salle : salles) {
            List<Seances> seancesActif = getAllSeancesActive(salle.getId());
            boolean hasSeanceActif = false;
            for (Seances seance : seancesActif) {

                if(seance.getIdSalle().getId() == salle.getId()){
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

    //    -------------------------all seance active by id salle
    public List<Seances> getAllSeancesActive(long idSalle){
        List<Seances> seancesList = seance_repositorie.getAllByIdSalle_Id(idSalle, LocalDate.now());
        if (seancesList.isEmpty()) {
            return new ArrayList<>();
        }
        return seancesList;
    }

//    liste des salles occuper a la meme date
    public List<Salles> salle_occuper_toDay( LocalDate date){
        List<Salles> salles = sallesRepositorie.findAll();
        List<Salles> salles_occuper = new ArrayList<>();

        for (Salles salle : salles) {
            List<Seances> seancesActif = getAllSeancesActive(salle.getId());
            boolean hasSeanceActif = false;
            for (Seances seance : seancesActif) {

                if(seance.getDate().equals(date)){
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


    //    ------------------------------methode pour eviter le chevauchement d'heure
    public boolean isOverlapping(Seances newSeance, Seances existingSeance) {
        LocalDateTime newStart = newSeance.getDate().atTime(newSeance.getHeureDebut());
        LocalDateTime newEnd = newSeance.getDate().atTime(newSeance.getHeureFin());
        LocalDateTime existingStart = existingSeance.getDate().atTime(existingSeance.getHeureDebut());
        LocalDateTime existingEnd = existingSeance.getDate().atTime(existingSeance.getHeureFin());

        // Vérifier si les intervalles se chevauchent
        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
    }

    public Object createConfig(List<SeanceConfig> seanceConfig){
//        System.out.println("------------------------"+seanceConfig);
        boolean hasConfig = false;
        for (SeanceConfig sc : seanceConfig) {
            SeanceConfig confiExist = seancType_repositorie.getByHeureDebutAndHeureFinAndIdSeanceId(sc.getHeureDebut(), sc.getHeureFin(),
                    sc.getIdSeance().getId());
            if (confiExist == null) {
                seancType_repositorie.save(sc);
                hasConfig = true;

            }

        }

        if (hasConfig) {
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        } else {
            throw new NoteFundException("Une configuration existante est trouver");
        }

    }

}
