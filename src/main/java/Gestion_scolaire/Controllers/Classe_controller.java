package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Services.Classe_service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api-class")
public class Classe_controller {

    @Autowired
    private Classe_service classe_service;

    @PostMapping("/add-niv-fili")
    public Object add(@RequestBody NiveauFilieres niveauFilieres){
           return classe_service.create(niveauFilieres);
    }

    //method get all class
    @GetMapping("/list-class")
    @Operation(summary = "Recuperer les classes")
    public List<StudentsClasse> getAllClasse(){
            return classe_service.readAllClass();

    }

    //method get by id
    @GetMapping("/class/{id}")
    @Operation(summary = "Recuperer la classe par l'id")
    public StudentsClasse getClass(@PathVariable long id){
            return classe_service.readByIdClasse(id);
    }

    @GetMapping("/classe-by-idNivFiliere/{idNivFiliere}")
    @Operation(summary = "Recuperer la classe par l'id nivFiliere")
    public StudentsClasse getClassByidNivFiliere(@PathVariable long idNivFiliere){
        return classe_service.readByIdNivFiliere(idNivFiliere);
    }
    //update methode
    @GetMapping("/update-promotion-classe/{idClasse}/{idAnnee}")
    @Operation(summary = "Modifier la promotion de la classe")
    public  Object update(@PathVariable long idClasse, @PathVariable long idAnnee){
            return classe_service.update(idClasse, idAnnee);
    }


    //get numbre of cunt mentions(classes)
    @GetMapping("/count-class")
    public int getCountClass(){
        return classe_service.cunt_class();
    }

    @GetMapping("/get-all-archives-by-class-id/{idClasse}")
    @Operation(summary = "Recupere les differents promotion d'une classe par son id")
    public List<StudentsClasse> getClassesByClassId(@PathVariable long idClasse){
        return classe_service.getAllArchivesById(idClasse);
    }

    //add promotion classe
    @GetMapping("/add-promotion-classe/{idNivFiliere}/{idAnnee}")
    @Operation(summary = "Ajouter promotion a la nivfiliere (mention)")
    public Object addProClasse(@PathVariable long idAnnee, @PathVariable long idNivFiliere){
        return classe_service.addProClasse(idAnnee, idNivFiliere);
    }
    //delete pro class
    @DeleteMapping("/delete-promotion-classe/{idClasse}")
    @Operation(summary = "Supprimer la classe d'une promotion")
    public Object deleteProClasse(@PathVariable long idClasse){
        return classe_service.deleteProClasse(idClasse);
    }

    //update niveau filiere
    @PutMapping("/update-niv-filiere")
    @Operation(summary = "Mise a jour du niveau filiere (mention)")
    public Object updateNivFili(@RequestBody NiveauFilieres niveauFilieres){
        return classe_service.updateNivFiliere(niveauFilieres);
    }

    //get all student classe
    @GetMapping("/get-all-classe-by-id-annee/{idAnnee}")
    @Operation(summary = "Recupere les classe par l'id de l'annee scolaire")
    public List<StudentsClasse> readAllClassIdAnneeId(@PathVariable long idAnnee){
        return classe_service.readAllClassIdAnneeId(idAnnee);
    }

    //get list of next class
    @GetMapping("/get-all-next-classe-by-id/{idClasse}")
    @Operation(summary = "Recupere les classes supperieurs par l'id de l'ancien classe")
    public List<StudentsClasse> getPreviousClasseById(@PathVariable long idClasse){
        return classe_service.getPreviousClasseById(idClasse);
    }

    //get current classe with ue
    @GetMapping("/current-classe-with-ue")
    @Operation(summary = "Recuperer la liste des classe qui on des UEs")
    public List<StudentsClasse> getCurrentClasse(){
        return classe_service.getAllCurrentClasseWithUe();
    }

    //get all classe for depot doc by type doc
    @GetMapping("classe-type-of-doc/{type}")
    @Operation(summary = "Recuper les classes pour le depot de doc par type de doc")
    public List<StudentsClasse> getClassTypeOfDoc(@PathVariable long type){
        return classe_service.getListClassForDepotDoc(type);
    }
}
