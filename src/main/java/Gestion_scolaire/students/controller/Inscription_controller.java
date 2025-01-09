package Gestion_scolaire.students.controller;

import Gestion_scolaire.students.dtos.DTO_scolarite;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.InscriptionDTO;
import Gestion_scolaire.students.entity.Inscription;
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
    private Student_service student_service;

    @Autowired
    private Groupe_service groupe_service;

    @Autowired
    private Doc_service doc_service;

    @Autowired
    private Inscription_service inscription_service;

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
            return inscription_service.add(dto, urlFile);
        }

        return inscription_service.add(dto, null);
    }



    @GetMapping("/subscribe-by-class-id/{idAnnee}/{idClasse}")
    @Operation(summary = "Recuperer la liste des etudians inscrits par id de la classe")
    public List<GetInscriptionDto> getStudentByClasse(@PathVariable long idAnnee, @PathVariable long idClasse){
        return student_service.get_by_classId(idAnnee,idClasse);
    }

    //get All Students By Group
    @GetMapping("/list-subscribe-by-group-id/{idGroup}")
    @Operation(summary = "Recuperer la liste des etudians inscrits par id du groupe")
    public List<Inscription> getAllStudentByGroup(@PathVariable long idGroup){
        return groupe_service.getAllStudentsByGroupId(idGroup);
    }

    @GetMapping("/list-subscribe-by-emploi-id/{idEmploi}")
    @Operation(summary = "Recuperer la liste des etudians inscrits par id du emplois")
    public List<Inscription> getAllByGroupes(@PathVariable long idEmploi){
        return groupe_service.getAllStudentsByGroupes(idEmploi);
    }

    @GetMapping("/annuler-depot/{idInscription}")
    @Operation(summary = "Annuler le depot de document d'un inscrit")
    public Object annulerDepot(@PathVariable long idInscription){
        return doc_service.annulerDepot(idInscription);
    }

    @GetMapping("/inscription-by-id/{idInscription}")
    @Operation(summary = "Recuperer l'inscription par son id")
    public GetInscriptionDto getInscriptionById(@PathVariable long idInscription){
      return   student_service.getInscriptionById(idInscription);
    }

    @GetMapping("/get-scolarite-and-reliquat-by/{idInscrit}")
    @Operation(summary = "Recuperer la scolarite payer et le reliquat ")
    public DTO_scolarite getScolariteByIdInscrit(@PathVariable long idInscrit){
        return inscription_service.getScolariteAndReliquatByIdIncrit(idInscrit);
    }

    @GetMapping("/all-subscribe-by-annee/{idAnnee}")
    @Operation(summary = "Recuperer tous les incrits de l'annee")
    public List<GetInscriptionDto> getAllSubscribeByAnnee(@PathVariable long idAnnee){
        return inscription_service.getAllInscritByYear(idAnnee);
    }

    @GetMapping("/all-subscribe-by-current-year")
    @Operation(summary = "Recuperer les inscrits de l'annee en cours")
    public List<GetInscriptionDto> getAllSubscribeByCurrentYear(){
        return inscription_service.getInscritCurrentYear();
    }
}
