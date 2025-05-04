package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.dtos.AdminPostesDTO;
import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Administrators.repositories.Poste_repositorie;

import Gestion_scolaire.Administrators.repositories.UserGrade_repositorie;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.RoleTypes;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Models.UsersGrade;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class Roles_services {

    private  final UserGrade_repositorie userGrade_repositorie;

    private final Poste_repositorie poste_repositorie;

    private final AdminRepositorie admin_repositorie;


    private final Validator validator;


    private final MessaSender messages;


    private final Shared_methods_service sharedService;



    String schoolEmail = "ousmatotoure98@gmail.com";


    public Object addPoste(Postes poste, long idAdmin) {
        Postes roleExist = poste_repositorie.findByNomAndTypeFiliere(poste.getNom(), poste.getTypeFiliere());
        if (roleExist != null) {
            throw new NoteFundException("Le poste exist dejà");
        }
//
        Set<ConstraintViolation<Postes>> violations = validator.validate(poste);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        System.out.println("role-------------------" + poste);

        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
        if (administrationUsers == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        String nomRole = sharedService.abrevigateName(administrationUsers.getIdPoste().getNom());
    if(!sharedService.abrevigateName(administrationUsers.getIdPoste().getNom()).equalsIgnoreCase(nomRole) && !administrationUsers.getIdPoste().getRoleType().equals(RoleTypes.SUPER_ADMIN)){
            throw new NoteFundException("Vous n'êtes pas autorisé");
        }

        //role.setIdAdminDg(administrationUsers.getIdAdministra());
        poste_repositorie.save(poste);
        return DTO_response_string.addMessage();

    }

    public Object addUserGrade(UsersGrade grade, long idAdmin) {
        UsersGrade gradExist = userGrade_repositorie.findByLibelle(grade.getLibelle());
        if (gradExist != null) {
            throw new NoteFundException("Le poste exist dejà");
        }

        Set<ConstraintViolation<UsersGrade>> violations = validator.validate(grade);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        System.out.println("grade-------------------" + grade);

        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
        if (administrationUsers == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        String nomRole = sharedService.abrevigateName(administrationUsers.getIdPoste().getNom());
        if(!sharedService.abrevigateName(administrationUsers.getIdPoste().getNom()).equalsIgnoreCase(nomRole) && !administrationUsers.getIdPoste().getRoleType().equals(RoleTypes.SUPER_ADMIN)){
            throw new NoteFundException("Vous n'êtes pas autorisé");
        }

        //role.setIdAdminDg(administrationUsers.getIdAdministra());
        userGrade_repositorie.save(grade);
        return DTO_response_string.addMessage();

    }

//    public Postes switchAccunt(Postes postes, long idAdmin) {
//        Set<ConstraintViolation<Postes>> violations = validator.validate(postes);
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(violations);
//        }
//        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
//        Postes postAdmin =
//        if (administrationUsers == null) {
//            throw new NoteFundException("L'admin n'existe pas");
//        }
//
//
//        String token = String.format("%06d", new Random().nextInt(10000));
//
//        PendingEmail emailPend = new PendingEmail();
//
//        emailPend.setToSend(administrationUsers.getEmail());
//        emailPend.setFromAdmin(schoolEmail);
//        emailPend.setBody(messages.messageForFictifAccunt(administrationUsers, token));
//        emailPend.setSubject("Autorisation du sous compte" + administrationUsers.getIdRole().getNom().toUpperCase());
//        messages.sendSimpleMail(emailPend);
//
//        Postes newPostes = new Postes();
//        newPostes.setDefaultAdmin(postes.getDefaultAdmin());
//        newPostes.setCurrentAdmin(administrationUsers);
//        newPostes.setDateTime(LocalDateTime.now());
//        newPostes.setOtp(token);
//
//       return poste_repositorie.save(newPostes);
//
//    }


    public List<Postes> getAllRoles(long idAdmin){
        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
        if (administrationUsers == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        if(!poste_repositorie.findAll().isEmpty() && administrationUsers.getIdPoste().getRoleType().equals(RoleTypes.SUPER_ADMIN)){
            return poste_repositorie.findAll();
        }
        return new ArrayList<>();
    }

    public List<UsersGrade> getAllGdrades(long idAdmin){
        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
        if (administrationUsers == null) {
            throw new NoteFundException("L'admin n'existe pas");
        }
        if(!userGrade_repositorie.findAll().isEmpty() && administrationUsers.getIdPoste().getRoleType().equals(RoleTypes.SUPER_ADMIN)){
            return userGrade_repositorie.findAll();
            
        }
        return new ArrayList<>();
    }

    public Object updateRole(Postes role) {
        if (role.getTypeFiliere() == null) {
            throw new NoteFundException("Le type de filière est obligatoire et ne peut pas être null");
        }

        Postes roleExist = poste_repositorie.findById(role.getId());
        if(roleExist == null){
            throw new NoteFundException("Le role n'existe pas");
        }

        roleExist.setNom(role.getNom().toUpperCase());
        roleExist.setTypeFiliere(role.getTypeFiliere());
        System.out.println("------------role ----------: " + role);
        poste_repositorie.save(roleExist);
        return DTO_response_string.fromMessage("Mises à jour effectuée avec succés");
    }

//    public Object deletedRole(long idRole){
//        Roles roleExist = role_repositorie.findById(idRole);
//
//        if (roleExist == null) {
//            throw new NoteFundException("Le rôle avec l'ID " + idRole + " n'existe pas.");
//        }
//
//        // Vérifie si le rôle est associé à un administrateur
//        boolean isRoleAssignedToAdmin = role_repositorie.isRoleAssignedToAdmin(idRole);
//
//        if (isRoleAssignedToAdmin) {
//            throw new NoteFundException("Impossible, le rôle est déjà associé à un admin.");
//        }
//
//        // Si le rôle n'est pas associé à un admin, on peut le supprimer
//        role_repositorie.delete(roleExist);
//
//        return DTO_response_string.fromMessage("Rôle supprimé avec succès");
//    }

//    public Object Addposte(long idCurrentAdmin, long idRole){
//        AdministrationUsers defaultAdministrationUsers = admin_repositorie.findByIdRoleIdAndActive(idRole, true);
//        if(defaultAdministrationUsers == null){
//            throw new NoteFundException("L'admin par défaut ne correspond pas");
//        }
//        System.out.println("---------------------------------------"+ defaultAdministrationUsers.getIdRole());
//        AdministrationUsers currentAdministrationUsers = admin_repositorie.getByIdAdministraAndActive(idCurrentAdmin, true);
//        if(currentAdministrationUsers == null){
//            throw new NoteFundException("L'admin courent ne correspond pas ");
//        }
//
//        Postes newPostes = new Postes();
//        newPostes.setDefaultAdmin(defaultAdministrationUsers);
//        newPostes.setCurrentAdmin(currentAdministrationUsers);
//        newPostes.setDateTime(LocalDateTime.now());
//
//        Set<ConstraintViolation<Postes>> violations = validator.validate(newPostes);
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(violations);
//        }
//        poste_repositorie.save(newPostes);
//        return DTO_response_string.fromMessage("Ajout effectué avec succés");
//    }



    public List<AdminPostesDTO> list_admin() {
        // Récupérer tous les admins actifs
        return getAllPostByEtat(true);
    }

    public List<AdminPostesDTO> getAllPostByEtat(boolean etat){
        List<AdministrationUsers> list = admin_repositorie.getAdminByActive(etat);

        List<AdminPostesDTO> listPostes = new ArrayList<>();

        // Pour chaque admin, récupérer ses postes
        for (AdministrationUsers administrationUsers : list) {
            AdminPostesDTO posteDTO = new AdminPostesDTO();
            posteDTO.setAdmin(administrationUsers);

            // Récupérer les postes associés à cet admin
            //List<Postes> postList = poste_repositorie.findByIdAdministrationUsersId(administrationUsers.getId());

            // Créer une liste pour accumuler les noms de rôles
            List<String> roleNames = new ArrayList<>();

            // Ajouter les rôles de chaque poste à la liste
//            postList.forEach(lp -> {
//                roleNames.add(lp.getDefaultAdmin().getIdRole().getNom());
//            });

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
