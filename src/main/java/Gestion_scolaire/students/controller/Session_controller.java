package Gestion_scolaire.students.controller;

import Gestion_scolaire.students.services.StudentSession_service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session-api")
public class Session_controller {

    @Autowired
    private StudentSession_service studentSession_service;

    @PostMapping("/add-session/{idIncrit}/{idSemestre}/{idModule}")
    @Operation(summary = "Ajouter la note du session pour un etudiant")
    public Object addSession(@PathVariable long idIncrit, @PathVariable long idSemestre,@PathVariable long idModule, @RequestBody Double note){
        return studentSession_service.addSessionNote(idIncrit, idSemestre, idModule, note);
    }
}
