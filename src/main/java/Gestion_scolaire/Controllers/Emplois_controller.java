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
//    -------------------------------------
}
