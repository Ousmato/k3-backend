package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.dtos.AddUeDTO;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Classes.services.Methods_shared;
import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Semestre_repositorie;
import Gestion_scolaire.Repositories.Ue_repositorie;
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
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Ue_repositorie ue_repositorie;


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
//        long idSemestre = 1;
//        List<Notes> notesList = methods_shared.getNote_service().getNotesByIdModule(module.getId(), idSemestre);
        System.out.println(module.getId() + "les notes");
//        if(notesList.isEmpty()){

            Modules moduleExist = modules_repositories.findByIdUeIdAndId(module.getIdUe().getId(), module.getId());
            System.out.println(moduleExist);
            if(moduleExist !=null){

                moduleExist.setNomModule(module.getNomModule());
                moduleExist.setCoefficient(module.getCoefficient());
                modules_repositories.save(moduleExist);
                return DTO_response_string.fromMessage("Mise à jour effectuer avec succè");
            }

//        }
        throw new NoteFundException("Le module n'est peut pas etre modifier en raison de notes deja associer");
    }


    public List<Modules> getModulesByIdclasseAndIdSemestre(long idclasse, long idSemestre){
        List<UE> ues = ue_repositorie.findByIdClasse(idclasse, idSemestre);
        List<Modules> modulesList = new ArrayList<>();

        List<Modules> modules = modules_repositories.allModulesHasNotProgram(idclasse, idSemestre);
        System.out.println("-------------------------size-------------" +modules.size());
        modulesList = modules.stream()
                .filter(modules1 -> ues.stream()
                        .anyMatch(ue -> ue.equals(modules1.getIdUe())))
                .toList();

        if (modulesList.isEmpty()){
            return new ArrayList<>();
        }
        return  modulesList;
    }

    public List<GetInputNoteInscritDTO> getModulesWithoutEmploi(long idClasse, long idSemestre) {
        List<Modules> modulesList = getModulesByIdclasseAndIdSemestre(idClasse, idSemestre);
        Semestres semestre = semestre_repositorie.findById(idSemestre);
        List<GetInputNoteInscritDTO> list = new ArrayList<>();

        if (modulesList.isEmpty()) {
            return list;
        }

        modulesList.forEach(mod -> {
            GetInputNoteInscritDTO getInputNoteInscritDTO = new GetInputNoteInscritDTO();
            AddUeDTO addUeDTO = new AddUeDTO();
            getInputNoteInscritDTO.setIdModule(mod.getId());
            getInputNoteInscritDTO.setExamNote(0.0);
            getInputNoteInscritDTO.setClasseNote(0.0);
            addUeDTO.setIdUe(mod.getIdUe());
            addUeDTO.setSemestre(semestre);
            addUeDTO.setModules(modulesList);
//            addNoteDTO.setAddUeDto(addUeDTO);
            list.add(getInputNoteInscritDTO);
        });

        return list;
    }

    public Modules getModule(long id) {
        Modules module = modules_repositories.findById(id);
        if (module == null) {
            throw new RuntimeException("le module n'existe pas");
        }
        return module;

    }

    public List<Modules> allModulesWithNotes(long idStudent,long idSemestre){
        List<Modules> modulesList = modules_repositories.allModuleWithNote(idStudent, idSemestre);
        if(modulesList.isEmpty()){
            return new ArrayList<>();
        }else {
            return modulesList;
        }
    }
    public List<Modules> allModulesOfClassByIdSemestre(long idSemestre, long idNivFiliere){
        List<Modules> modulesList = modules_repositories.allModulesOfClassBySemestre(idSemestre, idNivFiliere);
        if (modulesList.isEmpty()){
            return new ArrayList<>();
        }
        return modulesList;
    }


}
