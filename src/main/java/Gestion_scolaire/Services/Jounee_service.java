package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.Journee_DTO;
import Gestion_scolaire.Dto_classe.TeacherConfigJournDTO;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Journee_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Jounee_service {

    @Autowired
    private Journee_repositorie journee_repositorie;

    @Autowired
    private Common_service common_service;

    @Autowired
    private Validator validator;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Transactional
    public Object addJournee(List<Journee> journeeList) {
        boolean hasJour = false;

        for (Journee j : journeeList) {
            Set<ConstraintViolation<Journee>> constraintViolations = validator.validate(j);
            if (!constraintViolations.isEmpty()){
                throw new ConstraintViolationException(constraintViolations);
            }

            List<Journee> list = journee_repositorie.findByIdEmploisIdAndIdTeacherIdEnseignantAndSeanceType(
                    j.getIdEmplois().getId(), j.getIdTeacher().getIdEnseignant(), j.getSeanceType());
            if (!list.isEmpty()){
                for (Journee j2 : list) {

                    if (!j2.getIdSalle().equals(j.getIdSalle())) {
                        throw new NoteFundException("Impossible d'enregistrer deux salles différentes pour les séances de type %s pour l'enseignant %s"
                                .formatted(j.getSeanceType().toString().toUpperCase(), j.getIdTeacher().getNom()));
                    }

                }
            }
            common_service.validateSeance(j);

            Journee teacherCofig = journee_repositorie.findByIdTeacherIdEnseignantAndSeanceTypeAndDate(
                    j.getIdTeacher().getIdEnseignant(),j.getSeanceType(), j.getDate());
            if(teacherCofig != null) {
               throw new NoteFundException("L'enseignant : %sest déjà programmer pour cette date".formatted(j.getIdTeacher().getNom()));
            }
            List<Salles> occuperForDate = common_service.salle_occuper(j.getDate(), j.getHeureDebut());
            if(!occuperForDate.isEmpty()) {
                for (Salles s : occuperForDate) {
                    if(s.equals(j.getIdSalle())){
                        System.out.println("--------------" + j.getIdSalle());
                        throw new NoteFundException("La salle : " + j.getIdSalle().getNom() + " est occupé ");
                    }
                }

            }
            Journee jourExist = journee_repositorie.getByHeureDebutAndHeureFinAndDateAndIdEmploisId(
                     j.getHeureDebut(), j.getHeureFin(), j.getDate(), j.getIdEmplois().getId()
            );
            System.out.println("---------------------je suis la" + jourExist);

            if(jourExist !=null) {

                if (jourExist.getHeureFin().isAfter(j.getHeureDebut()) && jourExist.getHeureFin().isBefore(j.getHeureFin())) {
                    throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                }
            }
            System.out.println("----------------------------"+j);
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

    //-----------------------
    @Transactional
    public Object addSurveillance(List<Journee> journees){
        boolean hasJour = false;
        for (Journee j : journees) {

            List<Journee> journeeList = journee_repositorie.getByIdEmploisIdModuleId(j.getIdEmplois().getIdModule().getId());
            if(journeeList.isEmpty()) {
                throw new NoteFundException("Indisponible, aucune séance n'est programmer pour le moment");
            }
            common_service.validateSeance(j);

            List<Journee> list = journee_repositorie.findByIdEmploisId(j.getIdEmplois().getId());
            if (!list.isEmpty()) {
                for (Journee journee : list) {
                    // Vérification si c'est un cours et non un examen ou une session
                    if (!journee.getSeanceType().equals(Seance_type.examen) &&
                            !journee.getSeanceType().equals(Seance_type.session)) {

                        // Si l'examen est au même moment qu'un cours (chevauchement d'heures)
                        if (!(j.getHeureFin().isBefore(journee.getHeureDebut()) ||
                                j.getHeureDebut().isAfter(journee.getHeureFin()))) {
                            throw new NoteFundException("Un examen ne peut pas être planifié pendant un cours.");
                        }
                    }
                }
            }
            Journee teacherCofig = journee_repositorie.findByIdTeacherIdEnseignantAndSeanceTypeAndDate(
                    j.getIdTeacher().getIdEnseignant(),j.getSeanceType(), j.getDate());
            if(teacherCofig != null) {
                throw new NoteFundException("L'enseignant : " +j.getIdTeacher().getNom() + "est déjà programmer pour cette date");
            }
            List<Salles> occuperForDate = common_service.salle_occuper_toDay(j.getDate());
            if(!occuperForDate.isEmpty()) {
                throw new NoteFundException("La salle : " + j.getIdSalle().getNom() + "est occupé");
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

    //--------------------------------
    public List<TeacherConfigJournDTO> getAllTeacherConfigByIdEmploi(long idEmplois) {

        Emplois emploisExist = emplois_repositorie.findById(idEmplois);
        if(emploisExist == null) {
            return null;
        }
        List<Journee> journeeList = journee_repositorie.findByIdEmploisId(idEmplois);

        if (journeeList.isEmpty()) {
            return new ArrayList<>();
        }

        // Vérifier si la date de fin de l'emploi est après la date actuelle
        if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
            // Utiliser un Set pour éviter les doublons de TeacherConfigJournDTO
            Set<TeacherConfigJournDTO> journeeConfigJournDTOSet = new HashSet<>();

            // Convertir les séances en DTO et ajouter les pauses
            journeeList.forEach(seance -> {

                TeacherConfigJournDTO dto = new TeacherConfigJournDTO();
                dto.setId(seance.getIdTeacher().getIdEnseignant());
                dto.setNom(seance.getIdTeacher().getNom());
                dto.setPrenom(seance.getIdTeacher().getPrenom());
                dto.setSalle(seance.getIdSalle().getNom());

                Participant participant = seance.getIdParticipant();
                if (participant == null) {
                    dto.setGroupe(null);
                } else {
                    dto.setGroupe(participant.getIdStudentGroup().getNom());
                }

                // Utiliser un Set pour éviter les doublons dans les types de séance
                Set<Seance_type> seanceTypesSet = new HashSet<>();

                // Ajouter le type de séance de la journée courante au Set
                seanceTypesSet.add(seance.getSeanceType());

                // Récupérer toutes les séances liées à cet enseignant pour éviter les doublons
                List<Journee> list = journee_repositorie.findByIdEmploisIdAndIdTeacherIdEnseignant(idEmplois, seance.getIdTeacher().getIdEnseignant());
                for (Journee j : list) {
                    seanceTypesSet.add(j.getSeanceType());
                }

                // Ajouter les types de séances uniques au DTO
                dto.setSeanceType(new ArrayList<>(seanceTypesSet));

                // Ajouter le DTO au Set (évitera automatiquement les doublons)
                journeeConfigJournDTOSet.add(dto);
            });

            // Retourner la liste des enseignants uniques après conversion du Set en List
            return new ArrayList<>(journeeConfigJournDTOSet);
        }


        return new ArrayList<>();  // Si la date de fin est avant aujourd'hui, retourner une liste vide
    }

}
