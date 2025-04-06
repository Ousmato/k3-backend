package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.dtos.AdminDTO;
import Gestion_scolaire.Administrators.entity.Roles;
import Gestion_scolaire.Administrators.repositories.Role_repositorie;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Models.RefreshToken;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Repositories.RefreshRepositorie;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.configuration.SecurityConfigs.AdminInfoDetails;
import Gestion_scolaire.students.entity.Students;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
    private AdminRepositorie adminRepositorie;

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    @Autowired
    private Validator validator;

    @Autowired
    private RefreshRepositorie refreshRepositorie;

    @Autowired
    private Role_repositorie role_repositorie;

    @Autowired
    MessaSender messaSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessaSender messages;

    @Autowired
    private Shared_methods_service sharedService;

    @Autowired
    private HttpSession session;

    String schoolEmail = "ousmatotoure98@gmail.com";

    @Transactional
    @PostConstruct
    public void init() {
        String email = "ousmato98@gmail.com";
        String password = "Test@123";
        String nomRole = "Admin";

        Roles role = role_repositorie.findByNom(nomRole);

        if(role == null) {
            role = new Roles();
            role.setNom(nomRole);
            role.setIdAdminDg(1);
            role_repositorie.save(role);

        }
        Admin adminExist = adminRepositorie.findByEmail(email);
        if (adminExist == null) {
            Admin a = new Admin();
            a.setEmail(email);
            a.setNom("Oussou");
            a.setPassword(passwordEncoder.encode(password));
            a.setPrenom("Toure");
            a.setTelephone("73855156");
            a.setSexe("Homme");
            a.setIdRole(role);
            a.setActive(true);
            a.setUpdateDate(LocalDate.now());
            a.setUrlPhoto("image.jpg");
            adminRepositorie.save(a);
            role.setIdAdminDg(a.getIdAdministra());
            role_repositorie.save(role);

        }
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> userDetail = adminRepositorie.getAdminByEmailAndActive(email, true); // Assuming 'email' is used as username

        // Converting admin to UserDetails
        return userDetail.map(AdminInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    //  =======================================================================================
    public Object add(Admin admin, MultipartFile file) throws Exception {
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Admin adminExist = adminRepositorie.findByIdRoleIdAndActiveAndEmailAndIdRoleTypeFiliere(admin.getIdRole().getId(), true, admin.getEmail(), admin.getIdRole().getTypeFiliere());
        if (adminExist != null) {
           throw new NoteFundException("Impossible d'attribuer le meme role a deux administrateur");
        }
        Admin adminByEmail = adminRepositorie.findByEmail(admin.getEmail());
        if(adminByEmail != null) {
            throw new NoteFundException("L'admin avec cette email existe déjà");
        }
        String passWordPlan = admin.getPassword();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        String urPhoto = fileManagers.saveFile(file);
        admin.setUrlPhoto(urPhoto);


        PendingEmail emailPend = new PendingEmail();
        emailPend.setToSend(admin.getEmail());
        emailPend.setFromAdmin(schoolEmail);
        emailPend.setBody("Bonjour M. %s %s%s,".formatted(admin.getNom(), admin.getPrenom(), messages.messageAdmin(admin, passWordPlan)));
        emailPend.setSubject("Confirmation");

        messages.messageAdmin(admin,passWordPlan);
        adminRepositorie.save(admin);
        return DTO_response_string.fromMessage("Ajout effectué avec succès");
    }

//    ---------------------------------------
    public Object chageEtatByIdAdmin(long id) {
        Admin admin = adminRepositorie.findByIdAdministra(id);
        if(admin != null){

            if(!admin.isActive() && adminRepositorie.findByIdRoleIdAndActiveAndEmailAndIdRoleTypeFiliere(admin.getIdRole().getId(), true, admin.getEmail(), admin.getIdRole().getTypeFiliere()) != null){
                throw new NoteFundException("Il existe déjà un "+ admin.getIdRole().toString().toUpperCase() + " en activité");
            }
            admin.setActive(!admin.isActive());
            adminRepositorie.save(admin);
            return DTO_response_string.fromMessage("Mises à jour éffectuer avec succès");
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    ------------------------
    public Admin getAdminBy(long id) {
        Admin admin = adminRepositorie.findByIdAdministra(id);
        if(admin != null){
            return admin;
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    --------------------

    public Admin changeImage(long id, MultipartFile file) throws Exception {

        Admin admin = adminRepositorie.findByIdAdministra(id);
        System.out.println("----------------------" + admin);
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (admin == null) {
            throw new NoteFundException("L'administrateur est introuvable");
        }
        String oldPath = admin.getUrlPhoto();
        String urPhoto = fileManagers.updateFile(file, oldPath);
        admin.setUrlPhoto(urPhoto);
        admin.setUpdateDate(LocalDate.now());
//        System.out.println("-----------------------"+urPhoto);

        adminRepositorie.save(admin);
//        System.out.println("------------save-----------"+admin);
        return admin;
    }

//    ------------------
    public Object updatAdmin(AdminDTO admin){
        Admin adminExist = adminRepositorie.findByIdAdministra(admin.getIdAdministra());
        if(adminExist == null){
            throw new NoteFundException("L'administrateur est introuvable");
        }
        adminExist.setNom(admin.getNom());
        adminExist.setPrenom(admin.getPrenom());
        adminExist.setEmail(admin.getEmail());
        adminExist.setTelephone(admin.getTelephone());
        adminExist.setUpdateDate(LocalDate.now());
        adminRepositorie.save(adminExist);
        return adminExist;
    }

    public Admin forgotPassword(String email) {
        Admin admin = adminRepositorie.findByEmail(email);
        if(admin == null){
            throw new NoteFundException("L'administrateur est introuvable");
        }
        String token = String.format("%04d", new Random().nextInt(10000));
        session.setAttribute("resetToken", token);
        String sessionToken = (String) session.getAttribute("resetToken");
        System.out.println("----session to add-------" + sessionToken);
        String link = "http://localhost:8080/reset-password";
        PendingEmail emailPend = new PendingEmail();

        emailPend.setToSend(admin.getEmail());
        emailPend.setFromAdmin(schoolEmail);
        emailPend.setBody(messages.messageResetPassword(admin.getPrenom(), link, token));
        emailPend.setSubject("Réinitialisation de mot de passe");
        messaSender.sendSimpleMail(emailPend);

        return admin;
    }

    public  void addRefreshToken(Admin admin, String refreshToken) {
        RefreshToken rft = refreshRepositorie.findByAdminIdAdministra(admin.getIdAdministra());
        if (rft == null) {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setAdmin(admin);
            refreshRepositorie.save(newRefreshToken);
        }else {
            rft.setToken(refreshToken);
            rft.setAdmin(admin);
            refreshRepositorie.save(rft);
        }

    }


    public  void addRefreshTokenStudent(Students student, String refreshToken) {
        RefreshToken rft = refreshRepositorie.findByAdminIdAdministra(student.getIdEtudiant());
        if (rft == null) {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setStudents(student);
            refreshRepositorie.save(newRefreshToken);
        }else {
            rft.setToken(refreshToken);
            rft.setStudents(student);
            refreshRepositorie.save(rft);
        }

    }

    public UserDetails getRefreshToken(String email) {
      RefreshToken refreshTokenExist = refreshRepositorie.findByAdminEmail(email);

          return   loadUserByUsername(refreshTokenExist.getAdmin().getEmail());


    }

    public RefreshToken getRefresh(long idAdmin){
       RefreshToken rft =  refreshRepositorie.findByAdminIdAdministra(idAdmin);
         return rft;
    }

    public Admin getByEmail(String email) {
        return adminRepositorie.findByEmailAndActive(email, true);
    }

}
