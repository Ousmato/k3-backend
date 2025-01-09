package Gestion_scolaire.Classes.controllers;

import Gestion_scolaire.Classes.services.Methods_shared;
import Gestion_scolaire.Dto_classe.GetInputNoteInscritDTO;
import Gestion_scolaire.Classes.dtos.AddUeDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Services.Modules_service;
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
    private Ue_service ue_service;

    @Autowired
    private Methods_shared methods_shared;

    @Autowired
    private Classe_service classe_service;

    @PostMapping("/add-ue")
    public Object addUe(@RequestBody AddUeDTO dto){
        return ue_service.add(dto);
    }
    //method get list all eu----------------------


    @GetMapping("/list-ue-idClasse-idSemestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer la liste des ues de la classe")
    public List<AddUeDTO> getAllUe(@PathVariable long idClasse, @PathVariable long idSemestre){
        return ue_service.allUesByIdClasseAndIdSemestre(idClasse, idSemestre);
    }
//    -----------------------------------method get list module -----------
    @GetMapping("/list-module")
    public List<Modules> getAllModule(){
        return methods_shared.getModules_service().readAll();
    }
//    --------------------------methode get all module in class-----------------------
    @GetMapping("/all-module/{idClasse}")
    public List<Modules> getListModule(@PathVariable long idClasse){
        return ue_service.listModule(idClasse);
    }

    @GetMapping("/all-module-by-classe-semestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer les module du classe d'une semestre")
    public List<Modules> getModuleByClassAndSemestre(@PathVariable long idClasse, @PathVariable long idSemestre){
        return methods_shared.getModules_service().getModulesByIdclasseAndIdSemestre(idClasse, idSemestre);
    }
    //methode get list ue
    @GetMapping("/all-ue")
    public List<UE> getListUe(){
        return ue_service.getListUe();
    }
//    ----------------------------------------method get all module of classe without note
    @GetMapping("/all-module-without-note/{idClasse}/{idSemestre}")
    public List<Modules> allModule_without_note(@PathVariable long idClasse, @PathVariable long idSemestre){
        return ue_service.listModule_without_note(idClasse, idSemestre);
    }
    //---------------------------------------------tout les modules sans notes
    @GetMapping("/all-module-without-note_all/{idSemestre}")
    public List<Modules> allModule_without_note_all(@PathVariable long idSemestre){
    return ue_service.listModule_without_note_all(idSemestre);
}

//    ----------------------------------------method modifier un ue
    @PutMapping("/update-ue")
    public Object update_ue(@RequestBody AddUeDTO ue){
       return ue_service.update(ue);
    }

//    ------------------------------methode get all ue by idUe
    @GetMapping("/list-by-idUe/{idUe}")
    public List<Modules> modulesByUe(@PathVariable long idUe){
        return methods_shared.getModules_service().readByUe(idUe);
    }

//------------------------------------------------------method update module
    @PutMapping("/update-module")
    public Object update(@RequestBody Modules modules){
       return methods_shared.getModules_service().update(modules);
    }
//    ----------------------method pour appeler tous les modules
    @GetMapping("/list-ues-without-modules-notes/{idSemestre}")
    public List<UE> getListUesWithoutModulesNotes(@PathVariable long idSemestre){
        return ue_service.ueList_without_note_all(idSemestre);
    }
//    ---------------------------method delete ue
    @DeleteMapping("/delete-ue-by-id/{idUe}")
    public Object delete(@PathVariable long idUe){
        return ue_service.deleteUe_by_id(idUe);
    }

    //    ----------------------method pour appeler tous les ues sans classe ni modules
    @GetMapping("/all-ues-without-modules-and-classe")
    public List<UE> all_ue_null_associate(){
        return ue_service.getAll_ue_without_modules_and_classes();
    }
//    -------------------------method delete module
    @DeleteMapping("/delete-module-by-id/{idModule}")
    public Object delete_module(@PathVariable long idModule){
        return ue_service.delete_module_by_id(idModule);
    }

    @GetMapping("/all-niv-filiere")
    @Operation(summary = "Recupere les niveau et filiere associer (mention)")
    public List<NiveauFilieres> getAllNivFiliere(){
        return classe_service.getAllNiveauFilieres();
    }

    //get all modules without emploi
    @GetMapping("/all-modules-without-emploi/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer tous les modules non programmer pour le semestre et la classe")
    public List<GetInputNoteInscritDTO> getModulesWithoutEmploi(@PathVariable long idClasse, @PathVariable long idSemestre){
        return methods_shared.getModules_service().getModulesWithoutEmploi(idClasse, idSemestre);
    }
}
