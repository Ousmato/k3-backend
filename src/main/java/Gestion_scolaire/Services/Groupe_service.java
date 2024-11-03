package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Participant_repositorie;
import Gestion_scolaire.Repositories.StudentGroup_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Groupe_service {

    @Autowired
    private StudentGroup_repositorie group_repositorie;

    @Autowired
    private Validator validator;
    @Autowired
    private Participant_repositorie participant_repositorie;
    @Autowired
    private StudentGroup_repositorie studentGroup_repositorie;

    public Object add_group(StudentGroupe studentGroupe){
        Set<ConstraintViolation<StudentGroupe>> violations = validator.validate(studentGroupe);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
        StudentGroupe groupeExist = group_repositorie.findByIdEmploiIdAndNom(studentGroupe.getIdEmploi().getId(), studentGroupe.getNom());
        if(groupeExist == null){
            group_repositorie.save(studentGroupe);
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);

        }
        throw new NoteFundException("Le groupe exist déjà");
    }
//    -----------------------get all group
    public List<StudentGroupe> findAll(){
        return group_repositorie.findAll();
    }

    public List<StudentGroupe> listGroupByIdEmploi(long idEmploi){
        List<StudentGroupe> list = group_repositorie.getByIdEmploiId(idEmploi);
        if(list == null){
            return new ArrayList<>();
        }
        return list;
    }

//-------------------------------------------------------------------
//    add participant
    public Object add_participant(List<Participant> listParticipant) {
        boolean addedAtLeastOne = false;
        if(listParticipant.isEmpty()){
            throw new NoteFundException("Veuillez choisir au moins un étudiant");

        }
        for (Participant participant : listParticipant) {
            Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
            if(!violations.isEmpty()){
                throw new ConstraintViolationException(violations);
            }

            // Vérification de nullité
            if (participant == null) {
                throw new NoteFundException("Participant non valide");
            }

            if (participant.getIdInscription() == null) {
                throw new NoteFundException("Veuillez choisir au moins un étudiant pour le participant : " + participant);
            }

            if (participant.getIdStudentGroup() == null) {
                throw new NoteFundException("Veuillez choisir un groupe d'étudiants pour le participant : " + participant);
            }

            Participant oldParticipant = participant_repositorie.findByIdInscriptionIdAndIdStudentGroupIdEmploiId(
                    participant.getIdInscription().getId(), participant.getIdStudentGroup().getIdEmploi().getId());
            if(oldParticipant != null){
                throw new NoteFundException("Invalide un étudiant ne dois pas etre dans deux groupe a la fois");
            }

            // Recherche de la participation existante
            Participant p_Exist = participant_repositorie.findByIdInscriptionIdAndIdStudentGroupId(
                    participant.getIdInscription().getId(),
                    participant.getIdStudentGroup().getId());

            if (p_Exist == null) {

                participant_repositorie.save(participant);
                addedAtLeastOne = true;
            }
        }

        if (addedAtLeastOne) {
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        } else {
            throw new NoteFundException("Toutes les participations existent déjà");
        }
    }


    //    ---------------get all participation by emploi id
    public List<Participant> ge_allBy_idClass(long idEmploi){
        List<Participant> participantList = participant_repositorie.getAllByIdStudentGroupIdEmploiId(idEmploi);

        if(participantList.isEmpty()){
            return new ArrayList<>();
        }
        return participantList;
    }

//    ------------------------get list student of group
    public List<Inscription> getAllStudentsByGroupId(long groupId) {
        // Récupérer les participants associés au groupe
        List<Participant> participantList = participant_repositorie.findByIdStudentGroupId(groupId);

        // Mapper les participants pour obtenir la liste des étudiants
        return participantList.stream()
                .map(Participant::getIdInscription)
                .collect(Collectors.toList());
    }

    public List<Inscription> getAllStudentsByGroupes(long idEmploi) {
        // Récupérer les participants associés au groupe
        List<Participant> participantList = participant_repositorie.getAllByIdStudentGroupIdEmploiId(idEmploi);

        // Mapper les participants pour obtenir la liste des étudiants
        return participantList.stream()
                .map(Participant::getIdInscription)
                .collect(Collectors.toList());
    }


}
