package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Services.Seance_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-seance")
public class Seance_controller {
    @Autowired
    private Seance_service seance_service;

    @PostMapping("/add")
    public ResponseEntity<List<Seances>> addSeance(@RequestBody List<Seances> seancesList){
        try {
            List<Seances> list = seance_service.addMultiple(seancesList);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    public ResponseEntity<List<Seances>> getAllByIdEmlois(@PathVariable long idEmplois){
        try {
            List<Seances> list = seance_service.readByIdEmplois(idEmplois);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------------method calculate nombre heure for month--------------------------
    @GetMapping("/nbre-heure/{idTeacher}")
    public ResponseEntity<List<Integer>> getNobreHeureByMonth(@PathVariable long idTeacher){
        try {
            List<Integer> list = seance_service.getNbreBySeance(idTeacher);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ------------------------------------
}
