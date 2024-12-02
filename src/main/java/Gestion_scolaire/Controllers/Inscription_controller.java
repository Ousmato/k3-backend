package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Inscription;
import Gestion_scolaire.Services.Doc_service;
import Gestion_scolaire.Services.Groupe_service;
import Gestion_scolaire.Services.Student_service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-subscribe")
public class Inscription_controller {
    @Autowired
    private Student_service student_service;

    @Autowired
    private Groupe_service groupe_service;

    @Autowired
    private Doc_service doc_service;


    @GetMapping("/subscribe-by-class-id/{idAnnee}/{idClasse}")
    @Operation(summary = "Recuperer la liste des etudians inscrits par id de la classe")
    public List<Inscription> getStudentByClasse(@PathVariable long idAnnee,@PathVariable long idClasse){
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
    public Inscription getInscriptionById(@PathVariable long idInscription){
      return   student_service.getInscriptionById(idInscription);
    }
}
