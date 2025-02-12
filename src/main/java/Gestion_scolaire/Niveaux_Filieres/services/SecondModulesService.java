package Gestion_scolaire.Niveaux_Filieres.services;

import Gestion_scolaire.Classes.dtos.ModuleDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SecondModulesService {
    @Autowired
    private Shared_services sharedservices;


    @Autowired
    private Shared_repositories shared_repositories;

    public Object udateModuleVolHoraire(ModuleDTO dto){
        Modules moduleExist = sharedservices.getModules_service().getModule(dto.getId());

        // Mise à jour uniquement si les valeurs ne sont pas nulles

        if (dto.getVolHCM() != null) {
            moduleExist.setVolHCM(dto.getVolHCM());
        }
        if (dto.getVolHTD() != null) {
            moduleExist.setVolHTD(dto.getVolHTD());
        }
        if (dto.getVolTPE() != null) {
            moduleExist.setVolTPE(dto.getVolTPE());
        }
        if (dto.getVolTP() != null){
            moduleExist.setVolTP(dto.getVolTP());
        }
        Set<ConstraintViolation<Modules>> violation = sharedservices.getValidator().validate(moduleExist);
        if (!violation.isEmpty()) {
            throw new ConstraintViolationException(violation);
        }
        shared_repositories.getModules_repositories().save(moduleExist);
        return DTO_response_string.updateMessage();
    }


}
