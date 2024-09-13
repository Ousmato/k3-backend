package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Services.Filieres_service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api-filiere")
public class Filiere_controller {

    @Autowired
    private Filieres_service filieres_service;

    Filiere objectFiliere = new Filiere();

    @PostMapping("/addFiliere")
    public Object add(@RequestBody Filiere filiere){
           return filieres_service.create(filiere);

    }

//    ---------------------------------------------method pour lire la liste de niveau filiere---------------
    @GetMapping("/list-mentions")
    @Operation(summary = "Recuperer la liste des niveau et filiere associer")
    public List<NiveauFilieres> liste(){
       return filieres_service.readNivFil();
    }

    //    --------------------------------------------
    @PutMapping("/update")
    @Operation(summary = "Modifier la filiere")
    public Object update(@RequestBody Filiere filiere){
        return filieres_service.updateFilirer(filiere);
    }

    //-----------------------------------------
    @GetMapping("/readAll")
    @Operation(summary = "Recuperer la liste des filieres")
    public List<Filiere> readAll(){
      return   filieres_service.getFilieres();
    }

    //-------------------------------------
    @DeleteMapping("/filiere-delete/{idFiliere}")
    @Operation(summary = "Suppression du filiere sans association")
    public Object deleteFiliere(@PathVariable int idFiliere){
        return filieres_service.deleteFiliere(idFiliere);
    }
}
