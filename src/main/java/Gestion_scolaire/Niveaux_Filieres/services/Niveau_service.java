package Gestion_scolaire.Niveaux_Filieres.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.Niveau_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class Niveau_service {

    @Autowired
    private Niveau_repositorie niveau_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @PostConstruct
    public void init(){
        List<Niveau> niveauList = niveau_repositorie.findAll();
        if(niveauList.isEmpty()){
            defaultNiveau().forEach(name ->{
                Niveau niveau = new Niveau();
                niveau.setNom(name);
                niveau_repositorie.save(niveau);
            });
        }
    }
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
        return DTO_response_string.fromMessage("Ajout éffectué avec succès");
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
           return DTO_response_string.fromMessage("Mise à éffectué avec succès");

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
            return DTO_response_string.fromMessage("Suppression effectué avec succès");
        }
        throw new NoteFundException("Le niveau est introuvable");
    }

    private List<String> defaultNiveau(){
        return  Arrays.asList(
                "LICENCE 1",
                "LICENCE 2",
                "LICENCE 3"
        );
    }
}
