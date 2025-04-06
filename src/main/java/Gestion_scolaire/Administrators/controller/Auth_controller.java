package Gestion_scolaire.Administrators.controller;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Administrators.services.Auth_service;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Administrators.services.Admin_service;
import Gestion_scolaire.Services.InfoScool_service;
import Gestion_scolaire.Classes.services.PromotionAutomaticAdd_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.configuration.SecurityConfigs.JwtService;
import Gestion_scolaire.students.entity.Students;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Auth")
public class Auth_controller {

    @Autowired
    private Auth_service authServiceService;

    @Autowired
    private Admin_service adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private InfoScool_service infoScool_service;

    @Autowired
    private PromotionAutomaticAdd_service promotionAutomaticAdd_service;

    @Transactional
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        System.out.println("entre en methode" + loginRequest);
        Object userDetails = authServiceService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (userDetails == null) {
            throw new NoteFundException("Adresse mail ou mot de passe incorrect");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String token;
        String refreshToken;
        if (userDetails instanceof Admin) {
            Admin admin = mapper.convertValue(userDetails, Admin.class);
            token = jwtService.generateToken(admin.getEmail());
            refreshToken = jwtService.generateRefreshToken(admin.getEmail());
            adminService.addRefreshToken(admin, refreshToken);
        } else if (userDetails instanceof Students) {
            Students students = mapper.convertValue(userDetails, Students.class);
            token = jwtService.generateToken(students.getEmail());
            refreshToken = jwtService.generateRefreshToken(students.getEmail());
            adminService.addRefreshTokenStudent(students, refreshToken);
        } else {
            throw new NoteFundException("Utilisateur non reconnu");
        }

        LoginResponse response = new LoginResponse(userDetails, token, refreshToken);
        System.out.println("Réponse envoyée: " + response);  // <-- Vérifie ici si le JSON est correct
        return response;
    }
//    ----------------------------------------methode get admin
    @GetMapping("/read-info-school")
    public InfoSchool getInfo(){

         return  infoScool_service.getInfo();
    }

//    ----------------------------------methode update
    @PutMapping("/update")
    public Object update(
            @RequestParam("InfoSchool") String infoScool,
            @RequestParam(value = "file", required = false) MultipartFile urlFile) throws IOException {
//        System.out.println("------------------" + urlFile.getOriginalFilename() + "-----------------------------------------");

            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            InfoSchool inf = objectMapper.readValue(infoScool, InfoSchool.class);
//            log.info("Store JSON converted: {}", inf);

        if (urlFile != null && !urlFile.isEmpty()) {
            return infoScool_service.update(inf, urlFile);
        } else {
            System.out.println("URL file is empty");
            // Traitement si aucun fichier n'est téléchargé
            return infoScool_service.update(inf, null); // Vous devez implémenter la gestion de cas sans fichier dans votre service
        }
    }
//    ----------------------------add annee scolaire
    @PostMapping("/add-annee-scolaire/{idAdmin}")
    public Object add_annee_scolaire(@RequestBody AnneeScolaire scolaire, @PathVariable long idAdmin){
        return promotionAutomaticAdd_service.createSchoolYearAndClassesWithUes(scolaire, idAdmin);
    }

//    ---------------------------get all annee scolaire
    @GetMapping("/get-all-annee")
    public List<AnneeScolaire> getAllAnnee(){
        return infoScool_service.readAll_anne();
    }
//    -----------------------update annee scolaire
    @PutMapping("/updat-anne-scolaire")
    public Object update_anne_scolaire(@RequestBody AnneeScolaire scolaire){
        return infoScool_service.update_AnneeScolaire(scolaire);
    }

//    ----------------------delete annee scolaire
    @DeleteMapping("/delete-annee-scolaire/{idAnnee}")
    public Object delete_annee_scolaire(@PathVariable long idAnnee){
        return infoScool_service.delete_annee(idAnnee);
    }

    @GetMapping("/get-all-annee-scolaire-have-emploi-for-teacher/{idTeacher}")
    @Operation(summary = "Recuperer les annees scolaire dans lequelles l'enseignant a donnees cours par son id")
    public List<AnneeScolaire> getAllAnneeOfTeacherEmploiByIdTeacher(@PathVariable long idTeacher){
        return infoScool_service.getAllAnneeOfTeacherEmploiByIdTeacher(idTeacher);
    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;

    }

    @Data
    static class LoginResponse {
        private String token;
        private Object user;
        private String refreshToken;

        public LoginResponse(Object userDetails, String token, String refreshToken) {
            this.user = userDetails;
            this.token = token;
            this.refreshToken = refreshToken;

        }
    }
}