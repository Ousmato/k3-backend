package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Services.Semestre_service;
import Gestion_scolaire.configuration.NoteFundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-semestre")
@Slf4j
public class Semestre_controller {
    @Autowired
    private Semestre_service semestre_service;

    @GetMapping("/list")
    public List<Semestres> getAll(){
       return semestre_service.getAll();
    }
//    ---------------------------------get current semestre-------------------------
    @GetMapping("/current")
    public Semestres getCurrent(){
        return semestre_service.currentSemestre();
    }
//    -----------------------------------------update semestre
    @PutMapping("/update")
    public Object update(@RequestBody Semestres semestre){
       return semestre_service.update(semestre);
    }

    @GetMapping("/semestre-by-classe-id/{idClasse}")
    public Semestres get_semesre(@PathVariable int idClasse){
        return semestre_service.semestre_classe_id(idClasse);
    }

    @GetMapping("/current-semestre-of-year")
    @Operation(summary = "Recuperer la liste des tous les semestre en cours")
    public List<Semestres> getCurrentSemestreOfYear(){
        return semestre_service.currenctSemestres();
    }
//    ----------------------------------add semestre
    @PostMapping("/add-semestre")
    public Object addSemestre(@RequestBody Semestres semestre){
        return  semestre_service.add_semestre(semestre);
    }
}
