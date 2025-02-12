package Gestion_scolaire.students.controller;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.students.dtos.*;
import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.students.entity.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api-student")
@RequiredArgsConstructor
public class Student_controller {


    private final SharedControllers sharedControllers;

    //    ----------------------------------------method get all students----------------------------------
    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des etudiant de l'annee en cours")

    public Page<GetInscriptionDto> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return sharedControllers.getStudent_service().readAll(page, size);
    }
//    ----------------------------methode find all student
    @GetMapping("/find-all")
    public List<Students> getAllStudents() {
        return sharedControllers.getStudent_service().find_all();
    }
    //method update----------------------------------------
    @PutMapping("/update")
    private Object update(
            @RequestParam("student") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Inscription inscrit = objectMapper.readValue(studensString, Inscription.class);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        // Vérifie si urlFile est null ou vide
        if (urlFile == null) {

            System.out.println("file is ");
            return sharedControllers.getStudent_service().update(inscrit, null);
        }
            return sharedControllers.getStudent_service().update(inscrit, urlFile);

    }
    //method disabled student-----------------------
    @GetMapping("/desable/{idStudent}")
    public Object delete(@PathVariable long idStudent){
        return sharedControllers.getStudent_service().desable(idStudent);

    }
//    ------------------------method get all student in classe------------------
    @GetMapping("/list-student-by-classe/{idClasse}")
    public Page<GetInscriptionDto> AllStudentClass(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size,
            @PathVariable long idClasse){
        return sharedControllers.getStudent_service().readAllByClassId(page, size, idClasse);

    }
//    ------------------------methode call studend by id------------------------------
    @GetMapping("/student-by-id/{idStudent}")
    public Students getStudent(@PathVariable long idStudent){
        return sharedControllers.getStudent_service().studenById(idStudent);

    }

    //update scolarite
    @PutMapping("/update-scolarite/{idAdmin}")
    @Operation(summary = "Paiement de scolarite d'un inscrit par id")
    public Object updateScolarite(@RequestBody DTO_scolarite dtoScolarite,  @PathVariable long idAdmin){
        return sharedControllers.getStudent_service().update_scolarite(dtoScolarite, idAdmin);
    }

    //get all student by id annee scolaire
    @GetMapping("/student-by-anneScolaire-id/{idAnne}")
    public Page<GetInscriptionDto> getListByIaAnne(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size,
            @PathVariable long idAnne){
        return sharedControllers.getStudent_service().get_by_idAnneeScolaire(page, size, idAnne);
    }

//    -------------------------------------------------------------------------------

    @GetMapping("/find-all-groupe")
    public  List<StudentGroupe> getAll_group(){
        return  sharedControllers.getGroupe_service().findAll();
    }

//    -----------------------------------add group
    @PostMapping("/add-group/{idEmploi}")
    public Object addGroup(@PathVariable long idEmploi, @RequestBody String name){
        return  sharedControllers.getGroupe_service().add_group(idEmploi, name);
    }

    @GetMapping("/list-group-by-idEmploi/{idEmplois}")
    public List<StudentGroupe> getListGroupByEmploi(@PathVariable long idEmplois){
        return  sharedControllers.getGroupe_service().listGroupByIdEmploi(idEmplois);
    }

//    ----------------------------------------------------------------------

    @PostMapping("/add-more-participant")
    public Object addMoreParticipant(@RequestBody List<Participant> participants){
        return  sharedControllers.getGroupe_service().add_participant(participants);
    }

//    --------------get all participant by emplois id
    @GetMapping("/list-participant-by-class-id/{idEmploi}")
    public List<Participant> getListParticipantByClassId(@PathVariable long idEmploi){
        return sharedControllers.getGroupe_service().ge_allBy_idClass(idEmploi);
    }



//    -----------------------get sum of reliquat in this current year
    @GetMapping("/montants-cunt")
    @Operation(summary = "Calculer les montant des etudiants (reliquat, scolarite)")
    public MontantsCunt sumReliquat(){
        return sharedControllers.getStudent_service().getAll_reliquat();
    }

//    -----cunt student inscrit and non inscrit
    @GetMapping("/student-count")
    public CuntStudentDTO countStudent(){
        return sharedControllers.getStudent_service().cunt_student_inscrit();
    }
//    -------------------------------------------------reincreiption method
    @PostMapping("/re-inscription/{idClasse}/{idAdmin}")
    public Object reInscription(@RequestBody List<Long> ids, @PathVariable long idClasse, @PathVariable long idAdmin){


        List<Students> students = sharedControllers.getStudent_service().getStudentsNotInscribed(ids, idClasse);

        for (Students student : students){
               String statusStr = student.getStatus().toString();


               if (statusStr.contains(" ")) {

                   // Remplace les espaces par des underscores
                   String correctedStatus = statusStr.replaceAll(" ", "_");

                   try {
                       // Valide et affecte la valeur corrigée
                       Type_status validStatus = Type_status.valueOf(correctedStatus);
                       student.setStatus(validStatus);

                   } catch (IllegalArgumentException e) {
                       System.err.printf("Erreur : Le statut corrigé \"%s\" n'est pas valide pour Type_status.%n", correctedStatus);
                       // Gérez le cas où le statut corrigé est invalide (par exemple, en le journalisant ou en lançant une exception)
                   }
               }
           }
            return sharedControllers.getInscription_service().reinscription(students, idClasse, idAdmin);
        }
    //    ---------------------------------------
    @PostMapping("add-doc")
    @Operation(summary = "Ajouter un document (rapport ou memoire)")
    public Object addDoc(@RequestBody DocDTO doc){
        return sharedControllers.getDoc_service().addDoc(doc);
    }

    //    -------------------------------------------
    @GetMapping("all-docs")
    @Operation(summary = "Recuperer la liste des document")
    public List<Documents> getAllDocs(){
        return sharedControllers.getDoc_service().getAllDocs();

    }

    // ----------------------------------------------
    @GetMapping("/all-docs-by-idAnnee/{idAnnee}")
    @Operation(summary = "Recuperer tous les docs avec idAnnee et id Classe")
    public Page<DocDTO> getAllDocsByIdClassAndIdAnnee(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable long idAnnee) {
        return sharedControllers.getDoc_service().getByIdAnnee(page, size, idAnnee);
    }

    //---------------------------------------------

    @GetMapping("/default-docs-curent-year")
    @Operation(summary = "Recuperer les docs de l'annee en cours")
    public Page<DocDTO> getDefaultofYear(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return sharedControllers.getDoc_service().defultCurrentDocs(page, size);
    }

    //------------------------------------------------------
    @GetMapping("/all-docs-by-idClasse/{idClasse}")
    @Operation(summary = "Recuperer tous les doccument de la classe")
    public List<DocDTO> getAllDocsByIdClasse(@PathVariable long idClasse) {
        return sharedControllers.getDoc_service().getDocsByIdClass(idClasse);
    }

    //    -------------------------------------------
    @PostMapping("/add-soutenance")
    @Operation(summary = "Programer une soutenance")
    public Object addSoutenance(@RequestBody ProgramSoutenanceDto dto){
        return sharedControllers.getDoc_service().addProgramSoutenance(dto);
    }

//    -------------------------------------
    @GetMapping("/all-soutenance-actif")
    @Operation(summary = "Recuperer la liste des soutenance programmer actif")
    public List<SoutenanceDTO> getAllSoutenanceActif(){
        return sharedControllers.getDoc_service().getAllSoutenancesActive();
    }
    //-------------------------------------------
    @GetMapping("/memoire-number")
    @Operation(summary = "Recuperer les nombre de memoire ")
    public int countMemoire(){
        return sharedControllers.getDoc_service().countMemoire();
    }

    //    ----------------
    @GetMapping("/rapport-number")
    @Operation(summary = "Recuperer les nombre de rapport ")
    public int countRapport(){
        return sharedControllers.getDoc_service().countRapport();
    }

    //    ----------------------------
    @PostMapping("/students-import")
    @Operation(summary = "Ajout des etudiant du fichier excel importer")
    public Object addStudentImport(@Validated @RequestBody List<Inscription> students){

//        try {
            for (Inscription student : students) {

                if(student.getIdEtudiant().getEmail() == null || student.getIdEtudiant().getEmail().isEmpty()){
                    Random iud = new Random();
                    int numb = iud.nextInt(100) +1;
                    student.getIdEtudiant().setEmail("%s%d@gmail.com".formatted(student.getIdEtudiant().getNom().toLowerCase().replaceAll("\\s+", ""), numb));

                }


            }
            return sharedControllers.getStudent_service().addStudentsImport(students);

    }

    //-------------------------------
    @GetMapping("get-list-student-by-idAnnee-and-idClasse/{idAnnee}/{idClasse}")
    @Operation(summary = "Recuperer la list des etudiant d'une classe par année avec l'idClass")
    public List<GetInscriptionDto> getListStudentAnneeAndIdClasse(
            @PathVariable long idAnnee,
            @PathVariable long idClasse){
        return sharedControllers.getStudent_service().getListByIdAnneeAndIdClasse(idAnnee, idClasse);
    }
    //-----------------------------
    @GetMapping("/desaprouve-doc/{idDoc}")
    @Operation(summary = "Annuler une soutenance programer")
    public boolean desaprouveDoc(@PathVariable long idDoc){
        return sharedControllers.getDoc_service().annulerProgramSoutenance(idDoc);
    }

    //-----------------------
    @PostMapping("/add-soutenance-note/{idDoc}")
    @Operation(summary = "Noter une soutenance par id doc")
    public Object addSoutenanceNote(@PathVariable long idDoc, @RequestBody double note){
        return sharedControllers.getDoc_service().addSoutenanceNote(idDoc, note);
    }

    //-----------------------
    @GetMapping("get-student-by-etats/{value}")
    @Operation(summary = "Recuperer les etudiants par etat")
    public Page<GetInscriptionDto> getAllStudentsByEtats(
            @PathVariable int value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return sharedControllers.getStudent_service().readAllByEtat(value,page, size);
    }
//    //------------------------
    @GetMapping("/change-state-inscription/{idInscription}/{idClasse}")
    @Operation(summary = "Changer l'etat de l'inscription par id inscription")
    public Object changeEtatById(@PathVariable long idInscription, @PathVariable long idClasse){
        return sharedControllers.getStudent_service().desabledInscription(idInscription, idClasse);
    }


}
