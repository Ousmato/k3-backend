package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.dtos.ModuleDTO;
import Gestion_scolaire.Classes.dtos.UeDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Note_service {
    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Shared_methods_service shared_methods_service;

    @Autowired
    private Shared_services sharedservices;

    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private Common_service common_service;


    //    ------------------------------methode pour modifier la note d'un etudiant---------------------
    @Transactional
    public Object update(Notes notes){
        Notes noteExist = notes_repositorie.findById(notes.getId());
        if (noteExist == null){
            throw new RuntimeException("la note n'existe pas");
        }
        noteExist.setExamNote(notes.getExamNote());
        noteExist.setIdAdmin(notes.getIdAdmin());
        
        noteExist.setClasseNote(notes.getClasseNote());
        notes_repositorie.save(noteExist);
        return DTO_response_string.fromMessage("Mise à jour effectué avec succè");
    }

    //mehode pour ajouter une note
    @Transactional
    public Object addNote(AddNoteDTO dto) {
        // Récupérer les entités associées

        Modules module = sharedservices.getModules_service().getModule(dto.getIdModule());
        Semestres semestre = sharedservices.getSemestre_service().getSemestre(dto.getIdSemestre());
        Inscription inscription = sharedservices.getInscription_service().getInscription(dto.getIdInscription());
        if (module.getIdUe().getNomUE().toLowerCase().contains("lib")){
            // Traitement de la note (création ou mise à jour)
           return addUeLibre(dto, module, inscription, semestre);
        }
        // Convertir l'objet DTO en entité Notes
        Notes note = new Notes();
        note.setClasseNote(dto.getClasseNote());
        note.setExamNote(dto.getExamNote());
        note.setIdAdmin(dto.getIdAdmin());
        note.setIdModule(module);
        note.setIdSemestre(semestre);
        note.setIdInscription(inscription);

        double noteModule = ((dto.getExamNote() * 2) + dto.getClasseNote()) / 3;
       noteModule = Math.round(noteModule * 100.0) / 100.0;

//        System.out.println( "-------------la note ponderer du module-------------------" + noteModule);

        // Récupérer la note existante
        Notes noteExist = notes_repositorie.findStudentNoteByModuleAndSemestre(
                semestre.getId(), inscription.getId(), module.getId());
        if(noteExist != null){
            System.out.println( "-------------il existe donc modifier-------------------");
          Notes updateNote =  updateExistingNote(noteExist, note, noteModule);

            return buildNoteDto(dto, updateNote);
        }
        System.out.println( "-------------la les ids-------------------" + dto.getIdSemestre() + dto.getIdModule()+ dto.getIdInscription());

        // Traitement de la note (création ou mise à jour)
        Notes savedNote =  saveNewNote(note, noteModule);

        // Calcul des informations supplémentaires pour le DTO de sortie
        return buildNoteDto(dto, savedNote);
    }

    //list de notes de tous les etudiant pour le semestre
    public Page<StudentsNotesDTO> listNotes(int page, int pageSize, long idClasse, long idSemestre) {

        Pageable pageable = PageRequest.of(page, pageSize);

       List<Long> validIds = common_service.getIdsOfStudents(idSemestre,idClasse);
        List<StudentsNotesDTO> studentsNotesDTOList = new ArrayList<>();

        validIds.forEach(idInscription -> {
            Inscription inscrit = sharedservices.getInscription_service().getById(idInscription);

            if (inscrit != null) {
                List<GetNoteDTO> noteDTOList = new ArrayList<>(getNotesByIdStudentAndIdSemestre(idInscription, idSemestre));
                noteDTOList.sort(Comparator.comparing(GetNoteDTO ->GetNoteDTO.getUes().getCode()));
                StudentsNotesDTO studentsNotesDTO = new StudentsNotesDTO();
                studentsNotesDTO.setNoteDTO(noteDTOList);
                studentsNotesDTO.setMoyenGeneral(0);
                studentsNotesDTO.setId(inscrit.getId());
                studentsNotesDTO.setNom(inscrit.getIdEtudiant().getNom());
                studentsNotesDTO.setPrenom(inscrit.getIdEtudiant().getPrenom());
                studentsNotesDTO.setDate_naissance(inscrit.getIdEtudiant().getDateNaissance());
                studentsNotesDTO.setLieuNaissance(inscrit.getIdEtudiant().getLieuNaissance());
                String sexe = inscrit.getIdEtudiant().getSexe();
                studentsNotesDTO.setSexe(Objects.equals(sexe, "FEMME") ? "F" : "M");

//                System.out.println("Ajout de l'étudiant: " + studentsNotesDTO.getNom() + " " + studentsNotesDTO.getPrenom());

                studentsNotesDTOList.add(studentsNotesDTO);

//                System.out.println("Étudiant avec idInscription = " + idInscription + " a le bon nombre de notes.");
            } else {
                // Gérer le cas où l'inscription est absente si nécessaire
                throw new NoteFundException("Inscription non trouver");
            }
        });

        // Pagination de la liste
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageSize, studentsNotesDTOList.size());
        List<StudentsNotesDTO> paginatedList = studentsNotesDTOList.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, studentsNotesDTOList.size());
    }


    //get note of student for relever
    public List<GetNoteDTO> getNotesByIdStudentAndIdSemestre(long idStudent, long idSemestre) {
        // Vérification de l'existence de l'étudiant
        Inscription studentExist = sharedservices.getInscription_service().getInscription(idStudent);
        if (studentExist == null) {
            throw new RuntimeException("L'étudiant n'existe pas");
        }

        Semestres semestreExist = sharedservices.getSemestre_service().getSemestre(idSemestre);
        if (semestreExist == null) {
            throw new NoteFundException("Le semestre est introuvable");
        }


        List<Notes> notes = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);
        if (notes == null || notes.isEmpty()) {
            throw new NoteFundException("Aucune note trouvée");
        }



        List<GetNoteDTO> result = new ArrayList<>();

        // Récupération des modules ayant des notes
        List<Modules> modulesList = sharedservices.getModules_service().allModulesWithNotes(idStudent, idSemestre);

        // Groupement des modules par UE
        Map<UE, List<Modules>> modulesByUe = new HashMap<>();
        for (Modules module : modulesList) {
            modulesByUe.computeIfAbsent(module.getIdUe(), k -> new ArrayList<>()).add(module);
        }

        // Parcours des UEs pour construire les DTOs
        for (Map.Entry<UE, List<Modules>> entry : modulesByUe.entrySet()) {
            UE ue = entry.getKey();
            List<Modules> ueModules = entry.getValue();

            // Création de l'objet GetNoteDTO pour chaque UE
            GetNoteDTO dto = new GetNoteDTO();
            UeDTO ueDTO = new UeDTO();
            ueDTO.setId(ue.getId());
            ueDTO.setCode(ue.getCodeUE());
            ueDTO.setNomUE(ue.getNomUE());

            List<ModuleDTO> moduleDTOList = new ArrayList<>();
            double sommeNoteModule = 0.0;
            int somCoefUe = 0;

            for (Modules module : ueModules) {
                Notes note = notes_repositorie.getNotesByIdModuleIdAndIdInscriptionIdAndIdSemestreId(
                        module.getId(), idStudent, idSemestre
                );

                ModuleDTO moduleDTO = new ModuleDTO();
                moduleDTO.setNomModule(module.getNomModule());
                moduleDTO.setNoteModule(note.getNoteModule());
                moduleDTOList.add(moduleDTO);

                sommeNoteModule += note.getNoteModule() * module.getCoefficient();
                somCoefUe += module.getCoefficient();
            }

            // Calcul de la moyenne UE
            double noteUe = somCoefUe > 0 ? sommeNoteModule / somCoefUe : 0.0;
            noteUe = Math.round(noteUe * 100.0) / 100.0;

            ueDTO.setModules(moduleDTOList);
            dto.setUes(ueDTO);
            dto.setCoefUe(somCoefUe);
            dto.setMoyenUe(noteUe);

            result.add(dto);
        }

        return result;
    }


    private Notes saveNewNote(Notes note, double noteModule) {
        note.setNoteModule(noteModule);
        return notes_repositorie.save(note);
    }

    private Notes updateExistingNote(Notes noteExist, Notes note, double noteModule) {
        noteExist.setNoteModule(noteModule);
        noteExist.setExamNote(note.getExamNote());
        noteExist.setIdAdmin(note.getIdAdmin());
        noteExist.setClasseNote(note.getClasseNote());
        return notes_repositorie.save(noteExist);
    }
    private GetInputNoteInscritDTO buildNoteDto(AddNoteDTO dto, Notes savedNote) {
        GetInputNoteInscritDTO noteDto = new GetInputNoteInscritDTO();

        // Récupération de la session et calcul des valeurs
        double noteUe = shared_methods_service.noteUe(savedNote.getIdModule().getIdUe().getId(), dto.getIdSemestre(), savedNote.getIdInscription().getId());
        StudentSession session = shared_repositories.getSessions_repositorie().findByIdInscritIdAndIdSemestreIdAndIdModuleId(dto.getIdInscription(), dto.getIdSemestre(), dto.getIdModule());

        if (session == null) {
            noteDto.setSessionNote(0.0);
            noteDto.setNbreSession(1);
        } else {
            noteDto.setSessionNote(session.getNoteSession());
            noteDto.setNbreSession(session.getNbreSession());
            noteDto.setNoteUe(Math.max(noteUe, session.getNoteSession()));
        }

        // Attribution des valeurs au DTO de retour
        noteDto.setExamNote(savedNote.getExamNote());
        noteDto.setClasseNote(savedNote.getClasseNote());
        noteDto.setNoteUe(noteUe);
        noteDto.setValidate(noteUe >= 10 ? "ADj" : "AJ");

        return noteDto;
    }

    private Notes addUeLibre(AddNoteDTO dto, Modules module, Inscription inscription, Semestres semestre){
        Notes note = new Notes();
        note.setExamNote(dto.getClasseNote());
        note.setExamNote(dto.getClasseNote());
        note.setNoteModule(dto.getClasseNote());
        note.setIdAdmin(dto.getIdAdmin());
        note.setIdModule(module);
        note.setIdSemestre(semestre);
        note.setIdInscription(inscription);
        return notes_repositorie.save(note);
    }

    public List<UeValidateDTO> getUeValideByStudentBySemestre(long idInscrit, long idClasse){
        List<UeValidateDTO> ueValidateList = new ArrayList<>();

        List<Semestres> semestres = sharedservices.getSemestre_service().getCurrenctSemestresByIdNivFil(idClasse);
        for (Semestres semestre : semestres) {
            double countUeValide = 0;
            double countUeTotal = 0;
            Moyenne moyenne = shared_repositories.getMoyenne_repositorie().findByIdSemestreIdAndIdInscriptionId(semestre.getId(), idInscrit);
            List<GetNoteDTO> dtos = getNotesByIdStudentAndIdSemestre(idInscrit, semestre.getId());
            UeValidateDTO ueValidateDTO = new UeValidateDTO();

            for (GetNoteDTO dto : dtos) {
                if (dto.getUes().getCode().contains("LIB")){
                    continue;
                }
                countUeTotal++;
//                System.out.println("------------la moyenne--------" + dto.getMoyenUe());

                if (dto.getMoyenUe() >= 10){
                    countUeValide++;
                }

            }
            // Calcul du pourcentage des UE validées
            double ueValidatedPercentage = countUeTotal > 0 ? (countUeValide / countUeTotal) * 100 : 0;

//            System.out.println("------------les ues valider--------" + countUeValide);
//            System.out.println("------------Moyenne--------" + moyenne.getMoyenGenerale());
//
//
//            System.out.println("------------semestre--------" + semestre.getId());
//            System.out.println("------------semestre--------" + semestre.getNomSemetre());
//
//            System.out.println("-------------id inscrit----------" + idInscrit);
//
//            System.out.println("-------UE Total---------" + countUeTotal);


            // Remplir les données du DTO
            ueValidateDTO.setNomSemestre(semestre.getNomSemetre());
            ueValidateDTO.setPercentUeSemestre(Math.round(ueValidatedPercentage)); // Arrondi à 2 décimales
            ueValidateDTO.setMoyenSemestre(moyenne != null ? moyenne.getMoyenGenerale() : 0.0);

            // Ajouter le DTO à la liste
            ueValidateList.add(ueValidateDTO);


        }
        return ueValidateList;
    }

    public double calculerPourcentageUeValide(List<GetNoteDTO> ueDtos) {
        int totalUe = 0;
        int ueValidees = 0;

        // Parcourir les UE
        for (GetNoteDTO ueDto : ueDtos) {
            totalUe++; // Chaque UE est comptée
            if (ueDto.getMoyenUe() >= 10) {
                ueValidees++; // UE validée si la moyenne est >= 10
            }
        }

        // Calculer le pourcentage
        if (totalUe == 0) {
            return 0.0; // Éviter la division par zéro
        }
        return (ueValidees / (double) totalUe) * 100;
    }


}
