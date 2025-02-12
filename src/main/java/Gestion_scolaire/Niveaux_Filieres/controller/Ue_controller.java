package Gestion_scolaire.Niveaux_Filieres.controller;

import Gestion_scolaire.Classes.dtos.ModuleDTO;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.Shareds.Shared_services;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ue-api/")
public class Ue_controller {

    @Autowired
    private SharedControllers controllers;

    @PostMapping("/update-module-vol-horaire")
    @Operation(summary = "Modifier le volume horaire du module par id du module")
    public Object updateModuleVolHoraire(@RequestBody ModuleDTO moduleDTO) {
        return controllers.getSecondModulesService().udateModuleVolHoraire(moduleDTO);
    }

}
