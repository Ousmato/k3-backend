package Gestion_scolaire.Controllers;

import Gestion_scolaire.Authentification.Auth;
import Gestion_scolaire.Authentification.Jeton_Auth;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.UsersAbstract;
import Gestion_scolaire.Services.InfoScool_service;
import Gestion_scolaire.Services.PromotionAutomaticAdd_service;
import Gestion_scolaire.configuration.NoteFundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/Auth")
public class Auth_controller {

    @Autowired
    private Jeton_Auth jetonAuth;

    @Autowired
    private Auth authService;

    @Autowired
    private InfoScool_service infoScool_service;

    @Autowired
    private PromotionAutomaticAdd_service promotionAutomaticAdd_service;

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        Object userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (userDetails != null) {
            String token = jetonAuth.generateToken(userDetails);
            return userDetails;
        } else {
            throw  new NoteFundException("Address mail ou mot de passe est incorrect");
        }
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
    @PostMapping("/add-annee-scolaire")
    public Object add_annee_scolaire(@RequestBody AnneeScolaire scolaire){
        return promotionAutomaticAdd_service.autoPromotionForAllStudentClasses(scolaire);
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
//    ---------------------------------------


    @Data
    static class LoginRequest {
        private String email;
        private String password;

    }
}