package Gestion_scolaire.Controllers;

import Gestion_scolaire.Authentification.Auth;
import Gestion_scolaire.Authentification.Jeton_Auth;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.UsersAbstract;
import Gestion_scolaire.Services.InfoScool_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        Object userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (userDetails != null) {
            String token = jetonAuth.generateToken(userDetails);
            return userDetails;
        } else {
            return "Invalid email or password";
        }
    }
//    ----------------------------------------methode get admin
    @GetMapping("/read-info-school")
    public InfoSchool getInfo(){

         return  infoScool_service.getInfo();
    }

//    ----------------------------------methode update
    @PutMapping("/update")
    public InfoSchool update(
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
//    ---------------------------------------

//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
//        System.out.println(loginRequest.getEmail());
//        Object authenticatedUser = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//        if (authenticatedUser != null) {
//            System.out.println(authenticatedUser);
//            return ResponseEntity.ok(authenticatedUser);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//        }
//    }
//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestParam("email") String email, @RequestParam("password") String password) {
//        System.out.println(email);
//        Object authenticatedUser = authService.authenticate(email, password);
//        if (authenticatedUser != null) {
//            System.out.println(authenticatedUser);
//            return ResponseEntity.ok(authenticatedUser);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//        }
//    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;

    }
}