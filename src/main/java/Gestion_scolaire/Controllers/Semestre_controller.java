package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Services.Semestre_service;
import Gestion_scolaire.configuration.NoteFundException;
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
    public Semestres update(@RequestBody Semestres semestre){
       return semestre_service.update(semestre);
    }
}
