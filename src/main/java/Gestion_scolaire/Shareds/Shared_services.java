package Gestion_scolaire.Shareds;

import Gestion_scolaire.Classes.services.Ue_service;
import Gestion_scolaire.Emplois.services.Emplois_service;
import Gestion_scolaire.Services.*;
import Gestion_scolaire.Teachers.services.Surveillence_service;
import Gestion_scolaire.students.services.Inscription_service;
import Gestion_scolaire.students.services.Student_service;
import jakarta.validation.Validator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public  class Shared_services {


    private  final  Semestre_service semestre_service;


    private final PasswordEncoder passwordEncoder;


    private final Modules_service modules_service;


    private final Inscription_service inscription_service;


    private  final Student_service student_service;


    private final Validator validator;


    private  final Groupe_service groupe_service;


    private  final Emplois_service emplois_service;


    private final Shared_methods_service shared_methods_service;


    private  final Ue_service ue_service;

    private final Surveillence_service surveillence_service;

}
