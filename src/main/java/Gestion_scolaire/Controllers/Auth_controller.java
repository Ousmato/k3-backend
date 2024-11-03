package Gestion_scolaire.Controllers;

import Gestion_scolaire.Authentification.Auth;
import Gestion_scolaire.Authentification.Jeton_Auth;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Services.Admin_service;
import Gestion_scolaire.Services.InfoScool_service;
import Gestion_scolaire.Services.PromotionAutomaticAdd_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.configuration.SecurityConfigs.JwtService;
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
    private Admin_service adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private InfoScool_service infoScool_service;

    @Autowired
    private PromotionAutomaticAdd_service promotionAutomaticAdd_service;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        System.out.println("entre en methode" + loginRequest);
        Object userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        System.out.println("es ce que tu es retourne "+ userDetails);
        if (userDetails != null) {
            ObjectMapper adminMapper = new ObjectMapper();
            adminMapper.registerModule(new JavaTimeModule());
            Admin admin = adminMapper.convertValue(userDetails, Admin.class);
            String token = jwtService.generateToken(admin.getEmail());
            System.out.println("------------------token---------------" +token);
            System.out.println("------------------user---------------" +userDetails);

            return new LoginResponse(userDetails, token);
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

    @PostMapping("/refresh-token")
    @Operation(summary = "Racfrechire le token")
    public Object refreshToken(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String token = jwtService.generateToken(email);

        // Créer une réponse JSON avec le token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
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

        public LoginResponse(Object userDetails, String token) {
            this.user = userDetails;
            this.token = token;
        }
    }
}