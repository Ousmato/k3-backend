package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.AdminDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Services.Admin_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api-admin")
@RestController
public class Admin_controller {

    @Autowired
    private Admin_service adminService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_DG')")
    public Object add(
           @RequestParam("admin" ) String adminString,
            @RequestParam(value = "file", required = false)MultipartFile file) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Admin a = objectMapper.readValue(adminString, Admin.class);
        // Vérifie si urlFile est null ou vide
        if (file == null || file.isEmpty()) {
            // Si pas de fichier photo, ajoute l'enseignant sans spécifier de photo
            return adminService.add(a, null);
        } else {
            // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
            return adminService.add(a, file);

        }
    }

//    ------------------------------------get all admin
    @GetMapping("/administrateurs-actifs")
    @PreAuthorize("hasAuthority('ROLE_DG')")
    public List<Admin> list() {
        return adminService.list_admin();
    }

    @GetMapping("/administrateurs/{value}")
    @PreAuthorize("hasAuthority('ROLE_DG')")
    @Operation(summary = "Recuperer les admins par etat")
    public List<Admin> list(@PathVariable long value) {
        return adminService.getAllByEtat(value);
    }

    @GetMapping("/change-etat/{idAdmin}")
    @PreAuthorize("hasAuthority('ROLE_DG')")
    @Operation(summary = "changer l'etat de l'admin par id")
    public Object changeEtat(@PathVariable long idAdmin) {
        return adminService.chageEtatByIdAdmin(idAdmin);
    }

    // getAdmin by id
    @GetMapping("/administrateur/{idAdmin}")
    @Operation(summary = "Recuperer l'admin par id")
    public Object getById(@PathVariable long idAdmin) {
        return adminService.getAdminBy(idAdmin);
    }

    // -------change profil image
    @PutMapping("change-photo/{idAdmin}")
    @Operation(summary = "Changer la photo de l'admin par son id")
    public Object changePhoto(@PathVariable long idAdmin, @RequestParam(value = "file") MultipartFile file) throws Exception {

        // Vérifie si urlFile est null ou vide
        if (file == null || file.isEmpty()) {
            // Si pas de fichier photo, ajoute l'enseignant sans spécifier de photo
            return DTO_response_string.fromMessage("Image est invalide", 400);
        } else {
            // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
            return adminService.changeImage(idAdmin,file);

        }
    }


    //------------------------update
    @PutMapping("/update-admin")
    @PreAuthorize("hasAuthority('ROLE_DG')")
    @Operation(summary = "Modifier les information de l'admin")
    public Object updateAdmin(@RequestBody AdminDTO admin) {
        return adminService.updatAdmin(admin);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Recuperer l'utilisateur par email pour reunitialiser")
    public Admin forgotPassword(@RequestBody String email) {
        return adminService.forgotPassword(email);
    }

    @PostMapping("/validate-token")
    public boolean validateToken(HttpSession session, @RequestBody Map<String, String> request) {

        String sessionToken = (String) session.getAttribute("resetToken");
        String token = request.get("token");
        System.out.println("Session token : " + sessionToken);
        System.out.println("Token reçu : " + token);
        return sessionToken != null && sessionToken.equals(token);
    }
}
