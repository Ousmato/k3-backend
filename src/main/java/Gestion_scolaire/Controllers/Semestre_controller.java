package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Services.Semestre_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api-semestre")
@Slf4j
public class Semestre_controller {
    @Autowired
    private Semestre_service semestre_service;

    @GetMapping("/list")
    public ResponseEntity<List<Semestres>> getAll(){
        try{
            List<Semestres> list = semestre_service.getAll();
            return ResponseEntity.ok(list);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------get current semestre-------------------------
    @GetMapping("/current")
    public ResponseEntity<Semestres> getCurrent(){
        try {
            Semestres semestre = semestre_service.currentSemestre();
            return ResponseEntity.ok(semestre);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
