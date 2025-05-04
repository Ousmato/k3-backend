package Gestion_scolaire.Teachers.controller;

import Gestion_scolaire.Niveaux_Filieres.dtos.FiliereSpecialiteDto;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Teachers.dtos.SimpleTeacherDto;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.services.Specialite_service;
import Gestion_scolaire.Teachers.services.Teachers_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.util.FastList;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-teacher")
public class TeacherSpecialite_controller {

    @Autowired
    private Teachers_service teachers_service;

    @Autowired
    private Specialite_service specialite_service;

    @PostMapping("/add-specialite/")
    @PreAuthorize("hasAuthority('ROLE_DER')")
    @Operation(summary = "Ajouter une spécialité")
    public Object addSpecialite(@RequestBody FiliereSpecialiteDto dto) {
        return specialite_service.addSpecialite(dto.getSpecialite(), dto.getFilieres());

    }

    @GetMapping("/get-all-specialites")
    @Operation(summary = "Recuperer  tous les specialites")
    public List<Specialites> getAllSpecialites() {
        return specialite_service.getAllSpecialites();
    }

    @GetMapping("/get-filiere-specialite/")
    @Operation(summary = "Recuperer tous les specialites et leurs filieres")
    public  List<FiliereSpecialiteDto> getAllFilieresSpecialites() {
        return specialite_service.getAllFilieresSpecialite();
    }

    @PutMapping("/update-specialite/")
    @Operation(summary = "Modifier la specialite")
    public Object updateSpecialite(@RequestBody Specialites specialite) {
        return specialite_service.updateSpecialite(specialite);
    }

    @PostMapping("add-teacher-specialite/{idTeacher}/")
    @Operation(summary = "Ajouter les specialites pour un enseignant par sont id")
    public Object addTeacherSpecialite(@PathVariable int idTeacher, @RequestBody List<Specialites> specialites) {
        return specialite_service.addSpecialiteForTeacher(idTeacher, specialites);
    }


    @GetMapping("get-all-specialites-by-idTeacher/{idTeacher}")
    @Operation(summary = "Recuperer les specialites non associer a l'enseignant par son id")
    public List<Specialites> getAllSpecialitesByIdTeacher(@PathVariable int idTeacher) {
        return specialite_service.getAllSpecialiteNotAssociatedInByIdTeacher(idTeacher);
    }


}
