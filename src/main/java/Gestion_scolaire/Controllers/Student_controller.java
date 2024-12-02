package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Services.Doc_service;
import Gestion_scolaire.Services.Groupe_service;
import Gestion_scolaire.Services.Student_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private Doc_service doc_service;


    @PostMapping("/add")
    public Object addStudent(
            @RequestParam("inscription") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            InscriptionDTO dto = objectMapper.readValue(studensString, InscriptionDTO.class);


            if (!urlFile.isEmpty()) {
                return student_service.add(dto, urlFile);
            }

        return student_service.add(dto, null);
    }

    //    ----------------------------------------method get all students----------------------------------
    @GetMapping("/list")
    @Operation(summary = "Recuperer la liste des etudiant de l'annee en cours")

    public Page<Inscription> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
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
            @RequestParam("inscription") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {
//        System.out.println("------------------" + urlFile.getOriginalFilename() + "--------------" + studensString + "---------------------------");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Inscription inscrit = objectMapper.readValue(studensString, Inscription.class);
        // Vérifie si urlFile est null ou vide
        if (urlFile == null) {

            System.out.println("file is ");
            return student_service.update(inscrit, null);
        }
            return student_service.update(inscrit, urlFile);

    }
//    --------------------method desabled student-----------------------
    @GetMapping("/desable/{idStudent}")
    public Object delete(@PathVariable long idStudent){
        return student_service.desable(idStudent);

    }
//    ------------------------method get all student in classe------------------
    @GetMapping("/list-student-by-classe/{idClasse}")
    public Page<Inscription> AllStudentClass(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "20") int size,
            @PathVariable long idClasse){
        return student_service.readAllByClassId(page, size, idClasse);

    }
//    ------------------------methode call studend by id------------------------------
    @GetMapping("/student-by-id/{idStudent}")
    public Studens getStudent(@PathVariable long idStudent){
        return student_service.studenById(idStudent);

    }

//    --------------------------update scolarite
    @PutMapping("/update-scolarite/{idStudent}/{idAdmin}")
    public Object updateScolarite(@PathVariable long idStudent, @PathVariable long idAdmin, @RequestBody DTO_scolarite dtoScolarite){
        return student_service.update_scolarite(idStudent, idAdmin, dtoScolarite.getScolarite());
    }

    //get all student by id annee scolaire
    @GetMapping("/student-by-anneScolaire-id/{idAnne}")
    public Page<Inscription> getListByIaAnne(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "20") int size,
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
    @GetMapping("/list-participant-by-class-id/{idEmploi}")
    public List<Participant> getListParticipantByClassId(@PathVariable long idEmploi){
        return groupe_service.ge_allBy_idClass(idEmploi);
    }



//    -----------------------get sum of reliquat in this current year
    @GetMapping("/montants-cunt")
    @Operation(summary = "Calculer les montant des etudiants (reliquat, scolarite)")
    public MontantsCunt sumReliquat(){
        return student_service.getAll_reliquat();
    }

//    --------------------cunt student inscrit and non inscrit
    @GetMapping("/student-count")
    public CuntStudentDTO countStudent(){
        return student_service.cunt_student_inscrit();
    }
//    -------------------------------------------------reincreiption method
    @PostMapping("/re-inscription/{idClasse}/{idAdmin}")
    public Object reInscription(@RequestBody Inscription student, @PathVariable long idClasse, @PathVariable long idAdmin){
            return student_service.reinscription(student, idClasse, idAdmin);
    }

    //    ---------------------------------------
    @PostMapping("add-doc")
    @Operation(summary = "Ajouter un document (rapport ou memoire)")
    public Object addDoc(@RequestBody DocDTO doc){
        return doc_service.addDoc(doc);
    }

    //    -------------------------------------------
    @GetMapping("all-docs")
    @Operation(summary = "Recuperer la liste des document")
    public List<Documents> getAllDocs(){
        return doc_service.getAllDocs();

    }

    // ----------------------------------------------
    @GetMapping("/all-docs-by-idAnnee/{idAnnee}")
    @Operation(summary = "Recuperer tous les docs avec idAnnee et id Classe")
    public Page<DocDTO> getAllDocsByIdClassAndIdAnnee(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable long idAnnee) {
        return doc_service.getByIdAnnee(page, size, idAnnee);
    }

    //---------------------------------------------

    @GetMapping("/default-docs-curent-year")
    @Operation(summary = "Recuperer les docs de l'annee en cours")
    public Page<DocDTO> getDefaultofYear(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return doc_service.defultCurrentDocs(page, size);
    }

    //------------------------------------------------------
    @GetMapping("/all-docs-by-idClasse/{idClasse}")
    @Operation(summary = "Recuperer tous les doccument de la classe")
    public List<DocDTO> getAllDocsByIdClasse(@PathVariable long idClasse) {
        return doc_service.getDocsByIdClass(idClasse);
    }

    //    -------------------------------------------
    @PostMapping("/add-soutenance")
    @Operation(summary = "Programer une soutenance")
    public Object addSoutenance(@RequestBody ProgramSoutenanceDto dto){
        return doc_service.addProgramSoutenance(dto);
    }

//    -------------------------------------
    @GetMapping("/all-soutenance-actif")
    @Operation(summary = "Recuperer la liste des soutenance programmer actif")
    public List<SoutenanceDTO> getAllSoutenanceActif(){
        return doc_service.getAllSoutenancesActive();
    }
    //-------------------------------------------
    @GetMapping("/memoire-number")
    @Operation(summary = "Recuperer les nombre de memoire ")
    public int countMemoire(){
        return doc_service.countMemoire();
    }

    //    ----------------
    @GetMapping("/rapport-number")
    @Operation(summary = "Recuperer les nombre de rapport ")
    public int countRapport(){
        return doc_service.countRapport();
    }

    //    ----------------------------
    @PostMapping("/students-import")
    @Operation(summary = "Ajout des etudiant du fichier excel importer")
    public Object addStudentImport(@RequestBody List<Inscription> students){

        return student_service.addStudentsImport(students);
    }

    //-------------------------------
    @GetMapping("get-list-student-by-idAnnee-and-idClasse/{idAnnee}/{idClasse}")
    @Operation(summary = "Recuperer la list des etudiant d'une classe par année avec l'idClass")
    public List<Inscription> getListStudentAnneeAndIdClasse(
            @PathVariable long idAnnee,
            @PathVariable long idClasse){
        return student_service.getListByIdAnneeAndIdClasse(idAnnee, idClasse);
    }
    //-----------------------------
    @GetMapping("/desaprouve-doc/{idDoc}")
    @Operation(summary = "Annuler une soutenance programer")
    public boolean desaprouveDoc(@PathVariable long idDoc){
        return doc_service.annulerProgramSoutenance(idDoc);
    }

    //-----------------------
    @PostMapping("/add-soutenance-note/{idDoc}")
    @Operation(summary = "Noter une soutenance par id doc")
    public Object addSoutenanceNote(@PathVariable long idDoc, @RequestBody double note){
        return doc_service.addSoutenanceNote(idDoc, note);
    }

    //-----------------------
    @GetMapping("get-student-by-etats/{value}")
    @Operation(summary = "Recuperer les etudiants par etat")
    public Page<Inscription> getAllStudentsByEtats(
            @PathVariable int value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return student_service.readAllByEtat(value,page, size);
    }
//    //------------------------
    @GetMapping("/change-state-inscription/{idInscription}/{idClasse}")
    @Operation(summary = "Changer l'etat de l'inscription par id inscription")
    public Object changeEtatById(@PathVariable long idInscription, @PathVariable long idClasse){
        return student_service.desabledInscription(idInscription, idClasse);
    }
}
