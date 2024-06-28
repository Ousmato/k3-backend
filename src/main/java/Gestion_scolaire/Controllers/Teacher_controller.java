package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.TeacherSeancesDTO;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.TeachersPresence;
import Gestion_scolaire.Services.Seance_service;
import Gestion_scolaire.Services.Teachers_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api-teacher")
@Slf4j
public class Teacher_controller {

    @Autowired
    private Teachers_service teachers_service;

    @Autowired
    private Seance_service seance_service;

    @PostMapping("/add")
    private ResponseEntity<Teachers> addTeacher(
            @RequestParam("teacher") String teacherString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Teachers t = objectMapper.readValue(teacherString, Teachers.class);
            log.info("Store JSON converted: {}", t);

            // Vérifie si urlFile est null ou vide
            if (urlFile == null || urlFile.isEmpty()) {
                // Si pas de fichier photo, ajoute l'enseignant sans spécifier de photo
                Teachers teacher = teachers_service.add(t, null);
                return ResponseEntity.ok(teacher);
            } else {
                // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
                Teachers teacher = teachers_service.add(t, urlFile);
                return ResponseEntity.ok(teacher);
            }
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //    --------------------------------------method get all teachers--------------------------
    @GetMapping("/list")
    private ResponseEntity<List<Teachers>> getList(){
        try {
            List<Teachers> list = teachers_service.readAll();
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    ------------------------------method get teacher by id----------------------------------
    @GetMapping("/tech-by-id/{idTeacher}")
    private ResponseEntity<Teachers> getTeacher(@PathVariable long idTeacher){
        try{
            Teachers t = teachers_service.teachById(idTeacher);
            return new ResponseEntity<>(t, HttpStatus.OK);
        }catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    -----------------------------method update ---------------------
    @PutMapping("/update")
    private ResponseEntity<Teachers> update(
            @RequestParam("teacher") String teacherString,
            @RequestParam("file") MultipartFile urlFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Teachers t = objectMapper.readValue(teacherString, Teachers.class);
            log.info("Store JSON converted: {}", t);

    //            if (!urlFile.isEmpty()) {
            Teachers teacher = teachers_service.update(t, urlFile);
    //                if (teacher != null) {
            return ResponseEntity.ok(teacher);
    //                } else {
    //                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // or any other appropriate response
    //                }
    //                throw new RuntimeException("file no exist");
    //            }
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    -------------------------------------method delete---------------------
    @GetMapping("/delete/{idTeacher}")
    public ResponseEntity<String> delete(@PathVariable long idTeacher){
        try{
            String student = teachers_service.desactive(idTeacher);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    --------------------------------method add presence---------------
    @PostMapping("/add-presence")
    private  ResponseEntity<TeachersPresence> addPresence(@RequestBody TeachersPresence presence){
        try{
            TeachersPresence teachersPresence = teachers_service.addPresence(presence);
            return new ResponseEntity<>(teachersPresence, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
//    ----------------------------------method get all teacher presence------------------
    @GetMapping("/list-present")
    private  ResponseEntity<List<TeachersPresence>> listPresence(){
        try{
            List<TeachersPresence> teachersPresence = teachers_service.getListPresence();
            return new ResponseEntity<>(teachersPresence, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

//    ----------------------------method get teacher paies----------------------------
    @GetMapping("/list-paie")
    private  ResponseEntity<List<Paie>> listPaie(){
        try{
            List<Paie> paieList = teachers_service.readAllPaie();
            return new ResponseEntity<>(paieList, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
//    ------------------------method add paie----------------------------------
    @PostMapping("/add-paie")
    private  ResponseEntity<Paie> addPaie(@RequestBody Paie paie){
        try{
            Paie p = teachers_service.addPaie(paie);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
//    ----------------------method update teacher paiement---------------------------
    @PutMapping("/update-paie")
    private  ResponseEntity<Paie> updatePaie(@RequestBody Paie paie){
        try{
            Paie p = teachers_service.updatePaie(paie);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    //    -----------------------------method get all enseignants qui on des emplois actif--------------------
    @GetMapping("/all_teacher_seance_actif")
    public ResponseEntity<List<TeacherSeancesDTO>> allTeacherEmploisActif() {
        try {
            List<TeacherSeancesDTO> list = seance_service.all_teacher();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace(); // Pour des fins de débogage, à retirer en production
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//------------------------------------------------
    @GetMapping("/detaille/{idTeacher}")
    public ResponseEntity<TeacherSeancesDTO> getDetail_t_s(@PathVariable long idTeacher) {
        try {
            TeacherSeancesDTO tsDto = seance_service.getDetail(idTeacher);
            return ResponseEntity.ok(tsDto);
        } catch (Exception e) {
            e.printStackTrace(); // Pour des fins de débogage, à retirer en production
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
