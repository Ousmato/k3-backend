package Gestion_scolaire.Authentification;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.UsersAbstract;
import Gestion_scolaire.Repositories.Admin_repositorie;
import Gestion_scolaire.Repositories.Students_repositorie;
import Gestion_scolaire.Repositories.Teacher_repositorie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Auth {

    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private Admin_repositorie admin_repositorie;

    @Autowired
   private PasswordEncoder passwordEncoder;

    public Object authenticate(String email, String password) {
        Object authenticatedUser = null;
        System.out.println(email);
        System.out.println(password);
        Admin admin = admin_repositorie.findByEmail(email);
        System.out.println(admin);
//        System.out.println(passwordEncoder.matches(password, admin.getPassword())+"auth----------------------------");

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            authenticatedUser = admin;
        } else {
            Teachers teacher = teacher_repositorie.findByEmail(email);
            if (teacher != null && passwordEncoder.matches(password, teacher.getPassword())) {
                authenticatedUser = teacher;
            } else {
                Studens student = students_repositorie.findByEmail(email);
                if (student != null && passwordEncoder.matches(password, student.getPassword())) {
                    authenticatedUser = student;
                }
            }
        }

        return authenticatedUser;
    }


//    ==================================================================================
}
