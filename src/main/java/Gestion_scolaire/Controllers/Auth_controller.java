package Gestion_scolaire.Controllers;

import Gestion_scolaire.Authentification.Auth;
import Gestion_scolaire.Authentification.Jeton_Auth;
import Gestion_scolaire.Models.UsersAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Auth")
public class Auth_controller {

    @Autowired
    private Jeton_Auth jetonAuth;

    @Autowired
    private Auth authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Object userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (userDetails != null) {
            String token = jetonAuth.generateToken(userDetails);
            // Ajouter le jeton à la réponse
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

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