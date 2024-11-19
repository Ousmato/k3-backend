package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.AddNoteDTO;
import Gestion_scolaire.Dto_classe.AddUeDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Ue_service {

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Validator validator;
    @Autowired
    private Classe_repositorie classe_repositorie;



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
            List<ClasseModule> list  = classeModule_repositorie.getClasseModuleByIdUEId(uEexist.getId());
            for (ClasseModule cm : list) {
                if(cm.getIdSemestre().equals(dto.getSemestre())){
                    throw new NoteFundException("Ce nom existe déjà: " + uEexist.getNomUE());

                }
            }
        }

        if(dto.getModules().isEmpty()){
            throw new NoteFundException("Veuillez ajouter au moins un module et son coefficient");
        }
        LocalDate dateDebutSemestre = dto.getSemestre().getDateDebut();
        LocalDate dateFinSemestre = dto.getSemestre().getDatFin();
        LocalDate dateDebutAnne = classe.getIdAnneeScolaire().getDebutAnnee();
        LocalDate dateFinAnne = classe.getIdAnneeScolaire().getFinAnnee();

//        System.out.println("-------------debut s----------" + dateDebutSemestre);
//        System.out.println("--------- fin s---------------" + dateFinSemestre);
//        System.out.println("------------debut a------------" + dateDebutAnne);
//        System.out.println("-------------fin a-----------" + dateFinAnne);
        if (dateDebutSemestre.isBefore(dateDebutAnne) || dateFinSemestre.isAfter(dateFinAnne)) {
            throw new NoteFundException("La date de fin : " +  dateFinSemestre + " du semestre  \n doit être dans l'intervalle de l'année scolaire \n date de fin d'année " + dateFinAnne);
        }

        UE ueSaved = ue_repositorie.save(dto.getIdUe());
        ClasseModule classeModule = new ClasseModule();
        classeModule.setIdUE(ueSaved);
        classeModule.setIdSemestre(dto.getSemestre());
        classeModule.setIdNiveauFiliere(classe.getIdFiliere());
        classeModule_repositorie.save(classeModule);

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

        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
    }

//    -------------------------------------------
    public List<UE> readAllAssociated(long idClasse){
        List<UE> list = ue_repositorie.findAll();
        List<UE> ueNewList = new ArrayList<>();

        for (UE ue : list) {
            ClasseModule cm = classeModule_repositorie.findByIdNiveauFiliereIdAndIdUEId(idClasse, ue.getId());
            if (cm != null){
                ueNewList.add(ue);
            }
        }
        return ueNewList;
    }

//    ------------------------methode get all modules in classe-----------------------
    public  List<Modules> listModule(long idClass){
        List<UE> list = readAllAssociated(idClass);
        System.out.println("----------list ue-----" + list);
        List<Modules> modulesList = new ArrayList<>();
        for (UE ue : list){
         List<Modules> modulesForUe = modules_repositories.findByIdUeId(ue.getId());
            if (modulesForUe != null) {
                modulesList.addAll(modulesForUe);
            }
          System.out.println("--------list module-------" + modulesList);
        }
        return modulesList;
    }
//    -----------------------------------------------method get all module without note
    public  List<Modules> listModule_without_note(long idClass){
        List<UE> list = readAllAssociated(idClass);
        List<Modules> modulesList = new ArrayList<>();
        Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());

        for (UE ue : list) {
            List<Modules> modulesForUe = modules_repositories.findByIdUeId(ue.getId());
            List<Notes> notesList = notes_repositorie.findByIdSemestreId(semestre.getId());
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
                        modulesList.add(modules);
                    }
                }
            }
            System.out.println("--------list module-------" + modulesList);
        }

        return modulesList;
    }
//liste des tous les modules sans note pour modifier dans le parameter
public  List<Modules> listModule_without_note_all(){
    List<UE> list = ue_repositorie.findAll();
    List<Modules> modulesListExist = new ArrayList<>();
    Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());

    for (UE ue : list) {
        List<Modules> modulesForUe = modules_repositories.findByIdUeId(ue.getId());
        List<Notes> notesList = notes_repositorie.findByIdSemestreId(semestre.getId());
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
    public  List<Modules> getByIdStudentAndIdClasse(long idStudent, long idClasse, long idSemestre){
       List<Modules> modulesList = new ArrayList<>();
       List<UE> ueList = new ArrayList<>();
        List<ClasseModule> list = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(idClasse, idSemestre);
        for (ClasseModule classeModule : list) {
            ueList.add(classeModule.getIdUE());
        }
        for (UE ue : ueList) {
           List<Modules> modules = modules_repositories.findByIdUeId(ue.getId());
           modulesList.addAll(modules);
        }

//        Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idStudent);

        System.out.println("------note----------------------------" + notesList);

        for (Notes notes : notesList) {
            // Filtrer les modules avec note pour cet étudiant et les retirer de la liste sans note
            modulesList.removeIf(module -> module.getId() == notes.getIdModule().getId());
        }
    if(modulesList.isEmpty()){
        return new ArrayList<>();
    }
    return modulesList;
    }

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
                    modules_repositories.save(modExist);
                }else {
                    throw new NoteFundException("Le module " + modules.getNomModule() + " n'existe pas");
                }
            }
            return DTO_response_string.fromMessage("Mise à jour effectué avec succè", 200);
        }else {
            throw new RuntimeException("UE n'existe pas");
        }
    }
//--------------------------method get all ue-------------------------
    public List<UE> getListUe(){
        return ue_repositorie.findAll();
    }
//    ----------------------------liste des ues dont les modules n'ont pas de note-----------
    public List<UE> ueList_without_note_all(){
        List<UE> list = ue_repositorie.findAll();
        List<UE> newListUes = new ArrayList<>();
        List<Modules> modulesList = listModule_without_note_all();
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

        // Supprimer la ligne de ClasseModule correspondant à l'UE
        classeModule_repositorie.deleteByUeId(ueExist.getId());

        // Supprimer les modules associés
        List<Modules> modulesList = modules_repositories.findByIdUeId(ueExist.getId());
        if (modulesList != null && !modulesList.isEmpty()) {
            modules_repositories.deleteAll(modulesList);
        }
        // Supprimer l'UE
        ue_repositorie.delete(ueExist);
        return DTO_response_string.fromMessage("Suppression effectuée avec succès", 200);
    }

    //method delete module
    public Object delete_module_by_id(long idModule) {
        Modules modules = modules_repositories.findById(idModule);
        if (modules == null) {
            throw new  NoteFundException("Module doesn't exist");

        }
        modules_repositories.delete(modules);
        return DTO_response_string.fromMessage("Suppression effectue avec success", 200);
    }

    //    -------------------------------method liste des ues dont les modules n'ont pas de notes associe
    public List<UE> getAllUe_without_modules() {
        List<UE> list = ue_repositorie.findAll();
        List<UE> newListUes = new ArrayList<>();

        for (UE ue : list) {
            boolean hasModuleWithoutNotes = false;
            List<Modules> modulesForUe = modules_repositories.findByIdUeId(ue.getId());
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
    public List<UE> getAll_ue_without_modules_and_classes() {
        // Obtenir toutes les UEs
        List<UE> allUes = ue_repositorie.findAll();
        List<UE> ues_without_modules_and_classes = new ArrayList<>();

        for (UE ue : allUes) {
            // Vérifier si l'UE n'a pas de modules
            List<Modules> modulesForUe = modules_repositories.findByIdUeId(ue.getId());
            boolean hasNoModules = modulesForUe.isEmpty();

            // Vérifier si l'UE n'a pas de classes
            List<ClasseModule> classeModulesForUe = classeModule_repositorie.getClasseModuleByIdUEId(ue.getId());
            boolean hasNoClasses = classeModulesForUe.isEmpty();

            // Ajouter à la liste si l'UE n'a ni modules ni classes
            if (hasNoModules && hasNoClasses) {
                ues_without_modules_and_classes.add(ue);
            }
        }

        return ues_without_modules_and_classes;
    }

    //get

    public List<AddNoteDTO> getNotesForStudentInModules(long idNiveauFiliere, long idSemestre, long idEtudiant) {
        // Récupération de la classe de l'étudiant à partir de l'idNiveauFiliere
        StudentsClasse classe = classe_repositorie.findStudentsClasseByIdFiliereId(idNiveauFiliere);
        if (classe == null) {
            throw new NoteFundException("Classe non trouvée pour le niveau filière : " + idNiveauFiliere);
        }

        // Récupérer la liste des modules liés à cette classe pour ce semestre
        List<ClasseModule> classeModuleList = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(idNiveauFiliere, idSemestre);

        // Créer la liste pour stocker les résultats
        List<AddNoteDTO> addNoteDTOList = new ArrayList<>();

        // Récupérer les modules qui ont des emplois planifiés avant la date actuelle
        List<Modules> modulesWithEmplois = modules_repositories.findModulesWithEmplois(
                classe.getId(), idSemestre, LocalDate.now().minusDays(7));

        // Utilisation d'un Set pour éviter les doublons
        Set<Long> moduleIdsSeen = new HashSet<>();

        // Boucle sur chaque module de la classe pour récupérer les notes de l'étudiant
        for (ClasseModule classeModule : classeModuleList) {
            AddUeDTO dto = AddUeDTO.getAddUeDTO(classeModule); // On crée le DTO pour cette classe/module
            dto.setModules(modulesWithEmplois);

            // Boucle sur les modules récupérés
            for (Modules module : modulesWithEmplois) {
                // Si ce module a déjà été traité, on passe au suivant
                if (moduleIdsSeen.contains(module.getId())) {
                    continue;
                }
                moduleIdsSeen.add(module.getId());

                // On crée un DTO pour la note
                AddNoteDTO noteDTO = new AddNoteDTO();

                // Recherche de la note de l'étudiant pour ce module
                Notes note = notes_repositorie.findByIdSemestreIdAndIdModuleIdAndIdInscriptionId(idSemestre, module.getId(), idEtudiant);
                if (note != null) {
                    // Si la note existe, on la récupère
                    noteDTO.setIdNote(note.getId());
                    noteDTO.setIdModule(note.getIdModule().getId());
                    noteDTO.setNoteClasse(note.getClasseNote());
                    noteDTO.setNoteExam(note.getExamNote());
                } else {
                    // Si aucune note n'est trouvée, on initialise à 0
                    noteDTO.setIdModule(module.getId());
                    noteDTO.setNoteClasse(0.0);
                    noteDTO.setNoteExam(0.0);
                }

                // On associe le DTO du module (AddUeDTO) à chaque DTO de note
                noteDTO.setAddUeDto(dto);

                // Ajout du DTO de note à la liste
                addNoteDTOList.add(noteDTO);
            }
        }

        return addNoteDTOList;
    }


    public List<AddUeDTO> allUesByIdClasseAndIdSemestre(long idNiveauFiliere, long idSemestre) {
        List<ClasseModule> classeModuleList =  classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(idNiveauFiliere, idSemestre);
        List<AddUeDTO> addUeDTOList = new ArrayList<>();
        for (ClasseModule classeModule : classeModuleList) {


            List<Modules> modulesForUe = modules_repositories.findByIdUeId(classeModule.getIdUE().getId());
            AddUeDTO dto = AddUeDTO.getAddUeDTO(classeModule);
            dto.setModules(modulesForUe);
            addUeDTOList.add(dto);
        }
        return addUeDTOList;
    }


}
