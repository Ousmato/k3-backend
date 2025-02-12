package Gestion_scolaire.Emplois.controllers;

import Gestion_scolaire.Emplois.dtos.TeacherEmploiDTO;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.services.Emplois_service;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-emplois")
@Slf4j
public class Emplois_controller {
    @Autowired
    private Emplois_service emplois_service;

    @PostMapping("/add")
    public Object addEmplois(@Valid @RequestBody Emplois emplois, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Gérer les erreurs de validation
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
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
    @GetMapping("/all-actifs-emplois-of-classe/{idClasse}")
    public List<Emplois> emploisActif(@PathVariable long idClasse){
            return emplois_service.listEmploisActifs(idClasse);
    }

//    ------------------------------------------all-actifs-emplois-with-seances
    @GetMapping("/all-actifs-emplois/{idAdmin}")
    public List<Emplois> allEmploisActif( @PathVariable long idAdmin){
        return emplois_service.listEmploisActifOfAllClasses(idAdmin);
    }


}
