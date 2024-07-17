package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Repositories.Admin_repositorie;
import Gestion_scolaire.Repositories.InfoSchool_repositorie;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class Admin_service {

    @Autowired
    private Admin_repositorie admin_repositorie;

    @Autowired
    private InfoSchool_repositorie infoSchool_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String email = "ousmato98@gmail.com";
        String password = "12345@";

        Admin adminExist = admin_repositorie.findByEmail(email);
        if (adminExist == null) {
            Admin a = new Admin();
            a.setEmail(email);
            a.setNom("Oussou");
            a.setPassword(passwordEncoder.encode(password));
            a.setPrenom("Toure");
            a.setTelephone(73855156);
            a.setSexe("Homme");
            a.setActive(true);
            a.setUrlPhoto("image.jpg");
            admin_repositorie.save(a);
        }
    }

    //  =======================================================================================


}
