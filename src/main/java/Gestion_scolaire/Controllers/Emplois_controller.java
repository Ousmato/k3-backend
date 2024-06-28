package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Services.Emplois_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-emplois")
@Slf4j
public class Emplois_controller {
    @Autowired
    private Emplois_service emplois_service;

    @PostMapping("/add")
    public ResponseEntity<Emplois> addEmplois(@RequestBody Emplois emplois){
        try {
            Emplois e = emplois_service.add(emplois);
            return new ResponseEntity<>(e, HttpStatus.OK);
        }catch (Exception e){
        log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ------------------------get emplois by idClasse--------------------------------------
    @GetMapping("/read/{idClasse}")
    public ResponseEntity<Emplois> readByIdClass(@PathVariable long idClasse){
        try {
            Emplois e = emplois_service.getByIdClasse(idClasse);
            return new ResponseEntity<>(e,HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------------method update emplois-----------------------------------
    @PutMapping("/update")
    public ResponseEntity<Emplois> update(@RequestBody Emplois emplois){
        try {
            Emplois e = emplois_service.update(emplois);
            return new ResponseEntity<>(e,HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    --------------------------methode get all emplois- teacher--------------------
    @GetMapping("/list/{idTeacher}")
    public ResponseEntity<Emplois> getAllByIdTeacher(@PathVariable long idTeacher){
        try {
           List<Emplois> list = emplois_service.findAllEmploisByTeacher(idTeacher);
            return new ResponseEntity(list,HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -------------------------------------get emplois by id ----------------------
    @GetMapping("/emplois/{id}")
    public ResponseEntity<Emplois> getById(@PathVariable long id){
        try{
            Emplois emp = emplois_service.getById(id);
            return ResponseEntity.ok(emp);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ------------------------------------------verifier l'existence d'un emplois pour la classe
    @GetMapping("/hasEmplois/{classId}")
    public ResponseEntity<Object> hasEmplois(@PathVariable long classId) {
        Object hasEmplois = emplois_service.hasEmplois(classId);
        return ResponseEntity.ok(hasEmplois);
    }
//    -----------------------methode pour verifier l'existence des seances pour un emplois
        @GetMapping("/hasSeance/{idEmplois}")
        public ResponseEntity<Boolean> hasSeance(@PathVariable long idEmplois) {
            boolean hasEmplois = emplois_service.hasSeances(idEmplois);
            return ResponseEntity.ok(hasEmplois);
        }

//        ---------------------------------method for validated
    @GetMapping("/valid/{idEmplois}")
    public ResponseEntity<Boolean> validEmplois(@PathVariable long idEmplois) {
        try {
          boolean vld =  emplois_service.validated(idEmplois);
            return ResponseEntity.ok(vld);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    ----------------------------------method to verifier emplois is valid or no
    @GetMapping("/is-valid/{idEmplois}")
    public ResponseEntity<Boolean> isValid(@PathVariable long idEmplois){
        try {
            boolean isV = emplois_service.isValid(idEmplois);
            return ResponseEntity.ok(isV);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
