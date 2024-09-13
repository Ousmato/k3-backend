package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Seance_service {
    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private ListPresenceTeacher_repositorie listPresenceTeacher_repositorie;

    @Autowired
    private Common_service common_service;




//    ----------------------------------------------------appels tout les seances par id---------------
public List<SeanceDTO> readByIdEmplois(long idEmplois) {
    // Récupérer l'emploi par ID
    Emplois emploisExist = emplois_repositorie.findById(idEmplois);

    // Vérifier si l'emploi existe
    if (emploisExist == null) {
        throw new NoteFundException("L'emploi n'existe pas");
    }

    // Récupérer les séances associées à l'emploi
    List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploisExist.getId());

    // Vérifier si la liste des séances n'est pas vide
    if (seancesList.isEmpty()) {
        // Retourner une liste vide si aucune séance n'est trouvée
        return new ArrayList<>();
    }

    // Vérifier si la date de fin de l'emploi est après la date actuelle
    if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
        // Trier les séances par date et heure de début
        seancesList.sort(Comparator
                .comparing(Seances::getDate)  // Trier par date
                .thenComparing(Seances::getHeureDebut));


        // Convertir les séances en DTO et ajouter les pauses
        return seancesList.stream().map(seance -> {
            SeanceDTO dto = SeanceDTO.toSeanceDTO(seance);
            LocalTime heureDebut = seance.getHeureDebut();
            LocalTime heureFin = seance.getHeureFin();

            List<String> plagesHoraires = common_service.calculerPlagesHoraires(heureDebut,heureFin);


            dto.setPlageHoraire(plagesHoraires);
            return dto;
        }).collect(Collectors.toList());
    }

    // Retourner une liste vide si la date de fin de l'emploi est passée
    return new ArrayList<>();
}



//--------------------------------------method add seance
    public List<Seances> createSeances(List<Seances> seancesList) {
        // Vérifie la date par rapport à l'emploi du temps
        List<Seances> seancesListNew = new ArrayList<>();
        for (Seances seances : seancesList) {
//            common_service.validateSeance(seances);




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

          seancesListNew.add(seances);


        }
        return seance_repositorie.saveAll(seancesListNew);
//
    }


//    -----------------------------------------methode delete
    public Object deletedSeance(long idSeance){
        Seances seanceExist = seance_repositorie.getById(idSeance);
        if(seanceExist != null){
           List<TeachersPresence> tpresent = listPresenceTeacher_repositorie.getByIdSeanceId(idSeance);
           if (tpresent.isEmpty()){
               seance_repositorie.delete(seanceExist);
               return DTO_response_string.fromMessage("Success la seance a ete supprimer", 200);

           }else {
               throw new NoteFundException("Impossible de supprimer des presences son deja associer");

           }

        }
        return DTO_response_string.fromMessage("La seance n'existe pas", 400);

    }
//-------------------------------------update method
//    public Object update(Seances seances){
//        Seances seanceExist = seance_repositorie.getById(seances.getId());
//        if(seanceExist != null) {
//            TeachersPresence tpresent = listPresenceTeacher_repositorie.findByIdSeanceId(seanceExist.getId());
////            System.out.println("t presence --------------------------------"+tpresent);
//
//            if (!tpresent.isObservation()) {
//                if(seances.getIdTeacher() != null){
//                    List<Seances> existingSeances = seance_repositorie.getAllByDateAndIdTeacherIdEnseignant(seances.getDate(),seances.getIdTeacher().getIdEnseignant());
//                    for (Seances existingSeance : existingSeances) {
//                        if (common_service.isOverlapping(seances, existingSeance)) {
//                            throw new RuntimeException("La séance chevauche une séance existante pour cet enseignant");
//                        }
//                    }
//                }
//
//                List<Seances> listExist = seance_repositorie.getAllByDate(seances.getDate());
//                if (!listExist.isEmpty()) {
//                    for (Seances sc : listExist) {
//                        boolean hasHeureIntervale = true;
//                        if (seances.getHeureFin().isAfter(sc.getHeureDebut()) && seances.getHeureFin().isBefore(sc.getHeureFin())) {
//                            hasHeureIntervale = false;
//                            throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
//                        }
//                    }
//                }
//
//                if (seances.getHeureDebut() != null) {
//                    seanceExist.setHeureDebut(seances.getHeureDebut());
//                }
//                if (seances.getHeureFin() != null) {
//                    seanceExist.setHeureFin(seances.getHeureFin());
//                }
//                if (seances.getIdModule() != null) {
//                    seanceExist.setIdModule(seances.getIdModule());
//                }
//                if (seances.getIdTeacher() != null) {
//                    seanceExist.setIdTeacher(seances.getIdTeacher());
//                }
//                if (seances.getIdSalle() != null) {
//                    seanceExist.setIdSalle(seances.getIdSalle());
//                }
//                if(seances.getDate() != null){
//                    seanceExist.setDate(seances.getDate());
//                }
//                if(seances.getIdEmplois() != null){
//                    seanceExist.setIdEmplois(seances.getIdEmplois());
//                }
//                seance_repositorie.save(seanceExist);
//            } else {
//                throw new NoteFundException("Impossible de modifier la séance car des présences y sont déjà associées.");
//
//            }
//            return DTO_response_string.fromMessage("Modification effectuée avec succès", 200);
//        }
//       throw new NoteFundException("seance does not exist");
//    }


//    --------------------------get seance by id
    public SeanceDTO get_by_id(long idSeance){
        Seances seanceExist = seance_repositorie.getById(idSeance);
        if (seanceExist != null) {

            return SeanceDTO.toSeanceDTO(seanceExist);
        }
        throw new NoteFundException("La seance n'existe pas");
    }
//------------------------------------------------------------------------------------------


}



