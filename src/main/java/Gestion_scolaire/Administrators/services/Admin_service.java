package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.dtos.AdminDTO;
import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Models.RefreshToken;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.configuration.SecurityConfigs.AdminInfoDetails;
import Gestion_scolaire.students.entity.Students;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
public class Admin_service implements UserDetailsService {
    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    @Autowired
    private  Shared_repositories shared_repositories;

    @Autowired
    private Validator validator;

    @Autowired
    private MessaSender messaSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessaSender messages;

    @Autowired
    private HttpSession session;

    String schoolEmail = "ousmatotoure98@gmail.com";



//    @Transactional
//    @PostConstruct
//    public void init() {
//        String email = "ousmato98@gmail.com";
//        String password = "Test@123";
//        String nomRole = "Admin";
//
//        Roles role = shared_repositories.getRole_repositorie().findByNom(nomRole);
//
//        if(role == null) {
//            role = new Roles();
//            role.setNom(nomRole);
//            role.setIdSuperAdmin(1);
//            shared_repositories.getRole_repositorie().save(role);
//
//        }
//        AdministrationUsers administrationUsersExist = shared_repositories.getAdminRepositorie().findByEmail(email);
//        if (administrationUsersExist == null) {
//            AdministrationUsers a = new AdministrationUsers();
//            a.setEmail(email);
//            a.setNom("Oussou");
//            a.setPassword(passwordEncoder.encode(password));
//            a.setPrenom("Toure");
//            a.setTelephone("73855156");
//            a.setSexe("Homme");
//            a.setIdRole(role);
//            a.setActive(true);
//            a.setUpdateDate(LocalDate.now());
//            a.setUrlPhoto("image.jpg");
//            adminRepositorie.save(a);
//            role.setIdAdminDg(a.getIdAdministra());
//            role_repositorie.save(role);
//
//        }
//    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AdministrationUsers> userDetail = shared_repositories.getAdminRepositorie().getAdminByEmailAndActive(email, true); // Assuming 'email' is used as username

        // Converting admin to UserDetails
        return userDetail.map(AdminInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    //  =======================================================================================
    public Object add(AdministrationUsers administrationUsers, MultipartFile file) throws Exception {
        Set<ConstraintViolation<AdministrationUsers>> violations = validator.validate(administrationUsers);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
//        AdministrationUsers administrationUsersExist = shared_repositories.getAdminRepositorie().findByIdRoleIdAndActiveAndEmailAndIdRoleTypeFiliere(administrationUsers.getIdRole().getId(), true, administrationUsers.getEmail(), administrationUsers.getIdRole().getTypeFiliere());
//        if (administrationUsersExist != null) {
//           throw new NoteFundException("Impossible d'attribuer le meme role a deux administrateur");
//        }
        AdministrationUsers administrationUsersByEmail = shared_repositories.getAdminRepositorie().findByEmail(administrationUsers.getEmail());
        if(administrationUsersByEmail != null) {
            throw new NoteFundException("L'admin avec cette email existe déjà");
        }
        String passWordPlan = administrationUsers.getPassword();
        administrationUsers.setPassword(passwordEncoder.encode(administrationUsers.getPassword()));
        String urPhoto = fileManagers.saveFile(file);
        administrationUsers.setUrlPhoto(urPhoto);


        PendingEmail emailPend = new PendingEmail();
        emailPend.setToSend(administrationUsers.getEmail());
        emailPend.setFromAdmin(schoolEmail);
        emailPend.setBody("Bonjour M. %s %s%s,".formatted(administrationUsers.getNom(), administrationUsers.getPrenom(), messages.messageAdmin(administrationUsers, passWordPlan)));
        emailPend.setSubject("Confirmation");

        messages.messageAdmin(administrationUsers,passWordPlan);
        shared_repositories.getAdminRepositorie().save(administrationUsers);
        return DTO_response_string.fromMessage("Ajout effectué avec succès");
    }

//    ---------------------------------------
    public Object chageEtatByIdAdmin(long id) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findById(id);
        if(administrationUsers != null){

            if(!administrationUsers.isActive() && shared_repositories.getAdminRepositorie().findByIdPosteIdAndActiveAndEmailAndIdPosteTypeFiliere(administrationUsers.getIdPoste().getId(), true, administrationUsers.getEmail(), administrationUsers.getIdPoste().getTypeFiliere()) != null){
                throw new NoteFundException("Il existe déjà un "+ administrationUsers.getIdPoste().getNom().toUpperCase() + " en activité");
            }
            administrationUsers.setActive(!administrationUsers.isActive());
            shared_repositories.getAdminRepositorie().save(administrationUsers);
            return DTO_response_string.fromMessage("Mises à jour éffectuer avec succès");
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    ------------------------
    public AdministrationUsers getAdminBy(long id) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findById(id);
        if(administrationUsers != null){
            return administrationUsers;
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    --------------------

    public AdministrationUsers changeImage(long id, MultipartFile file) throws Exception {

        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findById(id);
        System.out.println("----------------------" + administrationUsers);
        Set<ConstraintViolation<AdministrationUsers>> violations = validator.validate(administrationUsers);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (administrationUsers == null) {
            throw new NoteFundException("L'administrateur est introuvable");
        }
        String oldPath = administrationUsers.getUrlPhoto();
        String urPhoto = fileManagers.updateFile(file, oldPath);
        administrationUsers.setUrlPhoto(urPhoto);
        administrationUsers.setUpdateDate(LocalDate.now());
//        System.out.println("-----------------------"+urPhoto);

        shared_repositories.getAdminRepositorie().save(administrationUsers);
//        System.out.println("------------save-----------"+admin);
        return administrationUsers;
    }

//    ------------------
    public Object updatAdmin(AdministrationUsers admin){
        AdministrationUsers administrationUsersExist = shared_repositories.getAdminRepositorie().findById(admin.getId());
        if(administrationUsersExist == null){
            throw new NoteFundException("L'administrateur est introuvable");
        }
        administrationUsersExist.setNom(admin.getNom());
        administrationUsersExist.setPrenom(admin.getPrenom());
        administrationUsersExist.setMatricule(admin.getMatricule());
        administrationUsersExist.setIdPoste(admin.getIdPoste());
        administrationUsersExist.setSexe(admin.getSexe());
        administrationUsersExist.setCompteBanque(admin.getCompteBanque());
        administrationUsersExist.setNomBanque(admin.getNomBanque());
        administrationUsersExist.setEmail(admin.getEmail());
        administrationUsersExist.setUsersGrade(admin.getUsersGrade());
        administrationUsersExist.setTelephone(admin.getTelephone());
        administrationUsersExist.setUpdateDate(LocalDate.now());
        shared_repositories.getAdminRepositorie().save(administrationUsersExist);
        return DTO_response_string.updateMessage();
    }

    public AdministrationUsers forgotPassword(String email) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findByEmail(email);
        if(administrationUsers == null){
            throw new NoteFundException("L'administrateur est introuvable");
        }
        String token = String.format("%04d", new Random().nextInt(10000));
        session.setAttribute("resetToken", token);
        String sessionToken = (String) session.getAttribute("resetToken");
        System.out.println("----session to add-------" + sessionToken);
        String link = "http://localhost:8080/reset-password";
        PendingEmail emailPend = new PendingEmail();

        emailPend.setToSend(administrationUsers.getEmail());
        emailPend.setFromAdmin(schoolEmail);
        emailPend.setBody(messages.messageResetPassword(administrationUsers.getPrenom(), link, token));
        emailPend.setSubject("Réinitialisation de mot de passe");
        messaSender.sendSimpleMail(emailPend);

        return administrationUsers;
    }

    public  void addRefreshToken(AdministrationUsers administrationUsers, String refreshToken) {
        RefreshToken rft = shared_repositories.getRefreshRepositorie().findByAdministrationUsersId(administrationUsers.getId());
        if (rft == null) {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setAdministrationUsers(administrationUsers);
            shared_repositories.getRefreshRepositorie().save(newRefreshToken);
        }else {
            rft.setToken(refreshToken);
            rft.setAdministrationUsers(administrationUsers);
            shared_repositories.getRefreshRepositorie().save(rft);
        }

    }


    public  void addRefreshTokenStudent(Students student, String refreshToken) {
        RefreshToken rft = shared_repositories.getRefreshRepositorie().findByAdministrationUsersId(student.getId());
        if (rft == null) {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setStudents(student);
            shared_repositories.getRefreshRepositorie().save(newRefreshToken);
        }else {
            rft.setToken(refreshToken);
            rft.setStudents(student);
            shared_repositories.getRefreshRepositorie().save(rft);
        }

    }



    public RefreshToken getRefresh(long idAdmin){
       return shared_repositories.getRefreshRepositorie().findByAdministrationUsersId(idAdmin);

    }

    public AdministrationUsers getByEmail(String email) {
        return shared_repositories.getAdminRepositorie().findByEmailAndActive(email, true);
    }

}
