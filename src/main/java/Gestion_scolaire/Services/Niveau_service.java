package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Repositories.Classe_repositorie;
import Gestion_scolaire.Repositories.Niveau_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Niveau_service {

    @Autowired
    private Niveau_repositorie niveau_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;


//    ===============================================methode get All niveau==============================
    public List<Niveau> readAll(){
        return niveau_repositorie.findAll();
    }

    public Niveau getByIdNiveau(long idNiveau){
        return niveau_repositorie.findById(idNiveau);
    }

//    ----------------------------------------add niveau
    public Object addNiveau(Niveau niveau){
        Niveau n = niveau_repositorie.findByNom(niveau.getNom());
        if(n != null){
            throw new NoteFundException("Ce niveau exist déjà");
        }
        niveau_repositorie.save(niveau);
        return DTO_response_string.fromMessage("Ajout éffectué avec succès", 200);
    }

    // --------------------------------update
    public Object updateNiveau(Niveau niveau){
        Niveau n = niveau_repositorie.findById(niveau.getId());
       if(n != null){
           List<StudentsClasse> classeList = classe_repositorie.findByIdFiliereIdNiveauId(n.getId());

           if(!classeList.isEmpty()){
               throw new NoteFundException("Impossible de modifier car des classes sont déjà associer");
           }
           n.setNom(niveau.getNom());
           niveau_repositorie.save(n);
           return DTO_response_string.fromMessage("Mise à éffectué avec succès", 200);

       }
        throw new NoteFundException("Le niveau est introuvable");
    }

    public Object deleteNiveau(long idNiveau){
        Niveau n = niveau_repositorie.findById(idNiveau);
        if(n != null){
            List<StudentsClasse> classeList = classe_repositorie.findByIdFiliereIdNiveauId(n.getId());
            if(!classeList.isEmpty()){
                throw new NoteFundException("Suppression impossible des classes sont déjà associé");
            }
            niveau_repositorie.delete(n);
            return DTO_response_string.fromMessage("Suppression effectué avec succès", 200);
        }
        throw new NoteFundException("Le niveau est introuvable");
    }
}
