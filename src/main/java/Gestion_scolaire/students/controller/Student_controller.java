package Gestion_scolaire.students.controller;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.students.dtos.*;
import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.students.services.Doc_service;
import Gestion_scolaire.Services.Groupe_service;
import Gestion_scolaire.students.services.Student_service;
import Gestion_scolaire.students.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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


    //    ----------------------------------------method get all students----------------------------------
    @GetMapping("/list")
    @Operation(summary = "Recuperer la liste des etudiant de l'annee en cours")

    public Page<GetInscriptionDto> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return student_service.readAll(page, size);
    }
//    ----------------------------methode find all student
    @GetMapping("/find-all")
    public List<Students> getAllStudents() {
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
    public Page<GetInscriptionDto> AllStudentClass(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size,
            @PathVariable long idClasse){
        return student_service.readAllByClassId(page, size, idClasse);

    }
//    ------------------------methode call studend by id------------------------------
    @GetMapping("/student-by-id/{idStudent}")
    public Students getStudent(@PathVariable long idStudent){
        return student_service.studenById(idStudent);

    }

//    --------------------------update scolarite
    @PutMapping("/update-scolarite/{idStudent}/{idAdmin}")
    public Object updateScolarite(@PathVariable long idStudent, @PathVariable long idAdmin, @RequestBody DTO_scolarite dtoScolarite){
        return student_service.update_scolarite(idStudent, idAdmin, dtoScolarite.getScolarite());
    }

    //get all student by id annee scolaire
    @GetMapping("/student-by-anneScolaire-id/{idAnne}")
    public Page<GetInscriptionDto> getListByIaAnne(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size,
            @PathVariable long idAnne){
        return student_service.get_by_idAnneeScolaire(page, size, idAnne);
    }

//    -------------------------------------------------------------------------------

    @GetMapping("/find-all-groupe")
    public  List<StudentGroupe> getAll_group(){
        return groupe_service.findAll();
    }

//    -----------------------------------add group
    @PostMapping("/add-group/{idEmploi}")
    public Object addGroup(@PathVariable long idEmploi, @RequestBody String name){
        return groupe_service.add_group(idEmploi, name);
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
    public Object reInscription(@RequestBody List<Inscription> students, @PathVariable long idClasse, @PathVariable long idAdmin){
           for (Inscription inscrit : students){
               String statusStr = inscrit.getIdEtudiant().getStatus().toString();

               System.out.println("-------------" + statusStr);
               if (statusStr.contains(" ")) {
                   System.out.println("--------------Le nom contient des espaces--------------");

                   // Remplace les espaces par des underscores
                   String correctedStatus = statusStr.replaceAll(" ", "_");

                   try {
                       // Valide et affecte la valeur corrigée
                       Type_status validStatus = Type_status.valueOf(correctedStatus);
                       inscrit.getIdEtudiant().setStatus(validStatus);
                       System.out.println("--------------+ " + validStatus);
                   } catch (IllegalArgumentException e) {
                       System.err.println("Erreur : Le statut corrigé \"" + correctedStatus + "\" n'est pas valide pour Type_status.");
                       // Gérez le cas où le statut corrigé est invalide (par exemple, en le journalisant ou en lançant une exception)
                   }
               }
           }
            return student_service.reinscription(students, idClasse, idAdmin);
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
    public Object addStudentImport(@Validated @RequestBody List<Inscription> students){
        System.out.println("------------------------------" + students.size());
//        try {
            for (Inscription student : students) {

                if(student.getIdEtudiant().getEmail() == null || student.getIdEtudiant().getEmail().isEmpty()){
                    Random iud = new Random();
                    int numb = iud.nextInt(100) +1;
                    student.getIdEtudiant().setEmail(student.getIdEtudiant().getNom().toLowerCase().replaceAll("\\s+", "")+numb+"@gmail.com");

                }


            }
            return student_service.addStudentsImport(students);

//        }catch (Exception e){
//            log.error("Erreur lors de l'importation des étudiants : {}", e.getMessage(), e);
//            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
//        }

    }

    //-------------------------------
    @GetMapping("get-list-student-by-idAnnee-and-idClasse/{idAnnee}/{idClasse}")
    @Operation(summary = "Recuperer la list des etudiant d'une classe par année avec l'idClass")
    public List<GetInscriptionDto> getListStudentAnneeAndIdClasse(
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
    public Page<GetInscriptionDto> getAllStudentsByEtats(
            @PathVariable int value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return student_service.readAllByEtat(value,page, size);
    }
//    //------------------------
    @GetMapping("/change-state-inscription/{idInscription}/{idClasse}")
    @Operation(summary = "Changer l'etat de l'inscription par id inscription")
    public Object changeEtatById(@PathVariable long idInscription, @PathVariable long idClasse){
        return student_service.desabledInscription(idInscription, idClasse);
    }
}
