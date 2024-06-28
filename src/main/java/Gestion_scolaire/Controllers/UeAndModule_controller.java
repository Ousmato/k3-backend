package Gestion_scolaire.Controllers;

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
    public ResponseEntity<UE> addUe(@RequestBody UE ue){
        try {
            UE ue1 = ue_service.add(ue);
            return ResponseEntity.status(HttpStatus.OK).body(ue1);
        }catch (Exception  e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------------methode add modules-------------------------------
    @PostMapping("/add-module-class")
    public ResponseEntity<List<ClasseModule>> addClassModule(@RequestBody List<ClasseModule> cm){
        try {
            List<ClasseModule> modulesList = classe_service.add(cm);
            return ResponseEntity.status(HttpStatus.OK).body(modulesList);
        }catch (Exception  e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------------------------methode add module in module---------------
    @PostMapping("/add-module")
    public ResponseEntity<Modules> addModule(@RequestBody Modules module){
        try {
            Modules m = modules_service.addModule(module);
            return ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (Exception  e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    --------------------------------------------method get list all eu----------------------
    @GetMapping("/list-ue/{idClasse}")
    public ResponseEntity<List<UE>> getAllUe(@PathVariable long idClasse){
        try {
            List<UE> ueList = ue_service.readAll(idClasse);
            return ResponseEntity.status(HttpStatus.OK).body(ueList);
        }catch (Exception  e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------------------method get list module -----------
    @GetMapping("/list-module")
    public ResponseEntity<List<Modules>> getAllModule(){
        try {
            List<Modules> modulesList = modules_service.readAll();
            return ResponseEntity.status(HttpStatus.OK).body(modulesList);
        }catch (Exception  e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    --------------------------methode get all module in class-----------------------
    @GetMapping("/all-module/{idClasse}")
    public ResponseEntity<List<Modules>> getListModule(@PathVariable long idClasse){
        try{
            List<Modules> list = ue_service.listModule(idClasse);
            return ResponseEntity.ok(list);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------------------methode get list ue---------------------
    @GetMapping("/all-ue")
    public ResponseEntity<List<UE>> getListUe(){
        try {
            List<UE> list = ue_service.getListUe();
            return ResponseEntity.ok(list);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
