package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.EmploisDTO;
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
    public Object addEmplois(@RequestBody Emplois emplois){
            return emplois_service.add(emplois);
    }
//    ------------------------get emplois by idClasse--------------------------------------
    @GetMapping("/read/{idEmploi}")
    public Emplois readByIdClass(@PathVariable long idEmploi){
            return emplois_service.getById(idEmploi);
    }
//    -----------------------------method update emplois-----------------------------------
    @PutMapping("/update")
    public Object update(@RequestBody Emplois emplois){
            return emplois_service.update(emplois);
    }
//    --------------------------methode get all emplois- teacher--------------------
//    @GetMapping("/list/{idTeacher}")
//    public List<Emplois> getAllByIdTeacher(@PathVariable long idTeacher){
//        return emplois_service.findAllEmploisByTeacher(idTeacher);
//
//    }
//    -------------------------------------get emplois by id ----------------------
    @GetMapping("/emplois/{id}")
    public Emplois getById(@PathVariable long id){
            return emplois_service.getById(id);
    }
//    ------------------------------------------verifier l'existence d'un emplois pour la classe
    @GetMapping("/hasEmplois/{classId}")
    public Object hasEmplois(@PathVariable long classId) {
        return emplois_service.hasEmplois(classId);
    }
//    -----------------------methode pour verifier l'existence des seances pour un emplois
        @GetMapping("/hasSeance/{idEmplois}")
        public Boolean hasSeance(@PathVariable long idEmplois) {
            return emplois_service.hasSeances(idEmplois);
        }

//        ---------------------------------method for validated
    @GetMapping("/valid/{idEmplois}")
    public Boolean validEmplois(@PathVariable long idEmplois) {
          return emplois_service.validated(idEmplois);
    }
//    ----------------------------------method to verifier emplois is valid or no
    @GetMapping("/is-valid/{idEmplois}")
    public Boolean isValid(@PathVariable long idEmplois){
            return emplois_service.isValid(idEmplois);
    }
//    --------------------------------get all emplois actif
    @GetMapping("/all-actifs-emplois")
    public List<Emplois> emploisActif(){
            return emplois_service.listEmploisActifs();
    }

//    ------------------------------------------all-actifs-emplois-with-seances
    @GetMapping("/all-actifs-emplois-with-seances")
    public List<EmploisDTO> emploisActifWithSeances(){
        return emplois_service.listEmploisActifs_with_seances();
    }
}
