package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Teachers.repositories.Paie_repositorie;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class Commom_methods {
    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private Paie_repositorie paie_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    private MessaSender messaSender;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    @Autowired
    private Validator validator;

    public Teachers validateTeacher(Teachers teacher) {
        teacher.setPassword("Tes@123");
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        Set<ConstraintViolation<Teachers>> violations = validator.validate(teacher);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Teachers tch = teacher_repositorie.getTeachersByEmailAndTelephone(teacher.getEmail(), teacher.getTelephone());
        if (tch != null) {
            throw new NoteFundException("Ce numéro de telephone ou l'adresse email existe déjà");
        }
        return teacher;
    }
}
