package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Salles;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Repositories.Salles_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Common_service {

    @Autowired
    Salles_repositorie sallesRepositorie;

    @Autowired
    private Seance_repositorie seance_repositorie;

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
}
