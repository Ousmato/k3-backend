package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionAutomaticAdd_service {
    @Autowired
   private Niveau_service niveau_service;

    @Autowired
   private Filieres_service filieres_service;

    @Autowired
    private Classe_service classe_service;

    @Autowired
    private InfoScool_service infoScool_service;


    @Transactional
    public Object autoPromotionForAllStudentClasses(AnneeScolaire anneeScolaire){
        List<Niveau> allNiveau = niveau_service.readAll();
        List<Filiere> allFilieres =filieres_service.getFilieres();

        AnneeScolaire newPromotion = infoScool_service.add_anneeScolaire(anneeScolaire);

        for (Filiere filiere : allFilieres) {
            for (Niveau niveau : allNiveau) {
                NiveauFilieres niveauFilieres = filieres_service.add(filiere, niveau, newPromotion);

                classe_service.addProClasse(niveauFilieres.getId(),newPromotion.getId());
            }


        }
        return DTO_response_string.fromMessage("Ajout effectué avec succès ", 200);

    }
}
