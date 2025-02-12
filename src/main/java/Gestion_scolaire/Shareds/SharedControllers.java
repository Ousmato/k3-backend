package Gestion_scolaire.Shareds;

import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Classes.services.StudentGroupServices;
import Gestion_scolaire.Classes.services.Ue_service;
import Gestion_scolaire.Niveaux_Filieres.services.SecondModulesService;
import Gestion_scolaire.Services.*;
import Gestion_scolaire.students.services.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class SharedControllers {

    @Autowired
    private SecondModulesService secondModulesService;

    @Autowired
    private StudentGroupServices studentGroupServices;

    @Autowired
    private Ue_service ueService;

    @Autowired
    private Modules_service modulesService;

    @Autowired
    private Classe_service classeService;

    @Autowired
    private Moyenne_service moyenne_service;

    @Autowired
    private Note_service note_service;

    @Autowired
    private Common_service common_service;

    @Autowired
    private Inscription_service inscription_service;

    @Autowired
    private Student_service student_service;

    @Autowired
    private Doc_service doc_service;

    @Autowired
    private Groupe_service groupe_service;

    @Autowired
    private Scolarite_service scolariteService;

    @Autowired
    private StudentStatistique_service studentStatistique_service;
}
