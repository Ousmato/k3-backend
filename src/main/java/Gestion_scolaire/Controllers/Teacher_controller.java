package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.TeacherSeancesDTO;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.TeachersPresence;
import Gestion_scolaire.Services.Seance_service;
import Gestion_scolaire.Services.Teachers_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private Object addTeacher(
            @RequestParam("teacher") String teacherString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            Teachers t = objectMapper.readValue(teacherString, Teachers.class);
             // Vérifie si urlFile est null ou vide
            if (urlFile == null || urlFile.isEmpty()) {
                // Si pas de fichier photo, ajoute l'enseignant sans spécifier de photo
                return teachers_service.add(t, null);
            } else {
                // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
                return teachers_service.add(t, urlFile);

            }
    }

    //    --------------------------------------method get all teachers--------------------------
    @GetMapping("/list")
    public Page<Teachers> getAllTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return teachers_service.readAll(page, size);
    }

    @GetMapping("list-all")
    public List<Teachers> get_list_All_Teachers() {
        return teachers_service.readAll_teacher();
    }
//    ------------------------------method get teacher by id----------------------------------
    @GetMapping("/teacher-by-id/{idTeacher}")
    private Teachers getTeacher(@PathVariable long idTeacher){
        return teachers_service.teachById(idTeacher);
    }
//    -----------------------------method update ---------------------
    @PutMapping("/update")
    private Object update(
            @RequestParam("teacher") String teacherString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            Teachers t = objectMapper.readValue(teacherString, Teachers.class);
            log.info("Store JSON converted: {}", t);

        if (urlFile == null) {
            System.out.println("file is ");
            return teachers_service.update(t, null);
        }
        return teachers_service.update(t, urlFile);

    }
//    -------------------------------------method delete---------------------
    @GetMapping("/delete/{idTeacher}")
    public Object delete(@PathVariable long idTeacher){
        return teachers_service.desactive(idTeacher);
    }
//    --------------------------------method add presence---------------
    @PostMapping("/add-presence")
    private  Object addPresence(@RequestBody TeachersPresence presence){
        return teachers_service.addPresence(presence);
    }
//    ------------------------method pour abscenter un teacher
    @PostMapping("/change-observation")
    public Object chage_observation(@RequestBody TeachersPresence abscence){
        return teachers_service.change_observation(abscence);
    }
//    -------------------------------method get status of teacher
    @GetMapping("/status/{idTeacher}")
    public List<TeachersPresence> getStatus(@PathVariable long idTeacher){
        return teachers_service.getStatus(idTeacher);
    }
//    ----------------------------------method get all teacher presence------------------
    @GetMapping("/list-presence")
    private  Page<TeachersPresence> listPresence(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
       return teachers_service.getListPresence(page, size);
    }
//------------------------------method get presence by id seance
    @GetMapping("/presence-by-idseance/{idSeance}")
    public TeachersPresence getBySeanceId(@PathVariable long idSeance){
        return teachers_service.getByIdSeanceId(idSeance);
    }
//    ----------------------------method get teacher paies----------------------------
    @GetMapping("/list-paie")
    private  Page<Paie> listPaie(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
                                 ){
      return teachers_service.read_All_Paie_page(page, size);
    }
//    ------------------------method add paie----------------------------------
    @PostMapping("/add-paie")
    private  Object addPaie(@RequestBody Paie paie){
       return teachers_service.addPaie(paie);
    }
//    -----------------------------------------method pour appeller tous les heures payers de teacher
    @GetMapping("/all-hours-paie-of-teacher/{idTeacher}")
    public List<Paie> getAllHoursPaie(@PathVariable long idTeacher){
        return teachers_service.getAll_paie_byIdTeacher(idTeacher);
    }
//    ----------------------method update teacher paiement---------------------------
    @PutMapping("/update-paie")
    private  Paie updatePaie(@RequestBody Paie paie){
        return teachers_service.updatePaie(paie);
    }
    //    -----------------------------method get all enseignants qui on des emplois actif--------------------
    @GetMapping("/all_teacher_seance_actif")
    public List<TeacherSeancesDTO> allTeacherEmploisActif() {
       return seance_service.all_teacher();
    }
//    --------------------------------get page of teacher qui on des emplois actif
@GetMapping("/get-page-teacher-seance-actif")
public Page<TeacherSeancesDTO> getPage_teacher_seance_actif(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size)
{
    return seance_service.all_teachers_seance_active(page, size);
}
//------------------------------------------------
    @GetMapping("/detaille/{idTeacher}")
    public TeacherSeancesDTO getDetail_t_s(@PathVariable long idTeacher) {
        return seance_service.getDetail(idTeacher);
    }
}
