package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Note_service {
    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Moyenne_repositorie moyenne_repositorie;

    @Autowired
    private Validator validator;

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;


//    ------------------------------methode pour modifier la note d'un etudiant---------------------
    public Object update(Notes notes){
        Notes noteExist = notes_repositorie.findById(notes.getId());
        if (noteExist == null){
            throw new RuntimeException("la note n'existe pas");
        }
        noteExist.setExamNote(notes.getExamNote());
        noteExist.setIdAdmin(notes.getIdAdmin());
        
        noteExist.setClasseNote(notes.getClasseNote());
        notes_repositorie.save(noteExist);
        return DTO_response_string.fromMessage("Mise à jour effectué avec succè", 200);
    }

//    ---------------------------------------------mehode pour ajouter une note----------------------
    public Object addNote(Notes notes ) {
        Set<ConstraintViolation<Notes>> violations = validator.validate(notes);
        if (!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
        Notes noteExist = notes_repositorie.findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(notes.getIdSemestre().getId(), notes.getIdModule().getId(), notes.getIdInscription().getId());

        System.out.println("------------------class ---------------- " + notes.getClasseNote());
        System.out.println("---------------exam ---------------- " + notes.getExamNote());


        double noteModule = ((notes.getExamNote() * 2) + notes.getClasseNote()) / 3;
        noteModule = Math.round(noteModule * 100.0)/100.0;
        if (noteExist == null) {
            notes.setNoteModule(noteModule);
            notes_repositorie.save(notes);
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        }else {
            System.out.println("----------------note mdule ---------------- " + noteModule);

            noteExist.setNoteModule(noteModule);
            noteExist.setExamNote(notes.getExamNote());
            noteExist.setIdAdmin(notes.getIdAdmin());
            noteExist.setClasseNote(notes.getClasseNote());
            notes_repositorie.save(noteExist);
            return DTO_response_string.fromMessage("Mise a jour effectué avec succès", 200);

        }


    }

    //methode pour appeler tout les module de la
    public ArrayList<Modules> readAllByAllEmplois(long idClasse){
//        Emplois emploisList = emplois_repositorie.getEmploisByDateFinAfterAndIdClasseId(LocalDate.now(), idClasse);
        List<ClasseModule> classeModuleList = classeModule_repositorie.findByIdNiveauFiliereId(idClasse);
        Set<Modules> modulesSet = new HashSet<>();

            for (ClasseModule clm : classeModuleList){
                UE ue = clm.getIdUE();
            if (ue != null) {
                List<Modules> toutModule = modules_repositories.findByIdUeId(ue.getId());
                modulesSet.addAll(toutModule);

            }

        }
        return new ArrayList<>(modulesSet);
    }

    //list de notes de tous les etudiant pour le semestre
    public Page<StudentsNotesDTO> listNotes(int page, int pageSize, long idClasse, long idSemestre, long idNivFiliere) {
        Pageable pageable = PageRequest.of(page, pageSize);

       List<Long> validIds = getIdsOfStudents(idSemestre,idNivFiliere,idClasse);
        List<StudentsNotesDTO> studentsNotesDTOList = new ArrayList<>();

        validIds.forEach(idInscription -> {
            Optional<Inscription> inscritOpt = inscription_repositorie.findById(idInscription);

            if (inscritOpt.isPresent()) {
                Inscription inscrit = inscritOpt.get();
                List<GetNoteDTO> noteDTOList = new ArrayList<>(getNotesByIdStudentAndIdSemestre(idInscription, idSemestre));

                noteDTOList.forEach(note ->{

                    System.out.println(" - Note ID: " + note.getIdNote() + ", Valeur: " + note.getMoyenUe()
                            );
                    note.getUes().getModules().forEach(module -> {
                        System.out.println(" - Matière: "+ module.getNomModule());
                    });
                });
                Moyenne myG = moyenne_repositorie.findByIdSemestreIdAndIdInscriptionId(idSemestre, idInscription);
                if (myG == null) {
                    return;
                }
                StudentsNotesDTO studentsNotesDTO = new StudentsNotesDTO();
                studentsNotesDTO.setNoteDTO(noteDTOList);
                studentsNotesDTO.setMoyenGeneral(myG.getMoyenGenerale());
                studentsNotesDTO.setNom(inscrit.getIdEtudiant().getNom());
                studentsNotesDTO.setPrenom(inscrit.getIdEtudiant().getPrenom());
                studentsNotesDTO.setDate_naissance(inscrit.getIdEtudiant().getDateNaissance());
                studentsNotesDTO.setLieuNaissance(inscrit.getIdEtudiant().getLieuNaissance());

                System.out.println("Ajout de l'étudiant: " + studentsNotesDTO.getNom() + " " + studentsNotesDTO.getPrenom());

                studentsNotesDTOList.add(studentsNotesDTO);

                System.out.println("Étudiant avec idInscription = " + idInscription + " a le bon nombre de notes.");
            } else {
                // Gérer le cas où l'inscription est absente si nécessaire
                throw new NoteFundException("Inscription non trouver");
            }
        });

        // Pagination de la liste
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageSize, studentsNotesDTOList.size());
        List<StudentsNotesDTO> paginatedList = studentsNotesDTOList.subList(start, end);

        // Debug : Afficher la taille de la sous-liste paginée
        System.out.println("Taille de la sous-liste paginée: " + paginatedList.size());
        System.out.println("Plage d'index: " + start + " à " + end);
        return new PageImpl<>(paginatedList, pageable, studentsNotesDTOList.size());
    }


    //methode pour appler les notes par id du module
    public List<Notes> getNotesByIdModule(long idModule){
        Semestres currentSemestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdModuleId(currentSemestre.getId(), idModule);
        if(!notesList.isEmpty()){
            return notesList;
        }
        return new ArrayList<>();
    }


    //get ids of students have note for all modules
    public List<Long> getIdsOfStudents(long idSemestre, long idNivFiliere, long idClasse) {
        List<Modules> modulesList = modules_repositories.allModulesOfClassBySemestre(idSemestre, idNivFiliere);
        List<Notes> notes = notes_repositorie.getByIdSemestreIdAndIdClasseId(idSemestre, idClasse);

        System.out.println("----------modules size--------------" + modulesList.size());

        // Grouper les inscriptions par idEtudiant
        Map<Long, List<Notes>> inscriptionsGroupedByStudent = notes.stream()
                .collect(Collectors.groupingBy(note -> note.getIdInscription().getId()));

        // Filtrer pour ne garder que les étudiants ayant un nombre de notes égal au nombre de modules

        return inscriptionsGroupedByStudent.entrySet().stream()
                .filter(entry -> entry.getValue().size() == modulesList.size())
                .map(Map.Entry::getKey)
                .toList();
    }
    //calculate note for student
    @Transactional
    public Object moyenOfStudent(long idStudent, long idSemestre) {
    // Vérification de l'existence de l'étudiant
    Inscription studentExist = inscription_repositorie.findByIdEtudiantIdEtudiant(idStudent);
    if (studentExist == null) {
        throw new RuntimeException("L'étudiant n'existe pas");
    }
    Semestres semestreExist = semestre_repositorie.findById(idSemestre);
    if(semestreExist == null){
        throw new NoteFundException("Le semestre est introuvable");
    }

    List<Long> studentsIdsHaveNotesForAllModules = getIdsOfStudents(idSemestre, studentExist.getIdClasse().getIdFiliere().getId(), studentExist.getIdClasse().getId());
    if(!studentsIdsHaveNotesForAllModules.contains(idStudent)){
        throw new NoteFundException("Impossible de calculer la moyenne l'étudiant ne possède pas des notes pour tous les modules");
    }
    // Récupération des ClasseModule associées
    List<ClasseModule> classeWithModule = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(
            studentExist.getIdClasse().getIdFiliere().getId(), idSemestre);

    // Récupération des notes associées à l'étudiant
    List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);

    List<NoteDTO> noteDTOList = new ArrayList<>();

    // Parcours des ClasseModule (qui sont associées aux UEs)
    for (ClasseModule classeModule : classeWithModule) {
        UE ue = classeModule.getIdUE(); // On récupère l'UE associée

        NoteDTO nDTO = new NoteDTO();
        nDTO.setIdUe(ue.getId());
        nDTO.setNomUE(ue.getNomUE());

        // Récupération des modules associés à l'UE
        List<Modules> modulesList = modules_repositories.findByIdUeId(ue.getId());
        List<NoteModuleDTO> noteModuleDTOList = new ArrayList<>();

        int totalCoefficients = 0;
        double totalSumNoteModule = 0;
        int CoefCuntNumber = 0;

        // Pour chaque module, on cherche la note correspondante
        for (Modules module : modulesList) {
            CoefCuntNumber++;
            Notes moduleNote = notesList.stream()
                    .filter(note -> note.getIdModule().equals(module))
                    .findFirst()
                    .orElse(null);

            // Si le module a une note, on l'ajoute au calcul
            if (moduleNote != null) {
                // Ajout à la somme pondérée des notes
                totalSumNoteModule += moduleNote.getNoteModule();
                totalCoefficients += module.getCoefficient();

                NoteModuleDTO noteModuleDTO = new NoteModuleDTO();
                noteModuleDTO.setNomModule(module.getNomModule());
                noteModuleDTO.setIdModule(module.getId());
                noteModuleDTO.setIdUe(ue.getId());
                noteModuleDTO.setCoefficient(module.getCoefficient());
                noteModuleDTO.setNoteModule( moduleNote.getNoteModule()); // Ajout de la note au DTO

                // Ajout du module au DTO uniquement s'il a une note
                noteModuleDTOList.add(noteModuleDTO);

            }
        }

        // Si des modules avec des notes sont trouvés, on calcule la note de l'UE
        if (!noteModuleDTOList.isEmpty()) {
            nDTO.setModules(noteModuleDTOList);
            nDTO.setCoefficientUe(totalCoefficients);
            double noteUe = (totalSumNoteModule / CoefCuntNumber);
            noteUe = Math.round(noteUe * 100.0) / 100.0;
            double noteUeCoef = noteUe * totalCoefficients;
            nDTO.setNoteUeCoefficient(noteUeCoef);
            nDTO.setNoteUE(noteUe);
        } else {
            // Si aucun module avec des notes n'a été trouvé, on peut décider de ne pas ajouter cet UE
            continue;
        }

        // Ajouter le DTO de l'UE à la liste des résultats
        noteDTOList.add(nDTO);

    }

    System.out.println("--------------ici je suis----------------------");
   return calculateMoyen(noteDTOList, studentExist, semestreExist); // Retourner la liste de tous les NoteDTO
}

    // Méthode pour calculer la moyenne générale
    public Object calculateMoyen(List<NoteDTO> dtos, Inscription inscription, Semestres semestre) {
        if (dtos.isEmpty()) {
            throw new NoteFundException("Aucune note trouvée pour ce calcul");
        }

        // Initialisation des variables pour le calcul
        double totalMoyenUe = 0.0;
        int totalCoefficients = 0;

        // Parcours des UEs pour accumuler les données nécessaires
        for (NoteDTO dto : dtos) {
            totalMoyenUe += dto.getNoteUeCoefficient(); // Somme pondérée des notes
            totalCoefficients += dto.getCoefficientUe();// Somme des coefficients

        }

        // Vérification des coefficients
        if (totalCoefficients == 0) {
            throw new RuntimeException("Les coefficients sont manquants, impossible de calculer la moyenne générale.");
        }



        // Calcul de la moyenne générale
        double moyenneGenerale = totalMoyenUe / totalCoefficients;
        moyenneGenerale = Math.round(moyenneGenerale * 100.0) / 100.0;// Arrondi à 2 décimales

        Moyenne myExist = moyenne_repositorie.findByIdSemestreIdAndIdInscriptionId(semestre.getId(), inscription.getId());
        if (myExist == null) {
            // Création de l'objet Moyenne
            Moyenne moyenne = new Moyenne();
            moyenne.setMoyenGenerale(moyenneGenerale);
            moyenne.setIdSemestre(semestre);
            moyenne.setIdInscription(inscription);

            // Sauvegarde dans le repository
            moyenne_repositorie.save(moyenne);
        }else {
            myExist.setMoyenGenerale(moyenneGenerale);
            moyenne_repositorie.save(myExist);
        }

        return DTO_response_string.fromMessage("Calcule effectué avec succée", 200);
    }

    //get note of student for relever
    public List<GetNoteDTO> getNotesByIdStudentAndIdSemestre(long idStudent, long idSemestre) {
        // Vérification de l'existence de l'étudiant
        Inscription studentExist = inscription_repositorie.findByIdEtudiantIdEtudiant(idStudent);
        if (studentExist == null) {
            throw new RuntimeException("L'étudiant n'existe pas");
        }

        Semestres semestreExist = semestre_repositorie.findById(idSemestre);
        if (semestreExist == null) {
            throw new NoteFundException("Le semestre est introuvable");
        }

        Moyenne myG = moyenne_repositorie.findByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);

        List<Long> studentIdsHaveNotesForAllModules = getIdsOfStudents(idSemestre, studentExist.getIdClasse().getIdFiliere().getId(), studentExist.getIdClasse().getId());
        if ( !studentIdsHaveNotesForAllModules.contains(idStudent) || myG == null ) {
            System.out.println("-------------------------" + idStudent);
            return new ArrayList<>();
        }

        List<Notes> notes = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);
        if (notes == null || notes.isEmpty()) {
            throw new NoteFundException("Aucune note trouvée");
        }



        List<GetNoteDTO> result = new ArrayList<>();

        // Récupération des modules ayant des notes
        List<Modules> modulesList = modules_repositories.allModuleWithNote(idStudent, idSemestre);

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
            dto.setMoyenGeneral(myG.getMoyenGenerale());

            result.add(dto);
        }

        return result;
    }


}
