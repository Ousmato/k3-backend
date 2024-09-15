package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.AddModuleDTO;
import Gestion_scolaire.Dto_classe.AddUeDTO;
import Gestion_scolaire.Dto_classe.DTO_ClassModule;
import Gestion_scolaire.Dto_classe.ModuleDTO;
import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import Gestion_scolaire.Services.Classe_service;
import Gestion_scolaire.Services.Modules_service;
import Gestion_scolaire.Services.Ue_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api-class")
public class UeAndModule_controller {
    @Autowired
    private Ue_service ue_service;

    @Autowired
    private Modules_service modules_service;

    @Autowired
    private Classe_service classe_service;

    @PostMapping("/add-ue")
    public Object addUe(@RequestBody AddUeDTO dto){
        return ue_service.add(dto);
    }
//    ---------------------------------------methode add modules-------------------------------
    @PostMapping("/add-module-class")
    public Object addClassModule(@RequestBody DTO_ClassModule cm){
        System.out.println(cm + "----------ici");
       return classe_service.add(cm);
    }
//    -----------------------------------------methode add module in module---------------
    @PostMapping("/add-module")
    public Object addModule(@RequestBody AddModuleDTO module){
       return modules_service.addModule(module);
    }
//    --------------------------------------------method get list all eu----------------------
    @GetMapping("/list-ue/{idClasse}")
    public List<UE> getAllUe(@PathVariable long idClasse){
        return ue_service.readAll(idClasse);
    }
//    -----------------------------------method get list module -----------
    @GetMapping("/list-module")
    public List<Modules> getAllModule(){
        return modules_service.readAll();
    }
//    --------------------------methode get all module in class-----------------------
    @GetMapping("/all-module/{idClasse}")
    public List<Modules> getListModule(@PathVariable long idClasse){
        return ue_service.listModule(idClasse);
    }
//    -----------------------------------methode get list ue---------------------
    @GetMapping("/all-ue")
    public List<UE> getListUe(){
        return ue_service.getListUe();
    }
//    ----------------------------------------method get all module of classe without note
    @GetMapping("/all-module-without-note/{idClasse}")
    public List<Modules> allModule_without_note(@PathVariable long idClasse){
        return ue_service.listModule_without_note(idClasse);
    }
//---------------------------------------------tout les modules sans notes
@GetMapping("/all-module-without-note_all")
public List<Modules> allModule_without_note_all(){
    return ue_service.listModule_without_note_all();
}

//    ----------------------------------------method modifier un ue
    @PutMapping("/update-ue")
    public Object update_ue(@RequestBody UE ue){
       return ue_service.update(ue);
    }

//    ------------------------------methode get all ue by idUe
    @GetMapping("/list-by-idUe/{idUe}")
    public List<Modules> modulesByUe(@PathVariable long idUe){
        return modules_service.readByUe(idUe);
    }

//------------------------------------------------------method update module
    @PutMapping("/update-module")
    public Object update(@RequestBody Modules modules){
       return modules_service.update(modules);
    }
//    ----------------------method pour appeler tous les modules
    @GetMapping("/list-ues-without-modules-notes")
    public List<UE> getListUesWithoutModulesNotes(){
        return ue_service.ueList_without_note_all();
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

}
