package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Dto_classe.GetInputNoteInscritDTO;
import Gestion_scolaire.Classes.dtos.AddUeDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentSession;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Sessions_repositorie;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class Ue_service {

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Validator validator;
    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private Shared_methods_service shared_methods_service;

    @Autowired
    private Sessions_repositorie sessions_repositorie;



    @Transactional
    public Object add(AddUeDTO dto) {
        StudentsClasse classe = classe_repositorie.findById(dto.getIdClasse());
        if (classe == null) {
            throw new NoteFundException("La classe n'existe pas");
        }

        UE uEexist = ue_repositorie.findByNomUE(dto.getIdUe().getNomUE());
        if (uEexist != null) {
            Set<ConstraintViolation<UE>> violation = validator.validate(dto.getIdUe());
            if (!violation.isEmpty()) {
                throw new ConstraintViolationException(violation);
            }

        }

        if(dto.getModules().isEmpty()){
            throw new NoteFundException("Veuillez ajouter au moins un module et son coefficient");
        }
        UE ueSaved = ue_repositorie.save(dto.getIdUe());


        boolean hasModule = false;
        for (Modules module : dto.getModules()) {
            Set<ConstraintViolation<Modules>> violation = validator.validate(module);
            if (!violation.isEmpty()) {
                throw new ConstraintViolationException(violation);
            }
            Modules modExist = modules_repositories.findByIdUeAndNomModule(ueSaved, module.getNomModule());
            if (modExist != null) {
                hasModule = true;
                break; // Exit loop if duplicate is found
            }
        }

        if (hasModule) {
            throw new NoteFundException("Le module existe déjà");
        }
        for (Modules module : dto.getModules()) {
            Modules mod = new Modules();
            mod.setCoefficient(module.getCoefficient());
            mod.setNomModule(module.getNomModule());
            mod.setIdUe(ueSaved);
            modules_repositories.save(mod);
        }

        return DTO_response_string.addMessage();
    }

////    -------------------------------------------
//    public List<UE> readAllAssociated(long idClasse){
//        List<UE> list = ue_repositorie.findAll();
//        List<UE> ueNewList = new ArrayList<>();
//
//        for (UE ue : list) {
//            ClasseModule cm = classeModule_repositorie.findByIdNiveauFiliereIdAndIdUEId(idClasse, ue.getId());
//            if (cm != null){
//                ueNewList.add(ue);
//            }
//        }
//        return ueNewList;
//    }

//    ------------------------methode get all modules in classe-----------------------
//    public  List<Modules> listModule(long idClass){
//        List<UE> list = readAllAssociated(idClass);
//        System.out.println("----------list ue-----" + list);
//        List<Modules> modulesList = new ArrayList<>();
//        for (UE ue : list){
//         List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
//            if (modulesForUe != null) {
//                modulesList.addAll(modulesForUe);
//            }
//          System.out.println("--------list module-------" + modulesList);
//        }
//        return modulesList;
//    }
//    -----------------------------------------------method get all module without note
//    public  List<Modules> listModule_without_note(long idClass, long idSemestre){
//        List<UE> list = readAllAssociated(idClass);
//        List<Modules> modulesList = new ArrayList<>();
////        Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
//
//        for (UE ue : list) {
//            List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
//            List<Notes> notesList = notes_repositorie.findByIdSemestreId(idSemestre);
//            if (modulesForUe != null) {
//                for (Modules modules: modulesForUe){
//                    boolean hasNote = false;
//                    for (Notes note : notesList){
//                        if(note.getIdModule().equals(modules)){
//                            hasNote = true;
//                            break;
//                        }
//                    }
//                    if (!hasNote) {
//                        modulesList.add(modules);
//                    }
//                }
//            }
//            System.out.println("--------list module-------" + modulesList);
//        }
//
//        return modulesList;
//    }
//liste des tous les modules sans note pour modifier dans le parameter
public  List<Modules> listModule_without_note_all(long idSemestre){
    List<UE> list = ue_repositorie.findAll();
    List<Modules> modulesListExist = new ArrayList<>();
//    Semestres semestre = semestre_repositorie.findById(idSemestre);

    for (UE ue : list) {
        List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
        List<Notes> notesList = notes_repositorie.findByIdSemestreId(idSemestre);
        if (modulesForUe != null) {
            for (Modules modules: modulesForUe){
                boolean hasNote = false;
                for (Notes note : notesList){
                    if(note.getIdModule().equals(modules)){
                        hasNote = true;
                        break;
                    }
                }
                if (!hasNote) {
                    modulesListExist.add(modules);
                }
            }
        }
        System.out.println("--------list module-------" + modulesListExist);
    }

    return modulesListExist;
}

    //    --------------------------get module of student without note for a student
//    public  List<Modules> getByIdStudentAndIdClasse(long idStudent, long idClasse, long idSemestre){
//       List<Modules> modulesList = new ArrayList<>();
//       List<UE> ueList = new ArrayList<>();
//        List<ClasseModule> list = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(idClasse, idSemestre);
//        for (ClasseModule classeModule : list) {
//            ueList.add(classeModule.getIdUE());
//        }
//        for (UE ue : ueList) {
//           List<Modules> modules = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
//           modulesList.addAll(modules);
//        }
//
////        Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
//        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);
//
//        System.out.println("------note----------------------------" + notesList);
//
//        for (Notes notes : notesList) {
//            // Filtrer les modules avec note pour cet étudiant et les retirer de la liste sans note
//            modulesList.removeIf(module -> module.getId() == notes.getIdModule().getId());
//        }
//    if(modulesList.isEmpty()){
//        return new ArrayList<>();
//    }
//    return modulesList;
//    }

    //method pour modifier l'ue and this modules
    @Transactional
    public Object update(AddUeDTO dto){
        UE ueExist = ue_repositorie.findById(dto.getIdUe().getId());
        if (ueExist != null){
            ueExist.setNomUE(dto.getIdUe().getNomUE());
           UE ueSaved =  ue_repositorie.save(ueExist);
            for(Modules modules: dto.getModules()){

                Modules modExist = modules_repositories.findById(modules.getId());
                if(modExist != null){
                    modExist.setIdUe(ueSaved);
                    modExist.setCoefficient(modules.getCoefficient());
                    modExist.setNomModule(modules.getNomModule());
                    modExist.setDescription(modules.getDescription());
                    modules_repositories.save(modExist);
                }else {
                    throw new NoteFundException("Le module " + modules.getNomModule() + " n'existe pas");
                }
            }
            return DTO_response_string.fromMessage("Mise à jour effectué avec succè");
        }else {
            throw new RuntimeException("UE n'existe pas");
        }
    }
//--------------------------method get all ue-------------------------
    public List<UE> getListUe(){
        return ue_repositorie.findAll();
    }
//    ----------------------------liste des ues dont les modules n'ont pas de note-----------
    public List<UE> ueList_without_note_all(long idSemestre){
        List<UE> list = ue_repositorie.findAll();
        List<UE> newListUes = new ArrayList<>();
        List<Modules> modulesList = listModule_without_note_all(idSemestre);
        for (UE ue : list) {
            boolean hasNote = true;
            for (Modules modules : modulesList) {

                if(modules.getIdUe().equals(ue)){
                    hasNote = false;
                    break;
                }

            }
            if (hasNote) {
                newListUes.add(ue); // Ajouter cette UE à la liste des UE sans note
            }
        }
        return newListUes;
    }

    //methode to delete ue
    @Transactional
    public Object deleteUe_by_id(long idUe) {
        UE ueExist = ue_repositorie.findById(idUe);
        if (ueExist == null) {
            throw new NoteFundException("UE n'existe pas");
        }

        // Vérifier si des notes sont associées aux modules de cette UE
        boolean notesExist = notes_repositorie.existsNotesByUeId(ueExist.getId());
        if (notesExist) {
            throw new NoteFundException("Suppression a échoué, il existe des notes associées aux modules");
        }


        // Supprimer les modules associés
        List<Modules> modulesList = modules_repositories.findByIdUeIdAndActive(ueExist.getId(), true);
        if (modulesList != null && !modulesList.isEmpty()) {
            modules_repositories.deleteAll(modulesList);
        }
        // Supprimer l'UE
        ue_repositorie.delete(ueExist);
        return DTO_response_string.fromMessage("Suppression effectuée avec succès");
    }


    //    -------------------------------method liste des ues dont les modules n'ont pas de notes associe
    public List<UE> getAllUe_without_modules() {
        List<UE> list = ue_repositorie.findAll();
        List<UE> newListUes = new ArrayList<>();

        for (UE ue : list) {
            boolean hasModuleWithoutNotes = false;
            List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
            if(modulesForUe.isEmpty()){
                hasModuleWithoutNotes = true;
            }
            if(hasModuleWithoutNotes){
                newListUes.add(ue);
            }
        }

        return newListUes;
    }
//---------------------------------nethode pour appeller tous les ues qui n'ont pas de notes ni des classes
//    public List<UE> getAll_ue_without_modules_and_classes() {
//        // Obtenir toutes les UEs
//        List<UE> allUes = ue_repositorie.findAll();
//        List<UE> ues_without_modules_and_classes = new ArrayList<>();
//
//        for (UE ue : allUes) {
//            // Vérifier si l'UE n'a pas de modules
//            List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
//            boolean hasNoModules = modulesForUe.isEmpty();
//
//            // Vérifier si l'UE n'a pas de classes
//            List<ClasseModule> classeModulesForUe = classeModule_repositorie.getClasseModuleByIdUEId(ue.getId());
//            boolean hasNoClasses = classeModulesForUe.isEmpty();
//
//            // Ajouter à la liste si l'UE n'a ni modules ni classes
//            if (hasNoModules && hasNoClasses) {
//                ues_without_modules_and_classes.add(ue);
//            }
//        }
//
//        return ues_without_modules_and_classes;
//    }

    //get

    public Page<GetInputNoteInscritDTO> getNotesForStudentInModules(long idClasse, long idAnneeScolaire, long idSemestre, long idModule, int page, int size) {
        // Récupération de la classe de l'étudiant à partir de l'idNiveauFiliere
        StudentsClasse classe = classe_repositorie.findById(idClasse);
        if (classe == null) {
            throw new NoteFundException("Classe non trouvée pour le niveau filière : " + idClasse);
        }
        Semestres semestre = semestre_repositorie.findById(idSemestre);
        if (semestre == null) {
            throw  new NoteFundException("Le semestre n'existe pas");
        }
        Modules module = modules_repositories.findById(idModule);
        if (module == null) {
            throw  new NoteFundException("Module n'existe pas");
        }

        Pageable pageable = PageRequest.of(page, size);
        // Récupérer la liste des modules liés à cette classe pour ce semestre
        Page<Inscription> inscriptionPage = inscription_repositorie.findByIdClasseIdAnneeScolaireIdAndIdClasseId(idAnneeScolaire,idClasse, pageable);

        // Créer la liste pour stocker les résultats
        List<GetInputNoteInscritDTO> getInputNoteInscritDTOList = new ArrayList<>();
        String nom = classe.getIdFiliere().getIdNiveau().getNom();
        if(nom.contains("1")){
            nom= "L1";
        }else if(nom.contains("2")){
            nom= "L2";
        }else if(nom.contains("3")){
            nom= "L3";
        }
        // Boucle sur chaque module de la classe pour récupérer les notes de l'étudiant
        for (Inscription inscription : inscriptionPage.getContent()) {

            inscription.getIdEtudiant().setSexe(inscription.getIdEtudiant().getSexe().equalsIgnoreCase("FEMME") ? "F" : "M");

            // On crée un DTO pour la note
            GetInputNoteInscritDTO noteDTO = new GetInputNoteInscritDTO();
            InscriptionNoteDTO inscriptionDTO = InscriptionNoteDTO.toDTO(inscription);
            noteDTO.setAnneeScolaire(classe.getIdAnneeScolaire());
            noteDTO.setSemestre(semestre.getNomSemetre());
            noteDTO.setNomModule(module.getNomModule());
            noteDTO.setNomClasse( shared_methods_service.abrevigateName(classe.getIdFiliere().getIdFiliere().getNomFiliere()) + "-" + nom );

            // Recherche de la note de l'étudiant pour ce module
            Notes note = notes_repositorie.findStudentNoteByModuleAndSemestre(idSemestre, inscription.getId(),  idModule);
            if (module.getIdUe().getNomUE().toLowerCase().contains("lib")) {

                // On associe les inscrits a chaque DTO de note
                if (note != null) {
                    noteDTO.setClasseNote(note.getNoteModule());

                }else {
                    noteDTO.setClasseNote(0);
                }
                noteDTO.setValidate(noteDTO.getClasseNote() >= 10 ? "ACQUIS" : "NON");


            }else {
                double noteUe = shared_methods_service.noteUe(module.getIdUe().getId(),idSemestre,inscription.getId());

                StudentSession session = sessions_repositorie.findByIdInscritIdAndIdSemestreIdAndIdModuleId(inscription.getId(), idSemestre, idModule);
                if (session == null){
                    noteDTO.setSessionNote(0.0);
                    noteDTO.setNbreSession(1);
                }else {
                    noteDTO.setSessionNote(session.getNoteSession());
                    noteDTO.setNbreSession(session.getNbreSession());
                }
                if (note != null) {
                    noteDTO.setIdNote(note.getId());
                    noteDTO.setIdModule(note.getIdModule().getId());
                    noteDTO.setClasseNote(note.getClasseNote());
                    noteDTO.setExamNote(note.getExamNote());
                    noteDTO.setNoteUe(noteUe);
                }else {
                    noteDTO.setIdModule(idModule);
//                    System.out.println("--------------------------"+noteDTO);
                }
                noteDTO.setValidate(noteUe >= 10 ? "AD" : "AJ");

            }
                // On associe les inscrits a chaque DTO de note
            noteDTO.setInscriptions(inscriptionDTO);
            noteDTO.setIdModule(idModule);

                // Ajout du DTO de note à la liste
            getInputNoteInscritDTOList.add(noteDTO);
        }

        return new PageImpl<>(getInputNoteInscritDTOList, pageable, inscriptionPage.getTotalElements());
    }


    public List<AddUeDTO> allUesByIdClasseAndIdSemestre(long idClasse, long idSemestre) {
        List<UE> ueList =  ue_repositorie.findByIdClasse(idClasse, idSemestre);
        List<AddUeDTO> addUeDTOList = new ArrayList<>();
        for (UE ue : ueList) {


            List<Modules> modulesForUe = modules_repositories.findByIdUeIdAndActive(ue.getId(), true);
            //System.out.println("--------------------modules bien passee" + modulesForUe);
            AddUeDTO dto = AddUeDTO.getAddUeDTO(ue);
            dto.setModules(modulesForUe);
            addUeDTOList.add(dto);
        }
        return addUeDTOList;
    }


}
