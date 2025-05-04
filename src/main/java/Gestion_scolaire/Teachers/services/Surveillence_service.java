package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.entity.Surveillants;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Surveillence_service {

    @Autowired
    private Validator validator;
    private final Shared_repositories shared_repositories;


    public Object addSurveillant(Surveillants surveillants) {
        Set<ConstraintViolation<Surveillants>> violation = validator.validate(surveillants);
        if(!violation.isEmpty()){
            throw new ConstraintViolationException(violation);
        }
        Surveillants surveillantExist = shared_repositories.getSurveillants_repositorie().findByNomAndPrenomAndTelephone(surveillants.getNom(), surveillants.getPrenom(), surveillants.getTelephone());

        if (surveillantExist != null) {
            throw new NoteFundException("Ce surveillant avec ces informations existe déjà");
        }
        shared_repositories.getSurveillants_repositorie().save(surveillants);
        return DTO_response_string.addMessage();
    }

    public List<Surveillants> findAllSurveillants() {
        return shared_repositories.getSurveillants_repositorie().findAll();
    }
}
