package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.GetNoteDTO;
import Gestion_scolaire.Dto_classe.NoteDTO;
import Gestion_scolaire.Dto_classe.StudentsNotesDTO;
import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.Moyenne;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Services.Note_service;
import Gestion_scolaire.Services.Ue_service;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    private Object addNote(@Valid @RequestBody Notes notes){
          return  note_service.addNote(notes);

    }

    //method get all note
    @GetMapping("/all-note-and-moyen/{idStudent}/{idSemestre}")
    @Operation(summary = "Recuperer tout les note  et la moyen general de l'etudiant ")
    public List<GetNoteDTO> getAllNoteByClasse(@PathVariable long idStudent, @PathVariable long idSemestre){
        return note_service.getNotesByIdStudentAndIdSemestre(idStudent, idSemestre);
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
    @GetMapping("/all-Modules-filter/{idStudent}/{idClasse}/{idSemestre}")
    public List<Modules> getAllModule(@PathVariable long idStudent, @PathVariable long idClasse, @PathVariable long idSemestre){
        return ue_service.getByIdStudentAndIdClasse(idStudent,idClasse, idSemestre);
    }
//    --------------------------------------read all notes of current semestre
    @GetMapping("/read-all-of-semestre/{idClasse}/{idSemestre}/{idNivFiliere}")
    public Page<StudentsNotesDTO> semestreNote(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable long idClasse, @PathVariable long idSemestre, @PathVariable long idNivFiliere){
        return note_service.listNotes(page, size, idClasse, idSemestre, idNivFiliere);
    }

    @GetMapping("/calculate-studen-moyen-by-semestre/{idStudent}/{idSemestre}")
    @Operation(summary = "Calculer la note de l'etudiant par semestre")
    public Object calculateMoyen(@PathVariable long idStudent, @PathVariable long idSemestre){
        return note_service.moyenOfStudent(idStudent, idSemestre);
    }

    //get all ids of students have note for all modules in the semestre
    @GetMapping("/get-ids-of-students/{idSemestre}/{idNivFiliere}/{idClasse}")
    @Operation(summary = "Recuperer les id des inscrits qui on des notes pour tous les modules pour le semestre")
    public List<Long> getAllIdsOfStudents(@PathVariable long idSemestre, @PathVariable long idNivFiliere, @PathVariable long idClasse){
        return note_service.getIdsOfStudents(idSemestre, idNivFiliere, idClasse);
    }
}
