package Gestion_scolaire.Services;

import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.ClasseModule_repositorie;
import Gestion_scolaire.Repositories.Classe_repositorie;
import Gestion_scolaire.Repositories.NiveauFiliere_repositorie;
import Gestion_scolaire.Repositories.Students_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Classe_service {
    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private NiveauFiliere_repositorie niveauFiliere_repositorie;


    public List<ClasseModule> add(List<ClasseModule> classeModules) {
        List<ClasseModule> addedModules = new ArrayList<>();

        for (ClasseModule module : classeModules) {
            ClasseModule existingModule = classeModule_repositorie.findByIdStudentClasseIdAndIdUEId(
                    module.getIdStudentClasse().getId(), module.getIdUE().getId());

            if (existingModule == null) {
                addedModules.add(classeModule_repositorie.save(module));
            }
        }

        return addedModules;
    }

    //        ----------------------------------------methode pour appeler tout les module de la class----------------
    public List<ClasseModule> readById(long id){
        List<ClasseModule> uEslist = classeModule_repositorie.findAllByIdStudentClasseId(id);
        return uEslist;
    }

//    ------------------------------------------------------------------------------------------

    public String create(StudentsClasse classe){
        NiveauFilieres filiereExist = niveauFiliere_repositorie.findByIdFiliereAndIdNiveau(classe.getIdFiliere().getIdFiliere(),classe.getIdFiliere().getIdNiveau());
        if(filiereExist != null){
            System.out.println("je suis la hoo");
            classe.setIdFiliere(classe.getIdFiliere());
            classe_repositorie.save(classe);

        }
        return "ajout avec success";
    }
//    ---------------------------------------get class in class Module-----------------------------
//    public ClasseModule getClass(long id){
//
//    }

//    =====================================method pour fermer une classe====================================================

    public String fermer(long id){
        StudentsClasse classeExist = classe_repositorie.findById(id);
        if (classeExist != null){
            classeExist.setFermer(!classeExist.isFermer());
            classe_repositorie.save(classeExist);
        }
        return "Successful";
    }
    //    ------------------------------------------------------method pour appeler tout les classe ouverte-------
    public List<StudentsClasse> readAllClass(){
        return classe_repositorie.findAll();
    }
    //-------------------------------------------methode pour appeler une classe par id---------------
    public StudentsClasse readByIdClasse(long id){
        return classe_repositorie.findById(id);
    }
// ----------------------------------------get ue list by id class in classe module---------------
    public ClasseModule getClass(long id){
        return classeModule_repositorie.findStudentsClasseWithUEsById(id);
    }

//    ------------------------------------------------------update student classe methode
    public StudentsClasse update(StudentsClasse classe){
        StudentsClasse classExist = classe_repositorie.findById(classe.getId());
        if (classExist != null){
            List<Studens> list = students_repositorie.findByIdClasseIdAndActive(classExist.getId(), true);
            if(!list.isEmpty()){
                Studens studentscolariteMax = list.get(0);

                for (Studens st: list){
                    if(st.getScolarite() > studentscolariteMax.getScolarite());
                    studentscolariteMax = st;

                }
                if (studentscolariteMax.getScolarite() > classe.getScolarite()) {
                    throw new RuntimeException("La scolarité ne doit pas être inférieure à la scolarité maximale des étudiants");
                }

            }

            classExist.setScolarite(classe.getScolarite());
           return classe_repositorie.save(classExist);
        }
        throw new RuntimeException("classe does not exist");
    }
}
