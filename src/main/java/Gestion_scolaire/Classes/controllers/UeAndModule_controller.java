package Gestion_scolaire.Classes.controllers;

import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.Dto_classe.GetInputNoteInscritDTO;
import Gestion_scolaire.Classes.dtos.AddUeDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Classes.services.Ue_service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api-class")
public class UeAndModule_controller {
    @Autowired
    private SharedControllers sharedControllers;

    @PostMapping("/add-ue")
    public Object addUe(@RequestBody AddUeDTO dto){
        return sharedControllers.getUeService().add(dto);
    }
    //method get list all eu----------------------


    @GetMapping("/list-ue-idClasse-idSemestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer la liste des ues de la classe")
    public List<AddUeDTO> getAllUe(@PathVariable long idClasse, @PathVariable long idSemestre){
        return sharedControllers.getUeService().allUesByIdClasseAndIdSemestre(idClasse, idSemestre);
    }
//    -----------------------------------method get list module -----------
    @GetMapping("/list-module")
    public List<Modules> getAllModule(){
        return sharedControllers.getModulesService().readAll();
    }
//    --------------------------methode get all module in class-----------------------
//    @GetMapping("/all-module/{idClasse}")
//    public List<Modules> getListModule(@PathVariable long idClasse){
//        return sharedControllers.getUeService().listModule(idClasse);
//    }

    @GetMapping("/all-module-by-classe-semestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer les module du classe d'une semestre")
    public List<Modules> getModuleByClassAndSemestre(@PathVariable long idClasse, @PathVariable long idSemestre){
        return sharedControllers.getModulesService().getModulesByIdclasseAndIdSemestre(idClasse, idSemestre);
    }
    //methode get list ue
    @GetMapping("/all-ue")
    public List<UE> getListUe(){
        return sharedControllers.getUeService().getListUe();
    }
//    ----------------------------------------method get all module of classe without note
//    @GetMapping("/all-module-without-note/{idClasse}/{idSemestre}")
//    public List<Modules> allModule_without_note(@PathVariable long idClasse, @PathVariable long idSemestre){
//        return sharedControllers.getUeService().listModule_without_note(idClasse, idSemestre);
//    }
    //---------------------------------------------tout les modules sans notes
    @GetMapping("/all-module-without-note_all/{idSemestre}")
    public List<Modules> allModule_without_note_all(@PathVariable long idSemestre){
    return sharedControllers.getUeService().listModule_without_note_all(idSemestre);
}

//    ----------------------------------------method modifier un ue
    @PutMapping("/update-ue")
    public Object update_ue(@RequestBody AddUeDTO ue){
       return sharedControllers.getUeService().update(ue);
    }

//    ------------------------------methode get all ue by idUe
    @GetMapping("/list-by-idUe/{idUe}")
    public List<Modules> modulesByUe(@PathVariable long idUe){
        return sharedControllers.getModulesService().readByUe(idUe);
    }

//------------------------------------------------------method update module
    @PostMapping("/add-module")
    public Object addModule(@RequestBody Modules modules){
       return sharedControllers.getModulesService().addModule(modules);
    }
//    ----------------------method pour appeler tous les modules
    @GetMapping("/list-ues-without-modules-notes/{idSemestre}")
    public List<UE> getListUesWithoutModulesNotes(@PathVariable long idSemestre){
        return sharedControllers.getUeService().ueList_without_note_all(idSemestre);
    }
//    ---------------------------method delete ue
    @DeleteMapping("/delete-ue-by-id/{idUe}")
    public Object delete(@PathVariable long idUe){
        return sharedControllers.getUeService().deleteUe_by_id(idUe);
    }

    //    ----------------------method pour appeler tous les ues sans classe ni modules
//    @GetMapping("/all-ues-without-modules-and-classe")
//    public List<UE> all_ue_null_associate(){
//        return sharedControllers.getUeService().getAll_ue_without_modules_and_classes();
//    }
//    -------------------------method delete module
    @DeleteMapping("/delete-module-by-id/{idModule}")
    public Object delete_module(@PathVariable long idModule){
        return sharedControllers.getModulesService().deleteById(idModule);
    }

    @GetMapping("/all-niv-filiere")
    @Operation(summary = "Recupere les niveau et filiere associer (mention)")
    public List<NiveauFilieres> getAllNivFiliere(){
        return sharedControllers.getClasseService().getAllNiveauFilieres();
    }

    //get all modules without emploi
    @GetMapping("/all-modules-without-emploi/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer tous les modules non programmer pour le semestre et la classe")
    public List<GetInputNoteInscritDTO> getModulesWithoutEmploi(@PathVariable long idClasse, @PathVariable long idSemestre){
        return sharedControllers.getModulesService().getModulesWithoutEmploi(idClasse, idSemestre);
    }
}
