package Gestion_scolaire.Teachers.controller;

import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.Teachers.entity.Surveillants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-surveillence")
@RequiredArgsConstructor
public class Surveillence_controller {

    private final Shared_services shared_services;

//    @GetMapping("/get-all-surveillence")
//    @Operation(summary = "Recuperation des surveillence")
//    public List<GetSurveillenceDto> getSurveillence() {
//
//    }

    @PostMapping("/add-surveillant")
    @Operation(summary = "Ajout de surveillance")
    public Object addSurveillant(@RequestBody Surveillants surveillant){
        return shared_services.getSurveillence_service().addSurveillant(surveillant);
    }

    @GetMapping("/get-all-surveillants")
    @Operation(summary = "Recuperation des surveillants")
    public List<Surveillants> getSurveillants() {
        return shared_services.getSurveillence_service().findAllSurveillants();
    }
}
