package Gestion_scolaire.Shareds;

import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Classes.services.StudentGroupServices;
import Gestion_scolaire.Classes.services.Ue_service;
import Gestion_scolaire.Emplois.services.Jounee_service;
import Gestion_scolaire.Niveaux_Filieres.services.SecondModulesService;
import Gestion_scolaire.Services.*;
import Gestion_scolaire.students.services.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
public class SharedControllers {


    private final SecondModulesService secondModulesService;


    private final StudentGroupServices studentGroupServices;


    private  final Ue_service ueService;


    private final Modules_service modulesService;


    private  final Classe_service classeService;


    private final Moyenne_service moyenne_service;


    private final Note_service note_service;


    private final Common_service common_service;


    private final Inscription_service inscription_service;


    private final Student_service student_service;


    private final Doc_service doc_service;


    private final Groupe_service groupe_service;

    private final Jounee_service jounee_service;

    private final Scolarite_service scolariteService;

    private final StudentStatistique_service studentStatistique_service;
}
