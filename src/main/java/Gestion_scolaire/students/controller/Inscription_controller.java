package Gestion_scolaire.students.controller;

import Gestion_scolaire.Dto_classe.GetInputNoteInscritDTO;
import Gestion_scolaire.Niveaux_Filieres.dtos.SousFiliereDTO;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.students.dtos.*;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Paiement;
import Gestion_scolaire.students.services.Doc_service;
import Gestion_scolaire.Services.Groupe_service;
import Gestion_scolaire.students.services.Inscription_service;
import Gestion_scolaire.students.services.Student_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api-subscribe")
public class Inscription_controller {

    @Autowired
    private SharedControllers sharedControllers;

    @Autowired
    private Shared_methods_service shared_methods_service;


    @PostMapping("/add")
    public Object addStudent(
            @RequestParam("inscription") String studensString,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InscriptionDTO dto = objectMapper.readValue(studensString, InscriptionDTO.class);

        if(dto.getIdEtudiant().getEmail() == null || dto.getIdEtudiant().getEmail().isEmpty()){
            Random iud = new Random();
            int numb = iud.nextInt(100) +1;
            dto.getIdEtudiant().setEmail(dto.getIdEtudiant().getNom().toLowerCase().replaceAll("\\s+", "")+numb+"@gmail.com");

        }


        if (urlFile != null && !urlFile.isEmpty()) {
            return sharedControllers.getInscription_service().add(dto, urlFile);
        }

        return sharedControllers.getInscription_service().add(dto, null);
    }



//    @GetMapping("/subscribe-by-class-id/{idAnnee}/{idClasse}")
//    @Operation(summary = "Recuperer la liste des etudiants qui ont payer inscrits  par id de la classe ")
//    public List<GetInscriptionDto> getStudentByClasse(@PathVariable long idAnnee, @PathVariable long idClasse){
//        return sharedControllers.getStudent_service().get_by_classId(idAnnee,idClasse);
//    }

    //get All Students By Group
    @GetMapping("/list-subscribe-by-group-id/{idGroup}/{idEmploi}")
    @Operation(summary = "Recuperer la liste des etudians inscrits par id du groupe")
    public List<GetInputNoteInscritDTO> getAllStudentByGroup(@PathVariable long idGroup, @PathVariable long idEmploi){
        return sharedControllers.getGroupe_service().getAllStudentsByGroupId(idGroup, idEmploi);
    }

//    @GetMapping("/list-subscribe-by-emploi-id/{idEmploi}")
//    @Operation(summary = "Recuperer la liste des etudians inscrits par id du emplois")
//    public List<Inscription> getAllByGroupes(@PathVariable long idEmploi){
//        return sharedControllers.getGroupe_service().getAllStudentsByGroupes(idEmploi);
//    }

    @GetMapping("/get-incrits-by-idClasse/{idClasse}")
    @Operation(summary = "Recuperer les inscrits de la classe sans paginer")
    public List<Inscription> getAllIncritsOfClasse( @PathVariable long idClasse){
        return sharedControllers.getInscription_service().getAllInscritOfClasse(idClasse);
    }

    @GetMapping("/annuler-depot/{idInscription}")
    @Operation(summary = "Annuler le depot de document d'un inscrit")
    public Object annulerDepot(@PathVariable long idInscription){
        return sharedControllers.getDoc_service().annulerDepot(idInscription);
    }

    @GetMapping("/inscription-by-id/{idInscription}")
    @Operation(summary = "Recuperer l'inscription par son id")
    public GetInscriptionDto getInscriptionById(@PathVariable long idInscription){
      return   sharedControllers.getStudent_service().getInscriptionById(idInscription);
    }

    @GetMapping("/get-scolarite-and-reliquat-by/{idInscrit}")
    @Operation(summary = "Recuperer la scolarite payer et le reliquat ")
    public DTO_scolarite getScolariteByIdInscrit(@PathVariable long idInscrit){
        return sharedControllers.getInscription_service().getScolariteAndReliquatByIdIncrit(idInscrit);
    }

    @GetMapping("/all-subscribe-by-annee/{idAnnee}")
    @Operation(summary = "Recuperer tous les incrits de l'annee")
    public List<GetInscriptionDto> getAllSubscribeByAnnee(@PathVariable long idAnnee){
        return sharedControllers.getInscription_service().getAllInscritByYear(idAnnee);
    }

    @GetMapping("/all-subscribe-by-current-year")
    @Operation(summary = "Recuperer les inscrits de l'annee en cours")
    public List<GetInscriptionDto> getAllSubscribeByCurrentYear(){
        return sharedControllers.getInscription_service().getInscritCurrentYear();
    }

    @GetMapping("/add-inscit-to-sous-filiere/{idInscit}/{idSoufiliere}")
    @Operation(summary = "Ajout des inscrits a une sous filiere")
    public Object addInscritInSousFiliere(@PathVariable long idInscit, @PathVariable long idSoufiliere) {
        return  sharedControllers.getInscription_service().addInscrit_to_souFiliere(idInscit,idSoufiliere);
    }

    @GetMapping("/get-inscriptions-by-filiere-specialite/{idSouFiliere}")
    @Operation(summary = "Recuperer les inscrits par specialite de filiere")
    public SousFiliereDTO getAllInscriptionsByFiliereSpecialite(@PathVariable long idSouFiliere) {
        return shared_methods_service.getSousFilieresAndOurInscrits(idSouFiliere);
    }

    @GetMapping("/list-paiement-scolarite-by-idInscrit/{idInscrit}")
    @Operation(summary = "Recuperer la liste des paiement de scolarite par inscrit")
    public List<Paiement> getListPaiement (@PathVariable long idInscrit){
        return sharedControllers.getScolariteService().getListPaiementByIdInscrit(idInscrit);
    }

    @PutMapping("/update-paiement/{idPaiement}/{idAdmin}")
    @Operation(summary = "Modifier le montant du paiement par id paiement")
    public Object updatePaiement(@PathVariable long idPaiement, @RequestBody DTO_scolarite dtoScolarite, @PathVariable long idAdmin){
        return  sharedControllers.getScolariteService().updatePaiement(idPaiement, dtoScolarite, idAdmin);
    }

    // get student statistique
    @GetMapping("/statistique-of-current-year/{idAdmin}")
    @Operation(summary = "Recuperer les statistique de l'annee en cours")
    public StatistiqueDTO getCurrentYearStatistique(@PathVariable long idAdmin){
        return sharedControllers.getStudentStatistique_service().getCurrentYearStatistique(idAdmin);
    }

    @GetMapping("/statistique-by-id-annee/{idAnnee}/{idAdmin}")
    @Operation(summary = "Recuperer les statistique par annee avec idAnnee")
    public StatistiqueDTO getStatistiqueByIdYear(@PathVariable long idAnnee, @PathVariable long idAdmin){
        return sharedControllers.getStudentStatistique_service().getStatistiqueByIdAnnee(idAnnee, idAdmin);
    }

    @GetMapping("/inscriptions-by-filiere-and-ispaye/{idFiliere}/{idAdmin}/{idAnnee}/{isPaye}")
    @Operation(summary = "Recuperer les etudiants inscrit par filiere et etat de paiement")
    public List<FiliereStudentDTO> getStudentByIdFiliereAndPaye(@PathVariable long idFiliere, @PathVariable long idAdmin, @PathVariable long idAnnee, @PathVariable boolean isPaye){
        return sharedControllers.getStudentStatistique_service().getStudentByFilieresAndPaye(idFiliere, idAdmin, idAnnee, isPaye);
    }

    @GetMapping("/inscriptions-by-status-and-ispaye/{status}/{idAdmin}/{idAnnee}/{isPaye}")
    @Operation(summary = "Recuperer les etudiants inscrit par filiere et etat de paiement")
    public List<FiliereStudentDTO> getStudentByStatusAndPaye(@PathVariable String status, @PathVariable long idAdmin, @PathVariable long idAnnee, @PathVariable long isPaye){
        return sharedControllers.getStudentStatistique_service().getStudentByStatusAndPaye(status, idAdmin, idAnnee, isPaye);
    }

}
