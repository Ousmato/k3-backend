package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Services.Niveau_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/niveau")
public class Niveau_controller {

    @Autowired
    private Niveau_service niveau_service;

    @GetMapping("/readAll")
    public List<Niveau> getAll(){
       return niveau_service.readAll();
    }
}
