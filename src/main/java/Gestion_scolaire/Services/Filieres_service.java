package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Repositories.Filiere_repositorie;
import Gestion_scolaire.Repositories.NiveauFiliere_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Filieres_service {

    @Autowired
    private Filiere_repositorie filiere_repositorie;

    @Autowired
    private NiveauFiliere_repositorie niveauFiliere_repositorie ;

    public Object add(Filiere filiere, Niveau niveau){
         // Vérification de la duplication de la relation NiveauFilieres
        NiveauFilieres existingNivFiliere = niveauFiliere_repositorie.findByIdFiliereAndIdNiveau(filiere, niveau);
        if (existingNivFiliere != null) {
            throw new RuntimeException("Attention duplication de filiere");
        }
        // Créer une nouvelle relation NiveauFilieres
        NiveauFilieres niveauFilieres = new NiveauFilieres();
        System.out.println("create new nivFiliere---------------------------------------------");
        niveauFilieres.setIdNiveau(niveau);
        niveauFilieres.setIdFiliere(filiere);

        // Sauvegarder la nouvelle relation
        return niveauFiliere_repositorie.save(niveauFilieres);
    }
//    -------------------------------------------list of all niveau filiere-----------------------
    public List<NiveauFilieres> readNivFil(){
        return niveauFiliere_repositorie.findAll();
    }
//----------------------------------------------------methode create filiere-----------------------------
    public Filiere create(Filiere filiere){
        Filiere filiereExist = filiere_repositorie.findByNomFiliere(filiere.getNomFiliere());
        if (filiereExist != null){
            filiereExist.setNomFiliere(filiere.getNomFiliere());
            return filiere_repositorie.save(filiereExist);
//            throw  new  RuntimeException("ce nom de Filiere existe deja");

        }else {
            return filiere_repositorie.save(filiere);
        }
    }


}
