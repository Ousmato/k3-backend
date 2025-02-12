package Gestion_scolaire.students.services;

import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Students;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class MehodShared {


    @Autowired
    private Validator validator;


    public Students validateSudent(Students student) {
        int currentYear = LocalDate.now().getYear();


        Set<ConstraintViolation<Students>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        String telephone = String.valueOf(student.getTelephone());
        if (telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }

//        LocalDate dateNaissance = LocalDate.now().minusYears(15);
//        if ( dateNaissance.isBefore(student.getDateNaissance())) {
//            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
//        }
        if (!student.getMatricule().isEmpty() && student.getMatricule().length() != 12) {
            throw new NoteFundException("Le matricule n'est pas valide");
        }

        int fiveLastYear = LocalDate.now().getYear()-5;
        if (student.getAnneeObtention() != 0 && (student.getAnneeObtention() < fiveLastYear || student.getAnneeObtention() > currentYear)) {
            throw new NoteFundException("L'année d'obtention du diplôme " + student.getSeries() + " n'est pas valide");
        }

        return student;
    }

    // formated enum class methode
    public String enumFormated(String enumName){
        return  enumName.replace("_", " ").toUpperCase();
    }



}
