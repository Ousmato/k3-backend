package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Repositories.Modules_repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Modules_service {

    @Autowired
    private Modules_repositories modules_repositories;

    public List<Modules> add(List<Modules> modules){
        List<Modules> list = new ArrayList<>();
        for (Modules module: modules){
            Modules modulesExist = modules_repositories.findByIdUeAndNomModule(module.getIdUe(), module.getNomModule());
            if(modulesExist == null){
                 list.add(modules_repositories.save(module));
            }else {
                throw new RuntimeException("Module exist deja");
            }
        }
    return list;
    }
//    -----------------------------------------------method pour ajouter un module dans la class module-----------
    public Modules addModule(Modules module){
        Modules modulesExist = modules_repositories.findByIdUeAndNomModule(module.getIdUe(), module.getNomModule());
        if(modulesExist == null){
            return modules_repositories.save(module);
        }
        throw new RuntimeException("ce module existe deja");
    }

//    -------------------------------------------------method pour modifier------------------------------
    public Modules update(Modules modules){
        Modules modulesExist = modules_repositories.findByIdUeAndNomModule(modules.getIdUe(), modules.getNomModule());
        if(modulesExist != null){
            modulesExist.setNomModule(modules.getNomModule());
            modulesExist.setCoefficient(modules.getCoefficient());
            modulesExist.setIdUe(modules.getIdUe());
            return modules_repositories.save(modulesExist);
        }else {
            throw new RuntimeException("module n'existe pas");
        }

    }
//    ---------------------------------------method pour appeler la liste-----------------------------
    public List<Modules> readAll(){
      return  modules_repositories.findAll();

    }
//    ----------------------------------------------methode pour appeler les modules par Ue----------------
    public List<Modules> readByUe(long idUe){
        List<Modules> modulesList = modules_repositories.findByIdUeId(idUe);
        return  modulesList;
    }
//    -------------------------------------
}
