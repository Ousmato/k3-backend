package Gestion_scolaire.Shareds;

import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Emplois.repositorie.Emplois_repositorie;
import Gestion_scolaire.Emplois.repositorie.Journee_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.Filiere_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.NiveauFiliere_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.Niveau_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.SousFilieres_repositorie;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.Teachers.repositories.Specialite_repositorie;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.students.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class Shared_repositories {

    private final Ue_repositorie ue_repositorie;

    private final  Modules_repositories modules_repositories;

    private final  Participant_repositorie participant_repositorie;

    
    private final StudentGroup_repositorie studentGroup_repositorie;

    
    private final Sessions_repositorie sessions_repositorie;

    
    private final Semestre_repositorie semestre_repositorie;

    
    private final Notes_repositorie notes_repositorie;

    
    private final Moyenne_repositorie moyenne_repositorie;

    
    private final Journee_repositorie journee_repositorie;

    
    private final Salles_repositorie salles_repositorie;

    
    private final Emplois_repositorie emplois_repositorie;

    
    private final Students_repositorie students_repositorie;

    
    private final Classe_repositorie classe_repositorie;

    
    private final Inscription_repositorie inscription_repositorie;

    
    private final AnneeScolaire_repositorie anneeScolaire_repositorie;

    
    private final AdminRepositorie adminRepositorie;

    
    private final Teacher_repositorie teacher_repositorie;

    
    private final Specialite_repositorie specialite_repositorie;

    
    private final Niveau_repositorie niveau_repositorie;

    
    private final Filiere_repositorie filiere_repositorie;

    
    private final NiveauFiliere_repositorie niveauFiliere_repositorie;

    
    private final SousFilieres_repositorie sousFilieres_repositorie;

    
    private final InscriptionSousFiliere_repositorie inscriptionSousFiliere_repositorie;

    
    private final Paiement_repositorie paiement_repositorie;


}
