package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.Journee_DTO;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Journee_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Jounee_service {

    @Autowired
    private Journee_repositorie journee_repositorie;

    @Autowired
    private Common_service common_service;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    public Object addJournee(List<Journee> journeeList) {
        boolean hasJour = false;

        for (Journee j : journeeList) {
            common_service.validateSeance(j);

            Journee teacherCofig = journee_repositorie.findByIdTeacherIdEnseignantAndSeanceTypeAndDate(
                    j.getIdTeacher().getIdEnseignant(),j.getSeanceType(), j.getDate());
            if(teacherCofig != null) {
               throw new NoteFundException("L'enseignant : " +j.getIdTeacher().getNom() + "est déjà programmer pour cette date");
            }
            List<Salles> occuperForDate = common_service.salle_occuper_toDay(j.getDate());
            if(!occuperForDate.isEmpty()) {
                throw new NoteFundException("La salle : " + j.getIdSalle().getNom() + "est occupé");
            }
            Journee jourExist = journee_repositorie.getByHeureDebutAndHeureFinAndDate(
                     j.getHeureDebut(), j.getHeureFin(), j.getDate()
            );
            if(jourExist !=null) {
                if(!jourExist.getSeanceType().equals(j.getSeanceType()) || jourExist.getHeureFin().isAfter(j.getHeureDebut()) && jourExist.getHeureFin().isBefore(j.getHeureFin()) ) {
                    throw new NoteFundException("Invalide il existe déjà un " +jourExist.getSeanceType().toString().toUpperCase() +" pour cette date");
                }
                if (jourExist.getHeureFin().isAfter(j.getHeureDebut()) && jourExist.getHeureFin().isBefore(j.getHeureFin())) {
                    throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                }
            }
            Journee saved = journee_repositorie.save(j);
            common_service.createPaieForConfig(saved);
            hasJour = true;

        }

        if (hasJour) {
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        } else {
            throw new NoteFundException("Une configuration existante a été trouvée");
        }

    }

    public List<Journee_DTO> readByIdEmplois(long idEmplois) {
        // Récupérer l'emploi par ID
        Emplois emploisExist = emplois_repositorie.findById(idEmplois);

        // Vérifier si l'emploi existe
        if (emploisExist == null) {
            throw new NoteFundException("L'emploi n'existe pas");
        }

        // Récupérer les séances associées à l'emploi
        List<Journee> seancesList = journee_repositorie.findByIdEmploisId(emploisExist.getId());

        // Vérifier si la liste des séances n'est pas vide
        if (seancesList.isEmpty()) {
            // Retourner une liste vide si aucune séance n'est trouvée
            return new ArrayList<>();
        }

        // Vérifier si la date de fin de l'emploi est après la date actuelle
        if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
            // Trier les séances par date et heure de début
            seancesList.sort(Comparator
                    .comparing(Journee::getDate)  // Trier par date
                    .thenComparing(Journee::getHeureDebut));


            // Convertir les séances en DTO et ajouter les pauses
            return seancesList.stream().map(seance -> {
                Journee_DTO dto = Journee_DTO.toJourneeDTO(seance);
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
}
