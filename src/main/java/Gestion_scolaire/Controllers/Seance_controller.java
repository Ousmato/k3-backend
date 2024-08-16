package Gestion_scolaire.Controllers;

import Gestion_scolaire.Dto_classe.DTO_Config;
import Gestion_scolaire.Dto_classe.SeanceDTO;
import Gestion_scolaire.Dto_classe.SurveillanceDTO;
import Gestion_scolaire.Models.SeanceConfig;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Services.Seance_service;
import Gestion_scolaire.Services.Serviellance_service;
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

    @PostMapping("/add")
    public Object addSeance(@RequestBody List<Seances> seancesList){
            return seance_service.createSeance(seancesList);
    }
//    ------------------------method get All seance by emplois---------------------------------
    @GetMapping("/list/{idEmplois}")
    public List<SeanceDTO> getAllByIdEmlois(@PathVariable long idEmplois){

            return seance_service.readByIdEmplois(idEmplois);
    }
//    ---------------------------------------method calculate nombre heure for month--------------------------
    @GetMapping("/nbre-heure/{idTeacher}")
    public List<Integer> getNobreHeureByMonth(@PathVariable long idTeacher){
            return seance_service.getNbreBySeance(idTeacher);

    }
//    ------------------------------------method delete seance
    @DeleteMapping("/delete/{idSeance}")
    public Object deletedSeance(@PathVariable long idSeance){
            return seance_service.deletedSeance(idSeance);
    }
//    ------------------------------------update seance
    @PutMapping("/update")
    public Object updateSeance(@RequestBody Seances seance){
       return seance_service.update(seance);

    }

    //    ------------get seance by id
    @GetMapping("/get-by-id/{idSeance}")
    public SeanceDTO get_by_idSeance(@PathVariable long idSeance){
        return seance_service.get_by_id(idSeance);
    }



    //    -------------------------------------------------------------------------
    @GetMapping("/all-config-by-id-emploi/{idEmplois}")
    public List<DTO_Config> getAll_config(@PathVariable long idEmplois){
        return seance_service.getAllSeanceConfig(idEmplois);
    }


    //    -----------------------add config seance
    @PostMapping("/add-config")
    public Object add_config(@RequestBody List<SeanceConfig> configList){
        return common_service.createConfig(configList);
    }

    //    ------------------------get config seance by id
    @GetMapping("/config-by-id/{idConfig}")
    public SeanceConfig get_by_idConfig(@PathVariable long idConfig){
        return seance_service.getSeanceConfig(idConfig);
    }

//    --------------------------get all seance config by exam or session  by curent seance active
    @GetMapping("/all-secance-config-exam-or-session")
    public List<DTO_Config> getAll_secance_config_exam_or_session(){
        return seance_service.getAllSeanceConfig_byExaAndSess();
    }

//    ------------------------------add surveillance
    @PostMapping("/add-surveillance")
    public Object addSurveillance(@RequestBody SurveillanceDTO surveillance){
        List<Seances> seancesList = surveillance.getSeancesList();
        List<SeanceConfig> seanceConfig = surveillance.getConfigList();
        System.out.println("---------------------list seance-----------------"+seancesList);
        return serviellance_service.addSurveillance(seancesList, seanceConfig);
    }
}
