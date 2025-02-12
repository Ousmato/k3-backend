package Gestion_scolaire.Shareds;

import Gestion_scolaire.Classes.services.Ue_service;
import Gestion_scolaire.Emplois.services.Emplois_service;
import Gestion_scolaire.Niveaux_Filieres.services.SecondModulesService;
import Gestion_scolaire.Services.*;
import Gestion_scolaire.students.services.Inscription_service;
import Gestion_scolaire.students.services.Student_service;
import jakarta.validation.Validator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
public  class Shared_services {

    @Autowired
    private Semestre_service semestre_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  Modules_service modules_service;

    @Autowired
    private Inscription_service inscription_service;

    @Autowired
    private Student_service student_service;

    @Autowired
    private Validator validator;

    @Autowired
    private Groupe_service groupe_service;

    @Autowired
    private Emplois_service emplois_service;

    @Autowired
    private Shared_methods_service shared_methods_service;

    @Autowired
    private Ue_service ue_service;

//    @Autowired
//    private Moyenne_service moyenne_service;


}
