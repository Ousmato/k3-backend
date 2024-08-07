package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Modules_service {

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Note_service note_service;

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
    public Object addModule(Modules module){
        Modules modulesExist = modules_repositories.findByIdUeAndNomModule(module.getIdUe(), module.getNomModule());
        if(modulesExist == null){
             modules_repositories.save(module);
             return DTO_response_string.fromMessage("Ajout effectué avec succè",200);
        }
        throw new RuntimeException("ce module existe deja");
    }

//    ---------------------------------------method pour appeler la liste-----------------------------
    public List<Modules> readAll(){
      return  modules_repositories.findAll();

    }
//    ----------------------------------------------methode pour appeler les modules par Ue----------------
    public List<Modules> readByUe(long idUe){
        List<Modules> modulesList = modules_repositories.findByIdUeId(idUe);
        if (modulesList.isEmpty()){
            return new ArrayList<>();
        }
        return  modulesList;
    }
//    -------------------------------------methode pour modifier le module qui n'as pas encore des notes
    public Object update(Modules module){
        List<Notes> notesList = note_service.getNotesByIdModule(module.getId());
        System.out.println(module.getId() + "les notes");
        if(notesList.isEmpty()){

            Modules moduleExist = modules_repositories.findByIdUeIdAndId(module.getIdUe().getId(), module.getId());
            System.out.println(moduleExist);
            if(moduleExist !=null){

                moduleExist.setNomModule(module.getNomModule());
                moduleExist.setCoefficient(module.getCoefficient());
                modules_repositories.save(moduleExist);
                return DTO_response_string.fromMessage("Mise à jour effectuer avec succè", 200);
            }

        }
        throw new NoteFundException("Le module n'est peut pas etre modifier en raison de notes deja associer");
    }
}
