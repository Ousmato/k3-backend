package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Services.Note_service;
import Gestion_scolaire.Services.Ue_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api-note")
public class Notes_controller {

    @Autowired
    private Note_service note_service;

    @Autowired
    private Ue_service ue_service;

    Notes noteReturn = new Notes();
    @PostMapping("/add-note")
    private Object addNote(@RequestBody Notes notes){
            return  note_service.addNote(notes);

    }
//    ----------------------------------methode get AllNote by student-------------
    @GetMapping("/read/{idStudent}/{idSemestre}")
    public List<Notes> getByIdStudent(@PathVariable long idStudent, @PathVariable long idSemestre){
        return note_service.readByIdCurrentSemestre(idStudent, idSemestre);

    }
//        -------------------------------method get all moyen in classe and curente semestre-----------
    @GetMapping("/allMoyen/{idClasse}/{idSemestre}")
    public List<Double> getAllNoteByClasse(@PathVariable long idClasse, @PathVariable long idSemestre){
        return note_service.calculerMoyennesClasse(idClasse, idSemestre);
    }
//    ---------------------------------------method get moyene by student----------------------------
    @GetMapping("/moyene/{idStudent}/{idSemestre}")
    public Double getMoyenStudent(@PathVariable long idStudent, @PathVariable long idSemestre){
        return note_service.calculerMoyenneGenerale(idStudent, idSemestre);
    }
//--------------------------------------methode pour appeler tout le module de la
//    classe de l'étudiant qui on deja ete programmer pour un emploi du temps
    @GetMapping("/allModules/{idClasse}")
    public ArrayList<Modules> getAllModuleProgrammed(@PathVariable long idClasse){
        return note_service.readAllByAllEmplois(idClasse);
    }
//    ---------------------------------method update --------------------------------------
    @PutMapping("/update-note")
    public Object update(@RequestBody Notes notes){
        return note_service.update(notes);
    }
//    ----------------------------liste de  modules par etudiant qui n'ont pas encore eu de note
    @GetMapping("/all-Modules-filter/{idStudent}/{idClasse}")
    public List<Modules> getAllModule(@PathVariable long idStudent, @PathVariable long idClasse){
        return ue_service.getByIdStudentAndIdClasse(idStudent,idClasse);
    }
//    --------------------------------------read all notes of current semestre
    @GetMapping("/read-all-of-semestre/{idClasse}")
    public Page<Notes> semestreNote(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable long idClasse){
        return note_service.listNotes(page, size, idClasse);
    }
}
