package Gestion_scolaire.Administrators.controller;

import Gestion_scolaire.Administrators.dtos.AdminDTO;
import Gestion_scolaire.Administrators.dtos.AdminPostesDTO;
import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.Administrators.services.Roles_services;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.services.Admin_service;
import Gestion_scolaire.Models.UsersGrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/api-admin")
@RestController
public class Admin_controller {

    @Autowired
    private Admin_service adminService;

    @Autowired
    private Roles_services roles_services;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public Object add(
           @RequestParam("admin" ) String adminString,
            @RequestParam(value = "file", required = false)MultipartFile file) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        AdministrationUsers a = objectMapper.readValue(adminString, AdministrationUsers.class);

        // Vérifie si urlFile est null ou vide
        if (file == null || file.isEmpty()) {
            // Si pas de fichier photo, ajoute l'enseignant sans spécifier de photo
            return adminService.add(a, null);
        } else {
            // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
            return adminService.add(a, file);

        }
    }

    //get all admin
    @GetMapping("/administrateurs-actifs")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public List<AdminPostesDTO> list() {
        return roles_services.list_admin();
    }
//
    @GetMapping("/administrateurs/{value}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    @Operation(summary = "Recuperer les admins par etat")
    public List<AdminPostesDTO> list(@PathVariable long value) {
        return roles_services.getAllByEtat(value);
    }

    @GetMapping("/change-etat/{idAdmin}")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
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
            return DTO_response_string.fromMessage("Image est invalide");
        } else {
            // Si un fichier photo est fourni, ajoute l'enseignant avec la photo
            return adminService.changeImage(idAdmin,file);

        }
    }


    //------------------------update
    @PutMapping("/update-admin")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    @Operation(summary = "Modifier les information de l'admin")
    public Object updateAdmin(@RequestBody AdministrationUsers admin) {
        return adminService.updatAdmin(admin);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Recuperer l'utilisateur par email pour reunitialiser")
    public AdministrationUsers forgotPassword(@RequestBody String email) {
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

    @GetMapping("List-roles/{idAdmin}")
    @Operation(summary = "Recuperer la liste des roles ")
    public List<Postes> getAllRoles(@PathVariable long idAdmin) {
        return roles_services.getAllRoles(idAdmin);
    }

    @GetMapping("get-all-grades/{idAdmin}")
    @Operation(summary = "Recuperer la liste des grades")
    public List<UsersGrade> getAllUserGrades(@PathVariable long idAdmin) {
        return roles_services.getAllGdrades(idAdmin);
    }

    @PostMapping("add-poste/{idAdmin}")
    @Operation(summary = "Ajouter un role")
    public Object addPoste(@RequestBody Postes poste, @PathVariable long idAdmin) {
        return roles_services.addPoste(poste, idAdmin);
    }

    @PutMapping("/update-role")
    @Operation(summary = "Modifier le role ")
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    public Object updateRole(@RequestBody Postes role) {
        System.out.println("---------------------role controller :" + role);
        return roles_services.updateRole(role);
    }

//    @DeleteMapping("deleted-role/{idRole}")
//    @Operation(summary = "Suprimer un role par son id")
//    @PreAuthorize("hasAuthority('ROLE_Admin')")
//    public Object deleteRole(@PathVariable long idRole) {
//        return roles_services.deletedRole(idRole);
//    }
//
//    @GetMapping("/add-poste/{idCurrentAdmin}/{idRole}")
//    @Operation(summary = "Ajouter un poste pour un admin")
//    @PreAuthorize("hasAuthority('ROLE_Admin')")
//    public Object addPoste(@PathVariable long idCurrentAdmin, @PathVariable long idRole){
//        return roles_services.Addposte(idCurrentAdmin, idRole);
//    }
//
//    @GetMapping("/get-roles-of-post-by-idAdmin/{idAdmin}")
//    @Operation(summary = "Recuperer les poste associer a l'admin par son id")
//    public List<AdminPostesDTO> getAllPostes(@PathVariable long idAdmin) {
//        return roles_services.getAllPoste(idAdmin);
//    }

    @PostMapping("add-grade/{idAdmin}")
    @Operation(summary = "Ajouter un role")
    public Object addGrade(@RequestBody UsersGrade grade, @PathVariable long idAdmin) {
        return roles_services.addUserGrade(grade, idAdmin);
    }

}
