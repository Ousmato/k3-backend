package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Salles;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Services.Salles_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-salle")
public class Salles_controller {

    @Autowired
    private Salles_service salles_service;

    @Autowired
    private Common_service common_service;

    @PostMapping("/add")
    public Object add(@RequestBody Salles salles) {
        return salles_service.SaveSalle(salles);
    }
//    ----------------get list of all salle non occuper
    @GetMapping("/list-salle-non-occuper")
    public List<Salles> listSalle() {
        return salles_service.getAllSalles_non_occuper();
    }

    @GetMapping("/all-salles-ocuper")
    public List<Salles> allSallesOccuper() {
        return common_service.salle_occuper();
    }

    //    ----------------get list of all salle
    @GetMapping("/list-all-salle")
    public List<Salles> list_all_Salle() {
        return salles_service.getAllSalles();
    }
}
