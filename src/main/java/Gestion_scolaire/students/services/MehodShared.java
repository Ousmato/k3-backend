package Gestion_scolaire.students.services;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.NoteModuleDTO;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Notes_repositorie;
import Gestion_scolaire.Repositories.Ue_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.enumClass.InscriptionSeries;
import Gestion_scolaire.students.enumClass.StudentDiplome;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Students_repositorie;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class MehodShared {

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Validator validator;

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Notes_repositorie notes_repositorie;

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
