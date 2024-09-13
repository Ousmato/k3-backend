package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class Serviellance_service {

    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
   private Common_service common_service;

    @Autowired
    private Paie_repositorie paie_repositorie;


//    public Object addSurveillance(List<Seances> seancesList, List<SeanceConfig> configList) {
//        for (Seances seances : seancesList) {
////           common_service.validateSeance(seances);
////
//            Seances savedSeance = seance_repositorie.save(seances);
//System.out.println("------------------------------------------"+savedSeance);
//            boolean configAdded = common_service.processSeanceConfigs(savedSeance, configList);
//
//            if (!configAdded) {
//                throw new NoteFundException("Une configuration existante est trouvée.");
//            }
//        }
//        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
//    }
//
//

}
