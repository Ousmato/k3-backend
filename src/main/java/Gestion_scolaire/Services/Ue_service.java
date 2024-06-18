package Gestion_scolaire.Services;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Models.UE;
import Gestion_scolaire.Repositories.ClasseModule_repositorie;
import Gestion_scolaire.Repositories.Classe_repositorie;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Ue_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Ue_service {

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    public UE add(UE ue){
        UE uEexist = ue_repositorie.findByNomUE(ue.getNomUE());
        if (uEexist != null){
            throw new RuntimeException("Ce nom Existe deja");
        }else {
            return ue_repositorie.save(ue);
        }
    }
//    ------------------------------------------------methode pour appeler la liste des ues-------------
    public List<UE> readAll(long idClasse){
        List<UE> list = ue_repositorie.findAll();
        List<UE> ueNewList = new ArrayList<>();

            for (UE ue : list) {
                        ClasseModule cm = classeModule_repositorie.findByIdStudentClasseIdAndIdUEId(idClasse, ue.getId());
                     if (cm == null){
                            ueNewList.add(ue);
                     }
            }
        return ueNewList;
    }
//    ----------------------------------------------------------supprimer une UE------------
    public String delete(long id){
        UE UeExist = ue_repositorie.findById(id);
        if (UeExist != null){
            List<Modules> modulesList = modules_repositories.findByIdUeId(UeExist.getId());
            if (modulesList.isEmpty()){
                ue_repositorie.delete(UeExist);
                return "delete sucessfly";
            }else {
                throw new RuntimeException("cette UE a deja des module impossible de le supprimer");
            }

        }else {
            throw new RuntimeException("UE non valid");
        }
    }
//    ---------------------------------method pour modifier l'ue---------------------------
    public UE update(UE ue){
        UE ueExist = ue_repositorie.findById(ue.getId());
        if (ueExist != null){
            ueExist.setNomUE(ue.getNomUE());
            return ue_repositorie.save(ueExist);
        }else {
            throw new RuntimeException("UE n'existe pas");
        }

    }

}
