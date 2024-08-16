package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Services.Admin_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api-admin")
@RestController
public class Admin_controller {

    @Autowired
    private Admin_service adminService;

    @PostMapping("/add")
    public Object add(
            @RequestParam("admin") String adminString,
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
    @GetMapping("/list")
    public List<Admin> list() {
        return adminService.list_admin();
    }
}
