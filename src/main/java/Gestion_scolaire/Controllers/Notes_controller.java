package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Services.Note_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/note")
public class Notes_controller {

    @Autowired
    private Note_service note_service;

    @PostMapping("/add")
    private ResponseEntity<List<Notes>> addNote(@RequestBody List<Notes> notes){
        try {
            List<Notes> note = note_service.addNote(notes);
            return ResponseEntity.ok(note);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ----------------------------------methode get AllNote by student-------------
    @GetMapping("/read/{idStudent}/{idSemestre}")
    public ResponseEntity<List<Notes>> getByIdStudent(@PathVariable long idStudent, @PathVariable long idSemestre){
        try {
            List<Notes> note = note_service.readByIdCurrentSemestre(idStudent, idSemestre);
            return ResponseEntity.ok(note);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//        -------------------------------method get all moyen in classe and curente semestre-----------
    @GetMapping("/allMoyen/{idClasse}/{idSemestre}")
    public ResponseEntity<List<Double>> getAllNoteByClasse(@PathVariable long idClasse, @PathVariable long idSemestre){
        try {
            List<Double> moyennes = note_service.calculerMoyennesClasse(idClasse, idSemestre);
            return ResponseEntity.ok(moyennes);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------------method get moyene by student----------------------------
    @GetMapping("/moyene/{idStudent}/{idSemestre}")
    public ResponseEntity<Double> getMoyenStudent(@PathVariable long idStudent, @PathVariable long idSemestre){
        try {
            Double moyenne = note_service.calculerMoyenneGenerale(idStudent, idSemestre);
            return ResponseEntity.ok(moyenne);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//--------------------------------------methode pour appeler tout les module de la
//    classe de l'etudiant qui on deja etait programmer pour un emplois du temps
    @GetMapping("/allModules/{idClasse}")
    public ResponseEntity<ArrayList<Modules>> getAllModuleProgrammed(@PathVariable long idClasse){
        try {
            ArrayList<Modules> modulesArrayList = note_service.readAllByAllEmplois(idClasse);
            return ResponseEntity.ok(modulesArrayList);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ---------------------------------method update --------------------------------------
    @PutMapping("/update")
    public ResponseEntity<Notes> update(@RequestBody Notes notes){
        try {
            Notes note = note_service.update(notes);
            return ResponseEntity.ok(note);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
