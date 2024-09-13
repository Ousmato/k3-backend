package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.Journee;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Services.Jounee_service;
import Gestion_scolaire.Services.Seance_service;
import Gestion_scolaire.Services.Serviellance_service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-seance")
public class Seance_controller {
    @Autowired
    private Seance_service seance_service;

    @Autowired
    private Serviellance_service serviellance_service;
    @Autowired
    private Common_service common_service;
    @Autowired
    private Jounee_service jounee_service;

//    @PostMapping("/add")
//    public Object addSeance(@RequestBody List<Seances> seancesList){
//            return seance_service.createSeance(seancesList);
//    }
//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    public List<Journee_DTO> getAllByIdEmlois(@PathVariable long idEmplois){

            return jounee_service.readByIdEmplois(idEmplois);
    }
//    ---------------------------------------method calculate nombre heure for month--------------------------
//    @GetMapping("/nbre-heure/{idTeacher}")
//    public List<Integer> getNobreHeureByMonth(@PathVariable long idTeacher){
//            return seance_service.getNbreBySeance(idTeacher);
//
//    }
//    ------------------------------------method delete seance
    @DeleteMapping("/delete/{idSeance}")
    public Object deletedSeance(@PathVariable long idSeance){
            return seance_service.deletedSeance(idSeance);
    }
//    ------------------------------------update seance
//    @PutMapping("/update")
//    public Object updateSeance(@RequestBody Seances seance){
//       return seance_service.update(seance);
//
//    }

//    @PostMapping("/add-dto")
//    @Operation(summary = "Ajouter une ou plusieurs seance et leurs configuration")
//    public Object add_dto(@RequestBody DTO dto){
//        return seance_service.create(dto);
//    }
    //    ------------get seance by id
    @GetMapping("/get-by-id/{idSeance}")
    public SeanceDTO get_by_idSeance(@PathVariable long idSeance){
        return seance_service.get_by_id(idSeance);
    }



    //    -------------------------------------------------------------------------
//    @GetMapping("/all-config-by-id-emploi/{idEmplois}")
//    public List<DTO_Config> getAll_config(@PathVariable long idEmplois){
//        return seance_service.getAllSeanceConfig(idEmplois);
//    }


    @PostMapping("/add-journee")
    @Operation(summary = "Ajout des journee pour un emplois")
    public Object add_jour(@RequestBody List<Journee> journeeList){
        return jounee_service.addJournee(journeeList);
    }
    //    -----------------------add config seance
//    @PostMapping("/add-config")
//    public Object add_config(@RequestBody List<SeanceConfig> configList){
//        return common_service.createConfig(configList);
//    }

    //    ------------------------get config seance by id
//    @GetMapping("/config-by-id/{idConfig}")
//    public DTO_Config get_by_idConfig(@PathVariable long idConfig){
//        return seance_service.getSeanceConfig(idConfig);
//    }
//
////    --------------------------get all seance config by exam or session  by curent seance active
//    @GetMapping("/all-secance-config-exam-or-session")
//    public List<DTO_Config> getAll_secance_config_exam_or_session(){
//        return seance_service.getAllSeanceConfig_byExaAndSess();
//    }

//    ------------------------------add surveillance
//    @PostMapping("/add-surveillance")
//    public Object addSurveillance(@RequestBody SurveillanceDTO surveillance){
//        List<Seances> seancesList = surveillance.getSeancesList();
//        List<SeanceConfig> seanceConfig = surveillance.getConfigList();
//        System.out.println("---------------------list seance-----------------"+seancesList);
//        return serviellance_service.addSurveillance(seancesList, seanceConfig);
//    }

    //----------------------
  
}
