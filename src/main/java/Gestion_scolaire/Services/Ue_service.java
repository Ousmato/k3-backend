package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.AddUeDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
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
    private  Note_service note_service;



    public Object add(AddUeDTO dto){
        UE uEexist = ue_repositorie.findByNomUE(dto.getIdUe().getNomUE());
        if (uEexist != null){
            throw new RuntimeException("Ce nom existe deja");
        }

        LocalDate dateDebutSemestre = dto.getSemestre().getDateDebut();
        LocalDate dateFinSemestre = dto.getSemestre().getDatFin();
        LocalDate dateDebutAnne = dto.getClasse().getIdAnneeScolaire().getDebutAnnee();
        LocalDate dateFinAnne = dto.getClasse().getIdAnneeScolaire().getFinAnnee();

        if(dateDebutSemestre.isBefore(dateDebutAnne) || dateFinSemestre.isBefore(dateFinAnne)){
            throw new NoteFundException("Le semestre doit etre dans l'intervalle de l'année scolaire");
        }
           UE ueSaved = ue_repositorie.save(dto.getIdUe());
            ClasseModule classeModule = new ClasseModule();
            classeModule.setIdUE(ueSaved);
            classeModule.setIdSemestre(dto.getSemestre());
            classeModule.setIdNiveauFiliere(dto.getClasse().getIdFiliere());
            classeModule_repositorie.save(classeModule);
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);

    }
//    ------------------------------------------------methode pour appeler la liste des ues-------------
    public List<UE> readAll(long idClasse){
        List<UE> list = ue_repositorie.findAll();
        List<UE> ueNewList = new ArrayList<>();

            for (UE ue : list) {
                ClasseModule cm = classeModule_repositorie.findByIdNiveauFiliereIdAndIdUEId(idClasse, ue.getId());
                 if (cm == null){
                        ueNewList.add(ue);
                 }
            }
        return ueNewList;
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
    public  List<Modules> getByIdStudentAndIdClasse(long idStudent, long idClasse){
        List<Modules> listSanNote = listModule(idClasse);

        Semestres semestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdStudentsIdEtudiant(semestre.getId(), idStudent);


        for (Notes notes : notesList) {
            // Filtrer les modules avec note pour cet étudiant et les retirer de la liste sans note
            listSanNote.removeIf(module -> module.getId() == notes.getIdModule().getId());
        }
    if(listSanNote.isEmpty()){
        return new ArrayList<>();
    }
    return listSanNote;
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

}
