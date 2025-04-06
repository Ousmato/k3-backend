package Gestion_scolaire.Administrators.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.students.repositories.Students_repositorie;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Auth_service {

    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private AdminRepositorie adminRepositorie;

    @Autowired
   private PasswordEncoder passwordEncoder;

    public Object authenticate(String email, String password) {
        Object authenticatedUser = null;
//        System.out.println("-------------------------------" +email);
        System.out.println(password);
        Admin admin = adminRepositorie.findByEmailAndActive(email, true);
        System.out.println(admin);
//        System.out.println(passwordEncoder.matches(password, admin.getPassword())+"auth----------------------------");

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            authenticatedUser = admin;
        } else {
            Teachers teacher = teacher_repositorie.findByEmail(email);
            if (teacher != null && passwordEncoder.matches(password, teacher.getPassword())) {
                authenticatedUser = teacher;
            } else {
                Students student = students_repositorie.findByEmail(email);
                System.out.println("je suuis bien etudiant : " + student);
                if (student != null) {
                    authenticatedUser = student;
                }
            }
        }

        return authenticatedUser;
    }
}
