package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.dtos.AdminPostesDTO;
import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Administrators.entity.Poste;
import Gestion_scolaire.Administrators.entity.Roles;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Administrators.repositories.Poste_repositorie;
import Gestion_scolaire.Administrators.repositories.Role_repositorie;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class Roles_services {

    @Autowired
    private Role_repositorie role_repositorie;

    @Autowired
    private AdminRepositorie admin_repositorie;

    @Autowired
    private Validator validator;

    @Autowired
    private MessaSender messages;

    @Autowired
    private Shared_methods_service sharedService;


    @Autowired
    private Poste_repositorie poste_repositorie;

    String schoolEmail = "ousmatotoure98@gmail.com";


    public Object addRole(Roles role, long idAdmin) {
        Roles roleExist = role_repositorie.findByNomAndTypeFiliere(role.getNom(), role.getTypeFiliere());
        if (roleExist != null) {
            throw new NoteFundException("Le role exist dejà");
        }
        Roles newRole = new Roles();
        newRole.setNom(role.getNom().toUpperCase());
        newRole.setTypeFiliere(role.getTypeFiliere());
        Set<ConstraintViolation<Roles>> violations = validator.validate(role);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Admin admin = admin_repositorie.getByIdAdministraAndActive(idAdmin, true);
        if (admin == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        String nomRole = sharedService.abrevigateName(admin.getIdRole().getNom());
        if(!sharedService.abrevigateName(admin.getIdRole().getNom()).equalsIgnoreCase(nomRole)){
            throw new NoteFundException("Vous n'êtes pas autorisé");
        }

        role.setIdAdminDg(admin.getIdAdministra());
        role_repositorie.save(role);
        return DTO_response_string.fromMessage("Ajout effectué avec succé");

    }

    public Poste switchAccunt(Poste poste, long idAdmin) {
        Set<ConstraintViolation<Poste>> violations = validator.validate(poste);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Admin admin = admin_repositorie.getByIdAdministraAndActive(idAdmin, true);
        if (admin == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }


        String token = String.format("%06d", new Random().nextInt(10000));

        PendingEmail emailPend = new PendingEmail();

        emailPend.setToSend(admin.getEmail());
        emailPend.setFromAdmin(schoolEmail);
        emailPend.setBody(messages.messageForFictifAccunt(admin, token));
        emailPend.setSubject("Autorisation du sous compte" + admin.getIdRole().getNom().toUpperCase());
        messages.sendSimpleMail(emailPend);

        Poste newPoste = new Poste();
        newPoste.setDefaultAdmin(poste.getDefaultAdmin());
        newPoste.setCurrentAdmin(admin);
        newPoste.setDateTime(LocalDateTime.now());
        newPoste.setOtp(token);

       return poste_repositorie.save(newPoste);

    }

    public List<Roles> getAllRoles(long idAdmin){
        Admin admin = admin_repositorie.getByIdAdministraAndActive(idAdmin, true);
        if (admin == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        if(role_repositorie.findAll().isEmpty()){
            return new ArrayList<>();
        }
        return role_repositorie.findAll();
    }

    public Object updateRole(Roles role) {
        if (role.getTypeFiliere() == null) {
            throw new NoteFundException("Le type de filière est obligatoire et ne peut pas être null");
        }

        Roles roleExist = role_repositorie.findById(role.getId());
        if(roleExist == null){
            throw new NoteFundException("Le role n'existe pas");
        }

        roleExist.setNom(role.getNom().toUpperCase());
        roleExist.setIdAdminDg(role.getIdAdminDg());
        roleExist.setTypeFiliere(role.getTypeFiliere());
        System.out.println("------------role ----------: " + role);
        role_repositorie.save(roleExist);
        return DTO_response_string.fromMessage("Mises à jour effectuée avec succés");
    }

    public Object deletedRole(long idRole){
        Roles roleExist = role_repositorie.findById(idRole);

        if (roleExist == null) {
            throw new NoteFundException("Le rôle avec l'ID " + idRole + " n'existe pas.");
        }

        // Vérifie si le rôle est associé à un administrateur
        boolean isRoleAssignedToAdmin = role_repositorie.isRoleAssignedToAdmin(idRole);

        if (isRoleAssignedToAdmin) {
            throw new NoteFundException("Impossible, le rôle est déjà associé à un admin.");
        }

        // Si le rôle n'est pas associé à un admin, on peut le supprimer
        role_repositorie.delete(roleExist);

        return DTO_response_string.fromMessage("Rôle supprimé avec succès");
    }

    public Object Addposte(long idCurrentAdmin, long idRole){
        Admin defaultAdmin = admin_repositorie.findByIdRoleIdAndActive(idRole, true);
        if(defaultAdmin == null){
            throw new NoteFundException("L'admin par défaut ne correspond pas");
        }
        System.out.println("---------------------------------------"+defaultAdmin.getIdRole());
        Admin currentAdmin = admin_repositorie.getByIdAdministraAndActive(idCurrentAdmin, true);
        if(currentAdmin == null){
            throw new NoteFundException("L'admin courent ne correspond pas ");
        }

        Poste newPoste = new Poste();
        newPoste.setDefaultAdmin(defaultAdmin);
        newPoste.setCurrentAdmin(currentAdmin);
        newPoste.setDateTime(LocalDateTime.now());

        Set<ConstraintViolation<Poste>> violations = validator.validate(newPoste);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        poste_repositorie.save(newPoste);
        return DTO_response_string.fromMessage("Ajout effectué avec succés");
    }

    public List<AdminPostesDTO> getAllPoste(long idAdmin){
        List<Poste> postList = poste_repositorie.findByCurrentAdminIdAdministra(idAdmin);

        if(postList.isEmpty()){
            return new ArrayList<>();
        }
        return  postList.stream().map(p->{
            // Créer une nouvelle instance de AdminPostesDTO à chaque itération
            AdminPostesDTO posteDTO = new AdminPostesDTO();

            // Définir l'admin pour le poste actuel
            posteDTO.setAdmin(p.getCurrentAdmin());

            // Créer une liste des noms de rôles pour cet admin et l'assigner
            List<String> roleNames = new ArrayList<>();
            roleNames.add(p.getDefaultAdmin().getIdRole().getNom());
            System.out.println("---------------------------------------"+roleNames);
            posteDTO.setRoleNames(roleNames);
            return posteDTO;
        }).toList();

    }

    public List<AdminPostesDTO> list_admin() {
        // Récupérer tous les admins actifs
        return getAllPostByEtat(true);
    }

    public List<AdminPostesDTO> getAllPostByEtat(boolean etat){
        List<Admin> list = admin_repositorie.findAllByActive(etat);

        List<AdminPostesDTO> listPostes = new ArrayList<>();

        // Pour chaque admin, récupérer ses postes
        for (Admin admin : list) {
            AdminPostesDTO posteDTO = new AdminPostesDTO();
            posteDTO.setAdmin(admin);

            // Récupérer les postes associés à cet admin
            List<Poste> postList = poste_repositorie.findByCurrentAdminIdAdministra(admin.getIdAdministra());

            // Créer une liste pour accumuler les noms de rôles
            List<String> roleNames = new ArrayList<>();

            // Ajouter les rôles de chaque poste à la liste
            postList.forEach(lp -> {
                roleNames.add(lp.getDefaultAdmin().getIdRole().getNom());
            });

            // Assigner la liste complète des rôles au DTO
            posteDTO.setRoleNames(roleNames);

            // Ajouter le DTO à la liste finale
            listPostes.add(posteDTO);
        }
        // Retourner la liste des DTOs
        return listPostes;
    }


    public List<AdminPostesDTO> getAllByEtat(long value) {
        if(value == 1){
            return getAllPostByEtat(true);
        }
        return getAllPostByEtat(false);
    }

}
