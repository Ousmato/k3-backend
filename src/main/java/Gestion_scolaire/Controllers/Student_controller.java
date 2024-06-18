package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentsPresence;
import Gestion_scolaire.Services.Student_service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api-student")
public class Student_controller {
    @Autowired
    private Student_service student_service;


    @PostMapping("/add")
    public ResponseEntity<Studens> addStudent(
            @RequestParam("student") String studensString,
            @RequestParam("file") MultipartFile urlFile) {
        System.out.println("------------------" + urlFile.getOriginalFilename() + "--------------" + studensString + "---------------------------");
//        System.out.println(s.getIdClasse()+ "id de la classe");
        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true);
            Studens students = objectMapper.readValue(studensString, Studens.class);

            log.info("student JSON converted: {}", students);

            if (!urlFile.isEmpty()) {
                Studens student = student_service.add(students, urlFile);
                if (student != null) {
                    return ResponseEntity.ok(student);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // or any other appropriate response
                }
            } else {
                return ResponseEntity.badRequest().body(students);
            }
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //    ----------------------------------------method get all students----------------------------------
    @GetMapping("/list")
    private ResponseEntity<List<Studens>> getAllStudent(){
        try {
           List<Studens> studentList = student_service.readAll();
            return ResponseEntity.ok(studentList);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------method update----------------------------------------
    @PutMapping("/update")
    private ResponseEntity<Studens> update(
            @RequestParam("student") String studensString,
            @RequestParam("file") MultipartFile urlFile) {
        System.out.println("------------------" + urlFile.getOriginalFilename() + "--------------" + studensString + "---------------------------");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Studens students = objectMapper.readValue(studensString, Studens.class);
            log.info("Store JSON converted: {}", students);

            if (!urlFile.isEmpty()) {
                Studens student = student_service.update(students, urlFile);
                if (student != null) {
                    return ResponseEntity.ok(student);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // or any other appropriate response
                }
            } else {
                return ResponseEntity.badRequest().body(students);
            }
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    --------------------method desabled student-----------------------
    @GetMapping("/delete/{idStudent}")
    public ResponseEntity<String> delete(@PathVariable long idStudent){
        try{
            String student = student_service.desable(idStudent);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    ------------------------method get all student in classe------------------
    @GetMapping("/s-class/{idClasse}")
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
