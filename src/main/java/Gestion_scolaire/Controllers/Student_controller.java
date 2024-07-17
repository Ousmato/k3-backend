package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentsPresence;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Services.Student_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private List<Studens> getAllStudent(){
        return student_service.readAll();
    }
//    -----------------------method update----------------------------------------
    @PutMapping("/update")
    private Studens update(
            @RequestParam("student") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {
//        System.out.println("------------------" + urlFile.getOriginalFilename() + "--------------" + studensString + "---------------------------");

        ObjectMapper objectMapper = new ObjectMapper();
        Studens studens = objectMapper.readValue(studensString, Studens.class);
        // Vérifie si urlFile est null ou vide
        if (urlFile == null || urlFile.isEmpty()) {
            // Si pas de fichier photo, ajoute  sans spécifier de photo
            return student_service.update(studens, null);
        } else {
            // Si un fichier photo est fourni, ajoute  avec la photo
            return student_service.update(studens, urlFile);

        }
    }
//    --------------------method desabled student-----------------------
    @GetMapping("/desable/{idStudent}")
    public ResponseEntity<Studens> delete(@PathVariable long idStudent){
        try{
            Studens student = student_service.desable(idStudent);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    ------------------------method get all student in classe------------------
    @GetMapping("/list-student-by-classe/{idClasse}")
    public ResponseEntity<List<Studens>> AllStudentClass(@PathVariable long idClasse){
        try {
            List<Studens> listStudent = student_service.readAllByClassId(idClasse);
            return new ResponseEntity<>(listStudent, HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    ------------------------methode call studend by id------------------------------
    @GetMapping("/student/{idStudent}")
    private ResponseEntity<Studens> getStudent(@PathVariable long idStudent){
        try {
            Studens Student = student_service.studenById(idStudent);
            return new ResponseEntity<>(Student, HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//   -------------------- method add presence--------------------------------
    @PostMapping("/add-presence")
    public ResponseEntity<StudentsPresence> addPresence(@RequestBody StudentsPresence presence){
        try {
            StudentsPresence sp = student_service.addPresence(presence);
            return new ResponseEntity<>(sp, HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    ----------------------------------method get all presence-----------------------------
    @GetMapping("/list-presence")
    public ResponseEntity<List<StudentsPresence>> getListPresence(){
        try {
            List<StudentsPresence> splist = student_service.getListPresence();
            return new ResponseEntity<>(splist, HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
