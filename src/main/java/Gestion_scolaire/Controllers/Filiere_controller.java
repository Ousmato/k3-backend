package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Services.Filieres_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filiere")
public class Filiere_controller {

    @Autowired
    private Filieres_service filieres_service;

    Filiere objectFiliere = new Filiere();

    @PostMapping("/addFiliere")
    public ResponseEntity<Object> add(@RequestBody Filiere filiere){
//        System.out.println(filiere+"---------------------------------------------");
        try {
            objectFiliere = filieres_service.create(filiere);
            return new ResponseEntity<>(objectFiliere, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//--------------------------------------------------------add niveau filiere---------------------------
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody NiveauFilieres request) {
        System.out.println(request+"------------request content------------------------------");
        Niveau niveau = request.getIdNiveau();
        Filiere filiere = objectFiliere;

        try {
            Object addedFiliere = filieres_service.add(filiere, niveau);
            return new ResponseEntity<>(addedFiliere, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------------------method pour lire la liste de niveau filiere---------------
    @GetMapping("liste")
    public ResponseEntity<List<NiveauFilieres>> liste(){
        try {
            List<NiveauFilieres> addedFiliere = filieres_service.readNivFil();
            return new ResponseEntity<>(addedFiliere, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
