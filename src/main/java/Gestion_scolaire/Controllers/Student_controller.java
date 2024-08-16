package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.CuntStudentDTO;
import Gestion_scolaire.Dto_classe.DTO_scolarite;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Services.Groupe_service;
import Gestion_scolaire.Services.Student_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api-student")
public class Student_controller {
    @Autowired
    private Student_service student_service;

    @Autowired
    private Groupe_service groupe_service;


    @PostMapping("/add")
    public Object addStudent(
            @RequestParam("student") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true);
            objectMapper.registerModule(new JavaTimeModule());
            Studens students = objectMapper.readValue(studensString, Studens.class);

            if (!urlFile.isEmpty()) {
                return student_service.add(students, urlFile);
            }

        return student_service.add(students, null);
    }

    //    ----------------------------------------method get all students----------------------------------
    @GetMapping("/list")
    public Page<Studens> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return student_service.readAll(page, size);
    }
//    ----------------------------methode find all student
    @GetMapping("/find-all")
    public List<Studens> getAllStudents() {
        return student_service.find_all();
    }
//    -----------------------method update----------------------------------------
    @PutMapping("/update")
    private Object update(
            @RequestParam("student") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {
//        System.out.println("------------------" + urlFile.getOriginalFilename() + "--------------" + studensString + "---------------------------");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Studens studens = objectMapper.readValue(studensString, Studens.class);
        // Vérifie si urlFile est null ou vide
        if (urlFile == null) {
            System.out.println("file is ");
            return student_service.update(studens, null);
        }
            return student_service.update(studens, urlFile);

    }
//    --------------------method desabled student-----------------------
    @GetMapping("/desable/{idStudent}")
    public Object delete(@PathVariable long idStudent){
        return student_service.desable(idStudent);

    }
//    ------------------------method get all student in classe------------------
    @GetMapping("/list-student-by-classe/{idClasse}")
    public Page<Studens> AllStudentClass(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @PathVariable long idClasse){
        return student_service.readAllByClassId(page, size, idClasse);

    }
//    ------------------------methode call studend by id------------------------------
    @GetMapping("/student-by-id/{idStudent}")
    public Studens getStudent(@PathVariable long idStudent){
        return student_service.studenById(idStudent);

    }
//   -------------------- method add presence--------------------------------
    @PostMapping("/add-presence")
    public StudentsPresence addPresence(@RequestBody StudentsPresence presence){
        return student_service.addPresence(presence);
    }
//    ----------------------------------method get all presence-----------------------------
    @GetMapping("/list-presence")
    public List<StudentsPresence> getListPresence(){
        return student_service.getListPresence();

    }
//    --------------------------update scolarite
    @PutMapping("/update-scolarite/{idStudent}")
    public Object updateScolarite(@PathVariable long idStudent, @RequestBody DTO_scolarite dtoScolarite){
        return student_service.update_scolarite(idStudent, dtoScolarite.getScolarite());
    }
//------------------list des etudians valider de la classe
    @GetMapping("/student-by-classe-id/{idClasse}")
    public List<Studens> getStudentByClasse(@PathVariable long idClasse){
        return student_service.get_by_classId(idClasse);
    }

//    -------------------------------------get all student by id annee scolaire
    @GetMapping("/student-by-anneScolaire-id/{idAnne}")
    public Page<Studens> getListByIaAnne(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @PathVariable long idAnne){
        return student_service.get_by_idAnneeScolaire(page, size, idAnne);
    }

//    -------------------------------------------------------------------------------

    @GetMapping("/find-all-groupe")
    public  List<StudentGroupe> getAll_group(){
        return groupe_service.findAll();
    }

//    -----------------------------------add group
    @PostMapping("/add-group")
    public Object addGroup(@RequestBody StudentGroupe studentGroupe){
        return groupe_service.add_group(studentGroupe);
    }

    @GetMapping("/list-group-by-idEmploi/{idEmplois}")
    public List<StudentGroupe> getListGroupByEmploi(@PathVariable long idEmplois){
        return groupe_service.listGroupByIdEmploi(idEmplois);
    }

//    ----------------------------------------------------------------------

    @PostMapping("/add-more-participant")
    public Object addMoreParticipant(@RequestBody List<Participant> participants){
        return groupe_service.add_participant(participants);
    }

//    --------------get all participant by emplois id
    @GetMapping("/list-participant-by-emploi-id/{emploiId}")
    public List<Participant> getListParticipantByEmploiId(@PathVariable long emploiId){
        return groupe_service.ge_allBy_idEmploi(emploiId);
    }

//    -----------------------get All Students By Group
    @GetMapping("/list-students-by-group-id/{idGroup}")
    public List<Studens> getAllStudentByGroup(@PathVariable long idGroup){
        return groupe_service.getAllStudentsByGroupId(idGroup);
    }

//    -----------------------------get sum of all scolarite of student in this year
    @GetMapping("/sum-scolarite")
    public double sumScolarite(){
        return student_service.get_scolarite_annuel();
    }

//    -----------------------get sum of reliquat in this current year
    @GetMapping("/sum-reliquat")
    public double sumReliquat(){
        return student_service.getAll_reliquat();
    }

//    --------------------cunt student inscrit and non inscrit
    @GetMapping("/student-count")
    public CuntStudentDTO countStudent(){
        return student_service.cunt_student_inscrit();
    }
//    -------------------------------------------------reincreiption method
    @PostMapping("/re-inscription")
    public Object reInscription(@RequestBody Studens studens){
            return student_service.reinscription(studens);
    }

}
