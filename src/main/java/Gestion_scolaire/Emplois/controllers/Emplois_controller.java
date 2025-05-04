package Gestion_scolaire.Emplois.controllers;

import Gestion_scolaire.Emplois.dtos.DtoEmploiByWeeks;
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
    //get emplois by idClasse--------------------------------------
    @GetMapping("/read/{idEmploi}")
    public Emplois readByIdClass(@PathVariable long idEmploi){
            return emplois_service.getById(idEmploi);
    }
    //method update emplois-----------------------------------
    @PutMapping("/update")
    public Object update(@RequestBody Emplois emplois){
            return emplois_service.update(emplois);
    }

    //get emplois by id ----------------------
    @GetMapping("/emplois/{id}")
    public Emplois getById(@PathVariable long id){
            return emplois_service.getById(id);
    }


    //get all emplois actifs
    @GetMapping("/all-actifs-emplois-of-classe/{idClasse}")
    public List<Emplois> emploisActif(@PathVariable long idClasse){
            return emplois_service.listEmploisActifs(idClasse);
    }

    //all-actifs-emplois-with-seances
//    @GetMapping("/all-actifs-emplois/{idAdmin}")
//    public List<Emplois> allEmploisActif( @PathVariable long idAdmin){
//        return emplois_service.listEmploisActifOfAllClasses(idAdmin);
//    }

    @GetMapping("/all-actifs-emplois-with-seances-without-exam")
    @Operation(summary = "Recuperer les emploi du temps actif sans examen")
    public List<Emplois> allEmploisActifWithoutExam(){
       return emplois_service.currentEmploiWithoutExamWithSeance();
    }

    @GetMapping("/all-actifs-emplois-with-journee")
    @Operation(summary = "Recuperer les emploi du temps actif pour tous les classes qui ont au moins une journne")
    public List<DtoEmploiByWeeks> allEmploisActifWithouHaveJourne(@RequestParam String value){
        return emplois_service.currentEmploiHaveJourne(value);
    }


}
