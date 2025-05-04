package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Administrators.repositories.Poste_repositorie;
import Gestion_scolaire.EnumClasse.RoleTypes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(Poste_repositorie poste_repositorie,
                               AdminRepositorie adminRepo,

                               PasswordEncoder passwordEncoder) {

        return args -> {
            // 1. Vérifier et créer le rôle SUPER_ADMIN si nécessaire
            String superAdminRoleName = "Super Administrateur";

            // Récupérer ou créer le rôle
            Postes superAdminRole = poste_repositorie.findByNom(superAdminRoleName);
            if (superAdminRole == null) {
                superAdminRole = new Postes();
                superAdminRole.setNom(superAdminRoleName);
                superAdminRole.setRoleType(RoleTypes.SUPER_ADMIN);
                superAdminRole.setDate(LocalDate.now());

                superAdminRole = poste_repositorie.save(superAdminRole); // Toujours enregistrer et réassigner
            }

            // Créer le SuperAdmin s'il n'existe pas
            String username = "Admin";
            if (!adminRepo.existsByNom(username)) {
                AdministrationUsers superAdmin = new AdministrationUsers();
                superAdmin.setNom(username);
                superAdmin.setIdPoste(superAdminRole); // ici on est sûr que ce n’est pas null
                superAdmin.setPrenom("Admin");
                superAdmin.setPassword(passwordEncoder.encode("Admin123@"));
                superAdmin.setEmail("ousmato98@gmail.com");
                superAdmin.setActive(true);
                superAdmin.setSexe("Admin");
                superAdmin.setUrlPhoto("default.jpg");
                superAdmin.setUpdateDate(LocalDate.now());

                System.out.println("------------super admin " + superAdmin);
                adminRepo.save(superAdmin);
            }
        };
    }
}
