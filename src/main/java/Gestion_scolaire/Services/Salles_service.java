package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Salles;
import Gestion_scolaire.Repositories.Salles_repositorie;
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
        sallesList.sort((s1, s2) -> {
            String[] parts1 = s1.getNom().split("-");
            String[] parts2 = s2.getNom().split("-");

            // Comparer d'abord la partie alphabétique (ex: "E2" vs "E2")
            int comparePrefix = parts1[0].compareTo(parts2[0]);
            if (comparePrefix != 0) {
                return comparePrefix;
            }

            // Comparer la partie numérique (ex: 10 vs 2)
            Integer num1 = Integer.parseInt(parts1[1]);
            Integer num2 = Integer.parseInt(parts2[1]);
            return num1.compareTo(num2);
        });
        return sallesList;
    }

}
