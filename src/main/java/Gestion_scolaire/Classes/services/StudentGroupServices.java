package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Classes.dtos.StudentGroupDto;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import Gestion_scolaire.students.entity.Participant;
import Gestion_scolaire.students.entity.StudentGroupe;
import Gestion_scolaire.students.entity.StudentsClasse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentGroupServices {

    @Autowired
    private Shared_services shared_services;

    @Autowired
    private Shared_repositories shared_repositories;

    public List<StudentGroupDto> ge_allBy_idClass(long idClasse){
        List<StudentGroupe> groupes =shared_repositories.getStudentGroup_repositorie().getByIdEmploiIdClasseId(idClasse);

        List<StudentGroupDto> dtos = new ArrayList<>();
        if(groupes.isEmpty()){
            return new ArrayList<>();
        }
        groupes.forEach(gr -> {
            StudentGroupDto dto = new StudentGroupDto();
            List<InscriptionNoteDTO> inscriptionDTOS = new ArrayList<>();  // Nouvelle liste à chaque groupe

            List<Participant> participants = shared_repositories.getParticipant_repositorie().findByIdStudentGroupId(gr.getId());

            dto.setId(gr.getId());
            dto.setNom(gr.getNom());
            dto.setNomModule(gr.getIdEmploi().getIdModule().getNomModule());

            if (!participants.isEmpty()) {
                participants.forEach(participant -> {
                    InscriptionNoteDTO inscriptionDTO = new InscriptionNoteDTO();
                    inscriptionDTO.setSexe(participant.getIdInscription().getIdEtudiant().getSexe());
                    inscriptionDTO.setNom(participant.getIdInscription().getIdEtudiant().getNom());
                    inscriptionDTO.setLieuNaissance(participant.getIdInscription().getIdEtudiant().getLieuNaissance());
                    inscriptionDTO.setDateNaissance(participant.getIdInscription().getIdEtudiant().getDateNaissance());
                    inscriptionDTOS.add(inscriptionDTO);
                });
            }

            dto.setInscriptions(inscriptionDTOS);
            dtos.add(dto);  // Ajouter le DTO complet à la liste de résultats
        });

        return dtos;
    }


    //get all participation by emploi id
    public List<Participant> ge_allParticipantBy_idClass(long idClasse){
        List<Participant> participantList = shared_repositories.getParticipant_repositorie().getByIdStudentGroupIdEmploiIdClasseId(idClasse);

        if(participantList.isEmpty()){
            return new ArrayList<>();
        }
        return participantList;
    }

    //get all participants of group
    public StudentGroupDto getAllParticipantsOfGrp(long idGroup, long idEmploi){
        StudentGroupe grp = shared_services.getGroupe_service().getStudentGroupeById(idGroup);
        Emplois emploi = shared_services.getEmplois_service().getById(idEmploi);
        StudentGroupDto dto = new StudentGroupDto();
        dto.setId(grp.getId());
        dto.setNom(grp.getNom());
        dto.setNomModule(emploi.getIdModule().getNomModule());
        dto.setAnnee(emploi.getIdClasse().getIdAnneeScolaire());
        dto.setSemestre(emploi.getIdSemestre().getNomSemetre());
        dto.setClasse(shared_services.getShared_methods_service().abregNiveauName(emploi.getIdClasse().getIdFiliere().getIdNiveau().getNom()) + " " + shared_services.getShared_methods_service().abrevigateName(emploi.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere()));
        List<InscriptionNoteDTO> inscriptionDTOS = new ArrayList<>();  // Nouvelle liste à chaque groupe

        List<Participant> participants = shared_repositories.getParticipant_repositorie().findByIdStudentGroupId(idGroup);

        participants.forEach(participant -> {
            InscriptionNoteDTO inscriptionDTO = new InscriptionNoteDTO();
            inscriptionDTO.setSexe(participant.getIdInscription().getIdEtudiant().getSexe());
            inscriptionDTO.setNom(participant.getIdInscription().getIdEtudiant().getNom());
            inscriptionDTO.setPrenom(participant.getIdInscription().getIdEtudiant().getPrenom());
            inscriptionDTO.setLieuNaissance(participant.getIdInscription().getIdEtudiant().getLieuNaissance());
            inscriptionDTO.setDateNaissance(participant.getIdInscription().getIdEtudiant().getDateNaissance());
            inscriptionDTOS.add(inscriptionDTO);

        });
        dto.setInscriptions(inscriptionDTOS);
        return dto;

    }

    //get group by id


}
