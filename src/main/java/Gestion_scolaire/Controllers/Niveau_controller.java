package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Services.Niveau_service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-niveau")
public class Niveau_controller {

    @Autowired
    private Niveau_service niveau_service;

    @GetMapping("/readAll")
    public List<Niveau> getAll(){
       return niveau_service.readAll();
    }

    @PostMapping("/add-niveau")
    @Operation(summary = "Ajout du niveau")
    public Object addNiveau(@RequestBody Niveau niveau){
        return niveau_service.addNiveau(niveau);
    }
    //  ---------------------------------------------
    @GetMapping("/get-by-id/{idNiveau}")
    @Operation(summary = "Recuperer le niveau par id")
    public Niveau getNiveauById(@PathVariable long idNiveau){
        return niveau_service.getByIdNiveau(idNiveau);
    }

    //------------------------------------
    @PutMapping("/update-niveau")
    @Operation(summary = "Modification du niveau")
    public Object updateNiveau(@RequestBody Niveau niveau){
        return niveau_service.updateNiveau(niveau);
    }

    //  ----------------------------------------------------
    @DeleteMapping("/delete-niveau/{idNiveau}")
    @Operation(summary = "Suppression du niveau sans classe associer")
    public Object deleteNiveauById(@PathVariable long idNiveau){
        return niveau_service.deleteNiveau(idNiveau);
    }
}
