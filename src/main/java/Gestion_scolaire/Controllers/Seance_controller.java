package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.Journee;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Services.Jounee_service;
import io.swagger.v3.oas.annotations.Operation;
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
    private Jounee_service jounee_service;


//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    @Operation(summary = "Recupere la liste de tous les seances par emploi")
    public List<Journee_DTO> getAllByIdEmlois(@PathVariable long idEmplois){

            return jounee_service.readByIdEmplois(idEmplois);
    }

    @GetMapping("/lit-teacher-config/{idEmploi}")
    @Operation(summary = "Recupere la liste des enseignants avec leurs config (group, salle, seancetype) par emploi")
    public List<TeacherConfigJournDTO> getAllTeacherConfigByIdEmploi(@PathVariable long idEmploi){
        return jounee_service.getAllTeacherConfigByIdEmploi(idEmploi);
    }

    @PostMapping("/add-journee")
    @Operation(summary = "Ajout des journee pour un emplois")
    public Object add_jour(@RequestBody List<Journee> journeeList){
        return jounee_service.addJournee(journeeList);
    }

    // -------------------add surveillance
    @PostMapping("/add-addSurveillance")
    @Operation(summary = "Programmer un examen ou session")
    public Object add_Surveillance(@RequestBody List<Journee> journeeList){
        return jounee_service.addSurveillance(journeeList);
    }

}
