package Gestion_scolaire.Teachers.controller;

import Gestion_scolaire.Emplois.services.Emplois_service;
import Gestion_scolaire.Teachers.dtos.SimpleTeacherDto;
import Gestion_scolaire.Teachers.dtos.TeacherDTO;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.services.Teachers_service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api-teacher")
@Slf4j
public class Teacher_controller {

    @Autowired
    private Teachers_service teachers_service;

    @Autowired
    private Emplois_service emplois_service;
    

    @PostMapping("/add/{idAdmimn}")
    @Operation(summary = "Ajouter un enseignant")
    private Object addTeacher( @RequestBody Teachers teacher, @PathVariable long idAdmimn) {
        return teachers_service.add(teacher, idAdmimn);

    }

    //method get all teachers
    @GetMapping("/list")
    public Page<Teachers> getAllTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return teachers_service.readAll(page, size);
    }

    @GetMapping("list-all")
    public List<Teachers> get_list_All_Teachers() {
        return teachers_service.readAll_teacher();
    }

    //method get teacher by id
    @GetMapping("/teacher-by-id/{idTeacher}")
    private Teachers getTeacher(@PathVariable long idTeacher){
        return teachers_service.teachById(idTeacher);
    }

    //method update
    @PutMapping("/update")
    private Object update(@RequestBody Teachers teacher) throws IOException {

        return teachers_service.update(teacher);

    }

    //method delete
    @PutMapping("/desable-teacher/{idTeacher}")
    public Object delete(@PathVariable long idTeacher){
        return teachers_service.desactive(idTeacher);
    }
//    --------------------------------method add presence---------------
    @PostMapping("/add-teachers-import/{idAdmin}")
    @Operation(summary = "Ajouter des enseignants depuis un fichier excel")
    private  Object addPresence(@RequestBody List<Teachers> teachers, @PathVariable long idAdmin){
        return teachers_service.importTeachers(teachers, idAdmin);
    }

//    ------------------------method add paie----------------------------------
    @GetMapping("/count-teacher-number")
    @Operation(summary = "Recuperer le nombre de d'enseignant")
    public int getCountTeacherNumber(){
        return teachers_service.countNumber();
    }


    @GetMapping("/all-techer-by-with-profile")
    @Operation(summary = "Recuperer la liste de profile de l'enseignant (filiere associer)")
    public Page<TeacherDTO> getAllFiliereByTeacher(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return teachers_service.getAllProfile(page, size);
    }

    //-----------------------get teachers suggers by seachTream
    @PostMapping("teachers-filtered")
    @Operation(summary = "Recuperer la liste des teachers filtrer par numero")
    public Teachers getFilteredTeachers(@RequestBody String telephone){
        return teachers_service.getTeacersFiltered(telephone);
    }

    //-------------------------get teachers by nom
    @PostMapping("teachers-filtered-list")
    @Operation(summary = "Recuperer la liste des teachers filtrer par nom")
    public List<Teachers> getListFilteredTeachers(@RequestBody String nomTeacher){
        return teachers_service.getListFiltered(nomTeacher);
    }

    @GetMapping("/all-emplois-of-teacher/{idAnnee}/{idTeacher}")
    @Operation(summary = "Recuperer les emplois d'un enseignant par an")
    public TeacherDTO getAllEmploisOfTeacherByYear(@PathVariable long idAnnee, @PathVariable long idTeacher) {
        return emplois_service.allEmploisOfTeacherByIdAnnee(idAnnee, idTeacher);
    }
    @GetMapping("/all-emplois-of-teacher-of-current-year/{idTeacher}")
    public TeacherDTO getAllEmploisOfCurrentYear(@PathVariable long idTeacher){
        return emplois_service.allEmploiOfTeacherOfCurrentYear(idTeacher);
    }

    @GetMapping("get-all-teachers-have-emplois-by-idAnnee-and-idSemestre/{idAnnee}/{idSemestre}")
    @Operation(summary = "Recuperer tous les enseignants qui ont des emplois pour l'annee")
    public List<SimpleTeacherDto> allTeachersHaveEmploisByIdAnnee(@PathVariable long idAnnee, @PathVariable long idSemestre) {
        return emplois_service.allTeachersHaveEmploisByIdAnnee(idAnnee, idSemestre);
    }
}
