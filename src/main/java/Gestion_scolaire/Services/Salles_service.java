package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Salles;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Repositories.Salles_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Salles_service {
    
    @Autowired
    private Salles_repositorie sallesRepositorie;

    @Autowired
    private Common_service common_service;
    
//    -------------------------liste des salles
    public List<Salles> getAllSalles_non_occuper() {
        List<Salles> sallesList = sallesRepositorie.findAll();
        List<Salles> newListe = new ArrayList<>();
        List<Salles> salleOccupe = common_service.salle_occuper();

        for (Salles salle : sallesList) {
            if (!salleOccupe.contains(salle)) {
                newListe.add(salle);
            }
        }

        return newListe;
    }
//    ---------------------------add salles
    public Object SaveSalle(Salles salles) {
        Salles salleExist = sallesRepositorie.findByNom(salles.getNom());
        if (salleExist != null) {
            throw  new NoteFundException("Cette existe déjà");
            
        }
        sallesRepositorie.save(salles);
        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
    }
//    ----------------------------------------get all salles
    public List<Salles> getAllSalles() {
        List<Salles> sallesList = sallesRepositorie.findAll();
        if (sallesList.isEmpty()) {
            return new ArrayList<>();
        }
        return sallesList;
    }

}
