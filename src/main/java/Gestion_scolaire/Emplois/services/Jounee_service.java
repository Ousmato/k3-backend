package Gestion_scolaire.Emplois.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Emplois.dtos.Journee_DTO;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Teachers.dtos.TeacherConfigJournDTO;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Participant;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class Jounee_service {


    private final Shared_repositories shared_repositories;


    private final Common_service common_service;

    private final Validator validator;

    @Transactional
    public Object addJournee(List<Journee> journeeList) {
        boolean hasJour = false;

        for (Journee j : journeeList) {
            Set<ConstraintViolation<Journee>> constraintViolations = validator.validate(j);
            if (!constraintViolations.isEmpty()){
                throw new ConstraintViolationException(constraintViolations);
            }

            List<Journee> list = shared_repositories.getJournee_repositorie().findByIdEmploisIdAndIntervenantIdAndSeanceType(
                    j.getIdEmplois().getId(), j.getIntervenant().getId(), j.getSeanceType());
            if (!list.isEmpty()){
                for (Journee j2 : list) {

                    if (!j2.getIdSalle().equals(j.getIdSalle())) {
                        throw new NoteFundException("Impossible d'enregistrer deux salles différentes pour les séances de type %s pour l'enseignant %s"
                                .formatted(j.getSeanceType().toString().toUpperCase(), j.getIntervenant().getNom()));
                    }

                }
            }
            common_service.validateSeance(j);
            System.out.println("--------apres validation------" );

            List<Journee> existingJours = shared_repositories.getJournee_repositorie()
                    .findByIntervenantIdAndDate(j.getIntervenant().getId(), j.getDate());

            for (Journee existing : existingJours) {
                if (j.getHeureDebut().isBefore(existing.getHeureFin()) &&
                        j.getHeureFin().isAfter(existing.getHeureDebut())) {
                    throw new NoteFundException("Conflit : l’enseignant %s est déjà programmé de %s à %s le %s."
                        .formatted(
                                j.getIntervenant().getNom(),
                                existing.getHeureDebut(),
                                existing.getHeureFin(),
                                j.getDate()
                        )
                    );
                }
            }

            List<Salles> occuperForDate = common_service.salle_occuper(j.getDate(), j.getHeureDebut());
            if(!occuperForDate.isEmpty()) {
                for (Salles s : occuperForDate) {
                    if(s.equals(j.getIdSalle())){
                        throw new NoteFundException("La salle : " + j.getIdSalle().getNom() + " est occupé ");
                    }
                }

            }
            Journee jourExist = shared_repositories.getJournee_repositorie().getByHeureDebutAndHeureFinAndDateAndIdEmploisId(
                     j.getHeureDebut(), j.getHeureFin(), j.getDate(), j.getIdEmplois().getId()
            );

            if(jourExist !=null) {

                if (jourExist.getHeureFin().isAfter(j.getHeureDebut()) && jourExist.getHeureFin().isBefore(j.getHeureFin())) {
                    throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                }
            }
            shared_repositories.getJournee_repositorie().save(j);
            hasJour = true;

        }

        if (hasJour) {
            return DTO_response_string.addMessage();
        } else {
            throw new NoteFundException("Une configuration existante a été trouvée");
        }

    }


    public List<Journee_DTO> readByIdEmplois(long idEmplois) {
        // Récupérer l'emploi par ID
        Emplois emploisExist = shared_repositories.getEmplois_repositorie().findById(idEmplois);

        // Vérifier si l'emploi existe
        if (emploisExist == null) {
            throw new NoteFundException("L'emploi n'existe pas");
        }

        // Récupérer les séances associées à l'emploi
        List<Journee> seancesList = shared_repositories.getJournee_repositorie().findByIdEmploisId(emploisExist.getId());

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
//                List<StudentGroupDto> groupes = studentGroup_repositorie.getByIdEmploiId(seance.getIdEmplois().getId());
                Journee_DTO dto = Journee_DTO.toJourneeDTO(seance);
//                dto.setGroupes(groupes);
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

    //-----------------------
    @Transactional
    public Object addSurveillance(List<Journee> journees){
        boolean hasJour = false;
        for (Journee j : journees) {

            List<Journee> journeeList = shared_repositories.getJournee_repositorie().getByIdEmploisIdModuleId(j.getIdEmplois().getIdModule().getId());
            if(journeeList.isEmpty()) {
                throw new NoteFundException("Indisponible, aucune séance n'est programmer pour le moment");
            }
            common_service.validateSeance(j);

            Journee jExist = shared_repositories.getJournee_repositorie().getJourneesByDateAndHeureFinIsAfterAndIdEmploisId(j.getDate(),j.getHeureFin(),j.getIdEmplois().getId());
            if (jExist != null) {
                throw new NoteFundException("Un examen ne peut pas être planifié pendant un cours.");

//                for (Journee journee : list) {
//                    // Vérification si c'est un cours et non un examen ou une session
//                    if (!journee.getSeanceType().equals(Seance_type.examen) &&
//                            !journee.getSeanceType().equals(Seance_type.session)) {
//
//                        // Si l'examen est au même moment qu'un cours (chevauchement d'heures)
//                        if (!(j.getHeureFin().isBefore(journee.getHeureDebut()) ||
//                                j.getHeureDebut().isAfter(journee.getHeureFin()))) {
//                            throw new NoteFundException("Un examen ne peut pas être planifié pendant un cours.");
//                        }
//                    }
//                }
            }
            Journee teacherCofig = shared_repositories.getJournee_repositorie().findByIntervenantIdAndSeanceTypeAndDate(
                    j.getIntervenant().getId(),j.getSeanceType(), j.getDate());
            if(teacherCofig != null) {
                throw new NoteFundException("L'enseignant : " +j.getIntervenant().getNom() + "est déjà programmer pour cette date");
            }
            List<Salles> occuperForDate = common_service.salle_occuper_toDay(j.getDate());
            if(!occuperForDate.isEmpty()) {
                throw new NoteFundException("La salle : " + j.getIdSalle().getNom() + "est occupé");
            }

            Journee saved = shared_repositories.getJournee_repositorie().save(j);
            hasJour = true;

        }

        if (hasJour) {
            return DTO_response_string.fromMessage("Ajout effectué avec succès");
        } else {
            throw new NoteFundException("Une configuration existante a été trouvée");
        }
    }

    //--------------------------------
    public List<TeacherConfigJournDTO> getAllTeacherConfigByIdEmploi(long idEmplois) {

        Emplois emploisExist = shared_repositories.getEmplois_repositorie().findById(idEmplois);
        if(emploisExist == null) {
            return null;
        }
        List<Journee> journeeList = shared_repositories.getJournee_repositorie().findByIdEmploisId(idEmplois);

        if (journeeList.isEmpty()) {
            return new ArrayList<>();
        }

        // Vérifier si la date de fin de l'emploi est après la date actuelle
        if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
            // Utiliser un Set pour éviter les doublons de TeacherConfigJournDTO
            Set<TeacherConfigJournDTO> journeeConfigJournDTOSet = new HashSet<>();

            // Convertir les séances en DTO et ajouter les pauses
            for (Journee seance : journeeList){
                if(!seance.getSeanceType().equals(Seance_type.examen)) {
                    TeacherConfigJournDTO dto = new TeacherConfigJournDTO();
                    dto.setId(seance.getIntervenant().getId());
                    dto.setNom(seance.getIntervenant().getNom());
                    dto.setPrenom(seance.getIntervenant().getPrenom());
                    dto.setSalle(seance.getIdSalle().getNom());

                    Participant participant = seance.getIdParticipant();
                    if (participant != null) {
                        dto.setGroupe(participant.getIdStudentGroup().getNom());
                        dto.setIdGroupe(participant.getIdStudentGroup().getId());
                    }else {
                        continue;
                    }


                    // Utiliser un Set pour éviter les doublons dans les types de séance
                    Set<String> seanceTypesSet = new HashSet<>();

                    // Ajouter le type de séance de la journée courante au Set
                    seanceTypesSet.add(seance.getSeanceType().toString());

                    // Récupérer toutes les séances liées à cet enseignant pour éviter les doublons
                    List<Journee> list = shared_repositories.getJournee_repositorie().findByIdEmploisIdAndIntervenantId(idEmplois, seance.getIntervenant().getId());
                    for (Journee j : list) {
                        seanceTypesSet.add(j.getSeanceType().toString());

                    }

                    // Ajouter les types de séances uniques au DTO
                    String seanceTypeString = String.join("&", seanceTypesSet);
                    if (seanceTypeString.toLowerCase().contains("td") && dto.getGroupe() != null) {
                        dto.setSeanceType(dto.getGroupe() + "_" + seanceTypeString.toUpperCase());
                    } else {
                        dto.setSeanceType(seanceTypeString.toUpperCase());
                    }
                    journeeConfigJournDTOSet.add(dto);
                }
            }

            return journeeConfigJournDTOSet.stream().toList();

        }

        return new ArrayList<>();
    }

    //update journee
    public Object updateJournee(Journee journee) {
        // Vérifier si la journée existe
        Journee jExist = shared_repositories.getJournee_repositorie().getById(journee.getId());
        if (jExist == null) {
            throw new NoteFundException("La séance est introuvable");
        }
        List<Salles> occuperForDate = common_service.salle_occuper(journee.getDate(), journee.getHeureDebut());
        if(!occuperForDate.isEmpty()) {
            for (Salles s : occuperForDate) {
                if(s.equals(journee.getIdSalle())){
                    throw new NoteFundException("La salle : " + journee.getIdSalle().getNom() + " est occupé ");
                }
            }

        }
        // Vérifier si l'enseignant existe
        if (journee.getIntervenant() != null) {
            Teachers enseignant = shared_repositories.getTeacher_repositorie().findById(journee.getIntervenant().getId()).orElse(null);
            if (enseignant == null) {
                throw new NoteFundException("L'enseignant spécifié n'existe pas");
            }
            jExist.setIntervenant(enseignant); // Assigner l'enseignant trouvé
        }

        // Vérifier si la salle existe
        if (journee.getIdSalle() != null) {
            Salles salle = shared_repositories.getSalles_repositorie().findById(journee.getIdSalle().getId()).orElse(null);
            if (salle == null) {
                throw new NoteFundException("La salle spécifiée n'existe pas");
            }
            jExist.setIdSalle(salle); // Assigner la salle trouvée
        }

        // Mettre à jour les autres informations de la journée
        jExist.setSeanceType(journee.getSeanceType());
        jExist.setHeureDebut(journee.getHeureDebut());
        jExist.setHeureFin(journee.getHeureFin());

        // Sauvegarder l'entité journee mise à jour
        shared_repositories.getJournee_repositorie().save(jExist);

        // Retourner un message de succès
        return DTO_response_string.updateMessage();
    }

    //deleted
    public Object deletedJournee(long idJournee){
        // Vérifier si la journée existe
        Journee jExist = shared_repositories.getJournee_repositorie().getById(idJournee);
        if (jExist == null) {
            throw new NoteFundException("La séance est introuvable");
        }
        shared_repositories.getJournee_repositorie().delete(jExist);
        return DTO_response_string.updateMessage();
    }

}
