package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.AddModuleDTO;
import Gestion_scolaire.Dto_classe.AddUeDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.ModuleDTO;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Ue_service {

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private  Modules_service modules_service;
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
//
//        System.out.println("-------------debut s----------" + dateDebutSemestre);
//        System.out.println("--------- fin s---------------" + dateFinSemestre);
//        System.out.println("------------debut a------------" + dateDebutAnne);
//        System.out.println("-------------fin a-----------" + dateFinAnne);
        if (dateDebutSemestre.isBefore(dateDebutAnne) || dateFinSemestre.isAfter(dateFinAnne)) {
            throw new NoteFundException("Le semestre doit être dans l'intervalle de l'année scolaire");
        }

        UE ueSaved = ue_repositorie.save(dto.getIdUe());
        ClasseModule classeModule = new ClasseModule();
        classeModule.setIdUE(ueSaved);
        classeModule.setIdSemestre(dto.getSemestre());
        classeModule.setIdNiveauFiliere(classe.getIdFiliere());
        classeModule_repositorie.save(classeModule);

        boolean hasModule = false;
        for (Modules module : dto.getModules()) {
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
//----------------------liste des tout les modules sans note pour modifier dans le parameter
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
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdStudentsIdEtudiant(idSemestre, idStudent);

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

//    ---------------------------------method pour modifier l'ue---------------------------
    public Object update(UE ue){
        UE ueExist = ue_repositorie.findById(ue.getId());
        if (ueExist != null){
            ueExist.setNomUE(ue.getNomUE());
            ue_repositorie.save(ueExist);
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
//    --------------------------------methode to delete ue
    public Object deleteUe_by_id(long idUe) {
        UE ueExist = ue_repositorie.findById(idUe);
        if (ueExist == null) {
            return DTO_response_string.fromMessage("Ue doesn't exist", 404);
        }
        List<UE> uesWithout_module = getAllUe_without_modules();
        if (uesWithout_module.contains(ueExist)) {

            ue_repositorie.delete(ueExist);
            return DTO_response_string.fromMessage("Suppression effectuée avec succès", 200);
        } else {
            return DTO_response_string.fromMessage("Suppression a échoué, il existe des modules associés", 400);
        }
    }
//    -------------------------------------method delete module
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

    public List<AddUeDTO> getAllUeByIdNiveauFiliereAndIdSemestre(long idNiveauFiliere, long idSemestre){
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
