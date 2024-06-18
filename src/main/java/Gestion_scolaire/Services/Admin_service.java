package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Repositories.Admin_repositorie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Admin_service {

    @Autowired
    private Admin_repositorie admin_repositorie;

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
            a.setUrlPhoto("oussou.png");
            admin_repositorie.save(a);
        }
    }

    //  =======================================================================================

}
