package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class Commom_methods {
    @Autowired
    private Teacher_repositorie teacher_repositorie;


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

    public String transformDate(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
        return "Semaine du " + startDate.format(formatter) + " au " + endDate.format(formatter);
    }


    public int getVolHoraire(List<Journee> journees) {
        long totalHeures = 0;
        for (Journee jour : journees) {
            Duration duration = Duration.between(jour.getHeureDebut(), jour.getHeureFin());
            long heures = duration.toHours();

            if (heures < 1) {
                throw new NoteFundException("La durée minimum de payement est égale à 1 heure");
            }
            if (heures > 8) {
                heures -= 2; // On retire 2 heures si la durée dépasse 8 heures
            }

            totalHeures += heures; // Accumulation des heures
        }
        return (int) totalHeures;
    }

}
