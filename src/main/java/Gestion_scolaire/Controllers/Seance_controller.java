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
    public List<Journee_DTO> getAllByIdEmlois(@PathVariable long idEmplois){

            return jounee_service.readByIdEmplois(idEmplois);
    }

    @PostMapping("/add-journee")
    @Operation(summary = "Ajout des journee pour un emplois")
    public Object add_jour(@RequestBody List<Journee> journeeList){
        return jounee_service.addJournee(journeeList);
    }

}
