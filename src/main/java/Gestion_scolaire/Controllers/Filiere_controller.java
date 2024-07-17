package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Services.Filieres_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/filiere")
public class Filiere_controller {

    @Autowired
    private Filieres_service filieres_service;

    Filiere objectFiliere = new Filiere();

    @PostMapping("/addFiliere")
    public Object add(@RequestBody Filiere filiere){
            objectFiliere = filieres_service.create(filiere);
            return  objectFiliere;
    }
//--------------------------------------------------------add niveau filiere---------------------------
    @PostMapping("/add")
    public Object add(@RequestBody NiveauFilieres request) {
        Niveau niveau = request.getIdNiveau();
        Filiere filiere = objectFiliere;

        return filieres_service.add(filiere, niveau);
    }
//    ---------------------------------------------method pour lire la liste de niveau filiere---------------
    @GetMapping("liste")
    public List<NiveauFilieres> liste(){
       return filieres_service.readNivFil();
    }
//    ----------------------------update niveau filiere
    @PutMapping("/update-niveau-filiere")
    public NiveauFilieres update(@RequestBody NiveauFilieres niveauFilieres){
       return filieres_service.update(niveauFilieres);

    }
}
