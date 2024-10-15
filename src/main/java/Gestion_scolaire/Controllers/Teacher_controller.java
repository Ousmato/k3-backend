package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.ProfilDTO;
import Gestion_scolaire.Dto_classe.PaieDTO;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Services.Teachers_service;
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
    

    @PostMapping("/add")
    @Operation(summary = "Ajouter un enseignant")
    private Object addTeacher( @RequestBody ProfilDTO profilDTO) {
        return teachers_service.add(profilDTO);

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
//    -------------------------------------------------------------------------------------
    @GetMapping("/list-teacher-by-filiere/{idFiliere}")
    @Operation(summary = "Recuperer la liste des enseignants par idUe(spécialité)")
    public List<Teachers> get_list_teachers_by_idFiliere(@PathVariable long idFiliere){
        return teachers_service.readAll_byProfile(idFiliere);
    }
//    ------------------------------method get teacher by id----------------------------------
    @GetMapping("/teacher-by-id/{idTeacher}")
    private Teachers getTeacher(@PathVariable long idTeacher){
        return teachers_service.teachById(idTeacher);
    }
//    -----------------------------method update ---------------------
    @PutMapping("/update")
    private Object update(@RequestBody Teachers teacher) throws IOException {

        return teachers_service.update(teacher);

    }
//    -------------------------------------method delete---------------------
    @GetMapping("/delete/{idTeacher}")
    public Object delete(@PathVariable long idTeacher){
        return teachers_service.desactive(idTeacher);
    }
//    --------------------------------method add presence---------------
//    @PostMapping("/add-presence")
//    private  Object addPresence(@RequestBody TeachersPresence presence){
//        return teachers_service.addPresence(presence);
//    }
////    ------------------------method pour abscenter un teacher
//    @PostMapping("/change-observation")
//    public Object chage_observation(@RequestBody TeachersPresence abscence){
//        return teachers_service.change_observation(abscence);
//    }
//    -------------------------------method get status of teacher
//    @GetMapping("/status/{idTeacher}")
//    public List<TeachersPresence> getStatus(@PathVariable long idTeacher){
//        return teachers_service.getStatus(idTeacher);
//    }
//    ----------------------------------method get all teacher presence------------------
//    @GetMapping("/list-presence")
//    private  Page<TeachersPresence> listPresence(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ){
//       return teachers_service.getListPresence(page, size);
//    }

//    ----------------------------method get teacher paies----------------------------
//    @GetMapping("/list-paie")
//    private  Page<Paie> listPaie(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//                                 ){
//      return teachers_service.read_All_Paie_page(page, size);
//    }
    @GetMapping("/list-paie")
    @Operation(summary = "Recuperer la liste de tout du mois")
    public List<PaieDTO> getListPaie(){
        return teachers_service.readAllPaie();
    }

    //-----------------all paie of month
    @GetMapping("/all-paie-of-month/{month}")
    @Operation(summary = "Recuperer la liste de paie par mois")
    public List<PaieDTO> getAllPaieOfMonth(@PathVariable int month){
        return teachers_service.getAllPaieByMonth(month);
    }
//    ------------------------method add paie----------------------------------
    @GetMapping("/count-teacher-number")
    @Operation(summary = "Recuperer le nombre de d'enseignant")
    public int getCountTeacherNumber(){
        return teachers_service.countNumber();
    }
//    -----------------------------------------method pour appeller tous les heures payers de teacher
    @GetMapping("/all-hours-paie-of-teacher/{idTeacher}")
    public List<Paie> getAllHoursPaie(@PathVariable long idTeacher){
        return teachers_service.getAll_paie_byIdTeacher(idTeacher);
    }
//    ----------------------method update teacher paiement---------------------------
//    @PutMapping("/update-paie")
//    private  Paie updatePaie(@RequestBody Paie paie){
//        return teachers_service.updatePaie(paie);
//    }
    //    -----------------------------method get all enseignants qui on des emplois actif--------------------
//    @GetMapping("/all_teacher_seance_actif")
//    public List<TeacherSeancesDTO> allTeacherEmploisActif() {
//       return seance_service.all_teacher();
//    }
////    --------------------------------get page of teacher qui on des emplois actif
//@GetMapping("/get-page-teacher-seance-actif")
//public Page<TeacherSeancesDTO> getPage_teacher_seance_actif(
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size)
//{
//     return seance_service.all_teachers_seance_active(page, size);
//}
//------------------------------------------------
//    @GetMapping("/detaille/{idTeacher}")
//    public TeacherSeancesDTO getDetail_t_s(@PathVariable long idTeacher) {
//        return seance_service.getDetail(idTeacher);
//    }
    @GetMapping("/all-techer-by-with-profile")
    @Operation(summary = "Recuperer la liste de profile de l'enseignant (filiere associer)")
    public Page<ProfilDTO> getAllFiliereByTeacher(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return teachers_service.getAllProfile(page, size);
    }

    //-----------------------get teachers suggers by seachTream
    @PostMapping("teachers-filtered")
    @Operation(summary = "Recuperer la liste des teachers filtrer par numero")
    public Teachers getFilteredTeachers(@RequestBody int telephone){
        return teachers_service.getTeacersFiltered(telephone);
    }

    //-------------------------get teachers by nom
    @PostMapping("teachers-filtered-list")
    @Operation(summary = "Recuperer la liste des teachers filtrer par nom")
    public List<Teachers> getListFilteredTeachers(@RequestBody String nomTeacher){
        return teachers_service.getListFiltered(nomTeacher);
    }

}
