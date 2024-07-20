package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Services.Classe_service;
import Gestion_scolaire.Services.StudentsClasse_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api-class")
public class Classe_controller {

    @Autowired
    private Classe_service classe_service;

    @PostMapping("/add")
    public Object add(@RequestBody StudentsClasse classe){
           return classe_service.create(classe);
    }
//    ---------------------------------method get all class-------------------------------
    @GetMapping("/list-class")
    public List<StudentsClasse> getAllClasse(){
            return classe_service.readAllClass();

    }
//    ---------------------------------------------method get by id----------------------------------
    @GetMapping("/class/{id}")
    public StudentsClasse getClass(@PathVariable long id){
            return classe_service.readByIdClasse(id);
    }
//-------------------------------------update methode
    @PutMapping("/update-class")
    public  Object update(@RequestBody StudentsClasse classe){
            return classe_service.update(classe);
    }
}
