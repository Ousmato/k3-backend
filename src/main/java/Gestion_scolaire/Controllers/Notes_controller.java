package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.AddNoteDTO;
import Gestion_scolaire.Dto_classe.GetInputNoteInscritDTO;
import Gestion_scolaire.Dto_classe.GetNoteDTO;
import Gestion_scolaire.Dto_classe.StudentsNotesDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Models.Moyenne;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Services.Note_service;
import Gestion_scolaire.Classes.services.Ue_service;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api-note")
public class Notes_controller {

    @Autowired
    private SharedControllers sharedControllers;

    Notes noteReturn = new Notes();
    @PostMapping("/add-note")
    private Object addNote(@Valid @RequestBody AddNoteDTO notes){
          return sharedControllers.getNote_service().addNote(notes);

    }

    //method get all note
    @GetMapping("/all-note-and-moyen/{idStudent}/{idSemestre}")
    @Operation(summary = "Recuperer tout les note  et la moyen general de l'etudiant ")
    public List<GetNoteDTO> getAllNoteByClasse(@PathVariable long idStudent, @PathVariable long idSemestre){
        return sharedControllers.getNote_service().getNotesByIdStudentAndIdSemestre(idStudent, idSemestre);
    }


//    ---------------------------------method update --------------------------------------
    @PutMapping("/update-note")
    public Object update(@RequestBody Notes notes){
        return sharedControllers.getNote_service().update(notes);
    }

//    --------------------------------------read all notes of current semestre
    @GetMapping("/read-all-of-semestre/{idClasse}/{idSemestre}")
    public Page<StudentsNotesDTO> semestreNote(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @PathVariable long idClasse, @PathVariable long idSemestre){
        return sharedControllers.getNote_service().listNotes(page, size, idClasse, idSemestre);
    }

    @GetMapping("/calculate-students-moyens-by-semestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Calculer la note des etudiants par semestre")
    public void calculateMoyen(@PathVariable long idClasse, @PathVariable long idSemestre){
        sharedControllers.getMoyenne_service().moyenOfStudent(idClasse, idSemestre);
    }

    //get all ids of students have note for all modules in the semestre
    @GetMapping("/get-ids-of-students/{idSemestre}/{idClasse}")
    @Operation(summary = "Recuperer les id des inscrits qui on des notes pour tous les modules pour le semestre")
    public List<Long> getAllIdsOfStudents(@PathVariable long idSemestre, @PathVariable long idClasse){
        return sharedControllers.getCommon_service().getIdsOfStudents(idSemestre, idClasse);
    }
    @GetMapping("/add-note-all-inscrit-of-class/{idClasse}/{idAnnee}/{idSemestre}/{idModule}")
    @Operation(summary = "Recuperer la liste des ues de la classe pour l'etudiant")
    public Page<GetInputNoteInscritDTO> getAllUeForStudent(
            @PathVariable long idClasse,
            @PathVariable long idAnnee,
            @PathVariable long idSemestre,
            @PathVariable long idModule,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size){
        return  sharedControllers.getUeService().getNotesForStudentInModules(idClasse, idAnnee, idSemestre, idModule, page, size);
    }

    @GetMapping("/get-all-moyennes-of-class-by-semestre/{idClasse}/{idSemestre}")
    @Operation(summary = "Recuperer les moyennes generales des etudiant par classe et semestre")
    public List<Moyenne> getMoyennesByClasse(@PathVariable long idClasse, @PathVariable long idSemestre){
        return sharedControllers.getMoyenne_service().getMoyenneByIdClassAndSemestre(idClasse, idSemestre);
    }

    @GetMapping("/get-student-note-of-semestre-by-idClasse/{idClasse}")
    @Operation(summary = "Recuperer tout les notes des differents semestre par id de la classe")
    public List<InscriptionNoteDTO> getStudentNoteOfSemestreById(@PathVariable long idClasse){
        return  sharedControllers.getMoyenne_service().getListSemestreMoyennes(idClasse);
    }

}
