package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Services.Classe_service;
import Gestion_scolaire.Services.StudentsClasse_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api-class")
public class Classe_controller {

    @Autowired
    private Classe_service classe_service;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody StudentsClasse classe){
        System.out.println(classe+"--------------------------------------");
        try {
            String addedClass = classe_service.create(classe);
            return new ResponseEntity<>(addedClass, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------method get all class-------------------------------
    @GetMapping("/list-class")
    public ResponseEntity<List<StudentsClasse>> getAllClasse(){
        try {
            List<StudentsClasse> addedClass = classe_service.readAllClass();
            return new ResponseEntity<>(addedClass, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -------------------------------method add module--------------------
    @PostMapping("/add-modules")
    public ResponseEntity<List<ClasseModule>> addModuleClass(@RequestBody List<ClasseModule> modules){
        try {
            List<ClasseModule> cm = classe_service.add(modules);
            return new ResponseEntity<>(cm, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------------------method get by id----------------------------------
    @GetMapping("/class/{id}")
    public ResponseEntity<StudentsClasse> getClass(@PathVariable long id){
        try{
            StudentsClasse cls = classe_service.readByIdClasse(id);
            return ResponseEntity.ok().body(cls);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
