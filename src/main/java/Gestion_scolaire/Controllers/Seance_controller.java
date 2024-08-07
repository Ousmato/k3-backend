package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.SeanceDTO;
import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Services.Seance_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public Object addSeance(@RequestBody List<Seances> seancesList){
            return seance_service.createSeance(seancesList);
    }
//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    public List<Seances> getAllByIdEmlois(@PathVariable long idEmplois){

            return seance_service.readByIdEmplois(idEmplois);
    }
//    ---------------------------------------method calculate nombre heure for month--------------------------
    @GetMapping("/nbre-heure/{idTeacher}")
    public List<Integer> getNobreHeureByMonth(@PathVariable long idTeacher){
            return seance_service.getNbreBySeance(idTeacher);

    }
//    ------------------------------------method delete seance
    @DeleteMapping("/delete/{idSeance}")
    public Object deletedSeance(@PathVariable long idSeance){
            return seance_service.deletedSeance(idSeance);
    }
//    ------------------------------------update seance
    @PutMapping("/update")
    public Object updateSeance(@RequestBody Seances seance){
       return seance_service.update(seance);

    }
//    ------------get seance by id
    @GetMapping("/get-by-id/{idSeance}")
    public SeanceDTO get_by_idSeance(@PathVariable long idSeance){
        return seance_service.get_by_id(idSeance);
    }
}
