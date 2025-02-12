package Gestion_scolaire.Emplois.controllers;

import Gestion_scolaire.Classes.dtos.StudentGroupDto;
import Gestion_scolaire.Shareds.SharedControllers;
import Gestion_scolaire.students.entity.Participant;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groupe-api/")
public class StudentGroupController {

    @Autowired
    private SharedControllers sharedControllers;

    @GetMapping("get-all-groupes-of-classe/{idClasse}")
    @Operation(summary = "Recuperer les groupe de la classe")
    public List<StudentGroupDto> getAllStudentGroupByIdClasse(@PathVariable long idClasse){
        return sharedControllers.getStudentGroupServices().ge_allBy_idClass(idClasse);
    }

    @GetMapping("get-all-participants-of-classe/{idClasse}")
    @Operation(summary = "Recuperer les groupe de la classe")
    public List<Participant> getAllParticipantByIdClasse(@PathVariable long idClasse){
        return sharedControllers.getStudentGroupServices().ge_allParticipantBy_idClass(idClasse);
    }

    @GetMapping("get-participants-of-group/{idGroup}/{idEmploi}")
    @Operation(summary = "Recuperer les participants du  groupe")
    public StudentGroupDto getAllParticipantsOfGrp(@PathVariable long idGroup, @PathVariable long idEmploi){
        return sharedControllers.getStudentGroupServices().getAllParticipantsOfGrp(idGroup, idEmploi);
    }

}
