package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.dtos.AddUeDTO;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Modules_service {


    @Autowired
    private Shared_repositories shared_repositories;


//    ---------------------------------------method pour appeler la liste-----------------------------
    public List<Modules> readAll(){
      return  shared_repositories.getModules_repositories().findAll();

    }
//    ----------------------------------------------methode pour appeler les modules par Ue----------------
    public List<Modules> readByUe(long idUe){
        List<Modules> modulesList = shared_repositories.getModules_repositories().findByIdUeIdAndActive(idUe, true);
        if (modulesList.isEmpty()){
            return new ArrayList<>();
        }
        return  modulesList;
    }
//    -------------------------------------methode pour modifier le module qui n'as pas encore des notes
    public Object addModule(Modules module){
        UE ue = shared_repositories.getUe_repositorie().findById(module.getIdUe().getId());
        if (ue == null){
            throw new NoteFundException("UE n'existe pas");
        }
        Modules moduleExist = shared_repositories.getModules_repositories().getByIdUeIdAndNomModuleAndActive(module.getIdUe().getId(), module.getNomModule(), true);
        System.out.println(moduleExist);
        if(moduleExist !=null){
            throw new NoteFundException("Le module : " + moduleExist.getNomModule() + " existe déjà pour cette UE");

        }
        shared_repositories.getModules_repositories().save(module);
        return DTO_response_string.fromMessage("Mise à jour effectuer avec succè");

    }


    public List<Modules> getModulesByIdclasseAndIdSemestre(long idclasse, long idSemestre){
        List<UE> ues = shared_repositories.getUe_repositorie().findByIdClasse(idclasse, idSemestre);
        List<Modules> modulesList = new ArrayList<>();

        List<Modules> modules = shared_repositories.getModules_repositories().allModulesHasNotProgram(idclasse, idSemestre);
//        System.out.println("-------------------------size-------------" +modules.size());
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
        Semestres semestre = shared_repositories.getSemestre_repositorie().findById(idSemestre);
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
        Modules module = shared_repositories.getModules_repositories().findById(id);
        if (module == null) {
            throw new RuntimeException("le module n'existe pas");
        }
        return module;

    }

    public List<Modules> allModulesWithNotes(long idStudent,long idSemestre){
        List<Modules> modulesList = shared_repositories.getModules_repositories().allModuleWithNote(idStudent, idSemestre);
        if(modulesList.isEmpty()){
            return new ArrayList<>();
        }else {
            return modulesList;
        }
    }
    public List<Modules> allModulesOfClassByIdSemestre(long idSemestre, long idClasse){
        List<Modules> modulesList = shared_repositories.getModules_repositories().allModulesOfClassBySemestre(idSemestre, idClasse);
        if (modulesList.isEmpty()){
            return new ArrayList<>();
        }
        return modulesList;
    }

    public Object deleteById(long idModule){
        Modules module = getModule(idModule);
        module.setActive(false);
        shared_repositories.getModules_repositories().save(module);
        return DTO_response_string.updateMessage();

    }



}
