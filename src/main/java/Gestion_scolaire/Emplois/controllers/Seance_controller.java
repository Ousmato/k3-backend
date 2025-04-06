package Gestion_scolaire.Emplois.controllers;

import Gestion_scolaire.Emplois.dtos.Journee_DTO;
import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Emplois.services.Jounee_service;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.Teachers.dtos.TeacherConfigJournDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-seance")
public class Seance_controller {


    @Autowired
    private Common_service common_service;
    @Autowired
    private SharedControllers sharedControllers;


//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    @Operation(summary = "Recupere la liste de tous les seances par emploi")
    public List<Journee_DTO> getAllByIdEmlois(@PathVariable long idEmplois){

            return sharedControllers.getJounee_service().readByIdEmplois(idEmplois);
    }

    @GetMapping("/lit-teacher-config/{idEmploi}")
    @Operation(summary = "Recupere la liste des enseignants avec leurs config (group, salle, seancetype) par emploi")
    public List<TeacherConfigJournDTO> getAllTeacherConfigByIdEmploi(@PathVariable long idEmploi){
        return sharedControllers.getJounee_service().getAllTeacherConfigByIdEmploi(idEmploi);
    }

    @PostMapping("/add-journee")
    @Operation(summary = "Ajout des journee pour un emplois")
    public Object add_jour(@Valid  @RequestBody List<Journee> journeeList){
        return sharedControllers.getJounee_service().addJournee(journeeList);
    }

    // -------------------add surveillance
    @PostMapping("/add-addSurveillance")
    @Operation(summary = "Programmer un examen ou session")
    public Object add_Surveillance(@RequestBody List<Journee> journeeList){
        return sharedControllers.getJounee_service().addSurveillance(journeeList);
    }

    @PutMapping("/update-seance")
    @Operation(summary = "Modification de la seance")
    public Object update_seance(@RequestBody Journee journee){
        return sharedControllers.getJounee_service().updateJournee(journee);
    }

    @DeleteMapping("/delete-journee-by-id/{idJournee}")
    @Operation(summary = "Suppression de la journee avec son id")
    public Object delete_journee_byId(@PathVariable long idJournee){
        return sharedControllers.getJounee_service().deletedJournee(idJournee);
    }

}
