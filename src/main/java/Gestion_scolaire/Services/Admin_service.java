package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.AdminDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.Admin_role;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Repositories.Admin_repositorie;
import Gestion_scolaire.Repositories.InfoSchool_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class Admin_service {

    @Autowired
    private Admin_repositorie admin_repositorie;

    @Autowired
    private fileManagers fileManagers;

    @Autowired
    private Validator validator;

    @Autowired
    MessaSender messaSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessaSender messages;

    @Autowired
    private HttpSession session;

    String adminEmail = "ousmatotoure98@gmail.com";

    @PostConstruct
    public void init() {
        String email = "ousmato98@gmail.com";
        String password = "Test@123";

        Admin adminExist = admin_repositorie.findByEmail(email);
        if (adminExist == null) {
            Admin a = new Admin();
            a.setEmail(email);
            a.setNom("Oussou");
            a.setPassword(passwordEncoder.encode(password));
            a.setPrenom("Toure");
            a.setTelephone(73855156);
            a.setSexe("Homme");
            a.setRole(Admin_role.dg);
            a.setActive(true);
            a.setUpdateDate(LocalDate.now());
            a.setUrlPhoto("image.jpg");
            admin_repositorie.save(a);
        }
    }

    //  =======================================================================================
    public Object add(Admin admin, MultipartFile file) throws Exception {
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Admin adminExist = admin_repositorie.findByRoleAndActive(admin.getRole(), true);
        if (adminExist != null) {
           throw new NoteFundException("Impossible d'attribuer le meme role a deux administrateur");
        }

        String passWordPlan = admin.getPassword();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        String urPhoto = fileManagers.saveFile(file);
        admin.setUrlPhoto(urPhoto);


        PendingEmail emailPend = new PendingEmail();
        emailPend.setToSend(admin.getEmail());
        emailPend.setFromAdmin(adminEmail);
        emailPend.setBody("Bonjour M. %s %s%s,".formatted(admin.getNom(), admin.getPrenom(), messages.messageAdmin(admin, passWordPlan)));
        emailPend.setSubject("Confirmation");

        messages.messageAdmin(admin,passWordPlan);
        admin_repositorie.save(admin);
        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
    }

//    ----------------------------------------get all admin
    public List<Admin> list_admin() {
        return admin_repositorie.findAllByActive(true);
    }

    public List<Admin> getAllByEtat(long value) {
        if(value == 1){
            return admin_repositorie.findAllByActive(true);
        }
        return admin_repositorie.findAllByActive(false);

    }
//    ---------------------------------------
    public Object chageEtatByIdAdmin(long id) {
        Admin admin = admin_repositorie.findByIdAdministra(id);
        if(admin != null){

            if(!admin.isActive() && admin_repositorie.findByRoleAndActive(admin.getRole(), true) != null){
                throw new NoteFundException("Il existe déjà un "+ admin.getRole().toString().toUpperCase() + " en activité");
            }
            admin.setActive(!admin.isActive());
            admin_repositorie.save(admin);
            return DTO_response_string.fromMessage("Mises à jour éffectuer avec succès", 200);
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    ------------------------
    public Admin getAdminBy(long id) {
        Admin admin = admin_repositorie.findByIdAdministra(id);
        if(admin != null){
            return admin;
        }
        throw new NoteFundException("L'administrateur est introuvable");
    }

//    --------------------

    public Admin changeImage(long id, MultipartFile file) throws Exception {

        Admin admin = admin_repositorie.findByIdAdministra(id);
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
        System.out.println("-----------------------"+urPhoto);

        admin_repositorie.save(admin);
        System.out.println("------------save-----------"+admin);
        return admin;
    }

//    ------------------
    public Object updatAdmin(AdminDTO admin){
        Admin adminExist = admin_repositorie.findByIdAdministra(admin.getIdAdministra());
        if(adminExist == null){
            throw new NoteFundException("L'administrateur est introuvable");
        }
        adminExist.setNom(admin.getNom());
        adminExist.setPrenom(admin.getPrenom());
        adminExist.setEmail(admin.getEmail());
        adminExist.setTelephone(admin.getTelephone());
        adminExist.setUpdateDate(LocalDate.now());
        admin_repositorie.save(adminExist);
        return adminExist;
    }

    public Admin forgotPassword(String email) {
        Admin admin = admin_repositorie.findByEmail(email);
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
        emailPend.setFromAdmin(adminEmail);
        emailPend.setBody(messages.messageResetPassword(admin.getPrenom(), link, token));
        emailPend.setSubject("Réinitialisation de mot de passe");
        messaSender.sendSimpleMail(emailPend);

        return admin;
    }

    //    ---------------validation code de confirmation

}
