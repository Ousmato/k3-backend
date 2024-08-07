package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.ClasseDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.NivauFilierDTO;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Filieres_service {

    @Autowired
    private Filiere_repositorie filiere_repositorie;

    @Autowired
    private NiveauFiliere_repositorie niveauFiliere_repositorie ;

    @Autowired
    private Niveau_repositorie niveau_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private Semestre_repositorie semestreRepositorie;

    public Object add(Filiere filiere, Niveau niveau){
         // Vérification de la duplication de la relation NiveauFilieres
        NiveauFilieres existingNivFiliere = niveauFiliere_repositorie.findByIdFiliereAndIdNiveau(filiere, niveau);
        if (existingNivFiliere != null) {
            throw new NoteFundException("Attention duplication de filiere");
        }
        // Créer une nouvelle relation NiveauFilieres
        NiveauFilieres niveauFilieres = new NiveauFilieres();
        niveauFilieres.setIdNiveau(niveau);
        niveauFilieres.setIdFiliere(filiere);

        // Sauvegarder la nouvelle relation
        niveauFiliere_repositorie.save(niveauFilieres);
        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
    }
//    -------------------------------------------list of all niveau filiere-----------------------
    public List<NiveauFilieres> readNivFil(){
        List<NiveauFilieres> list =  niveauFiliere_repositorie.findAll();
        List<StudentsClasse> classesS = classe_repositorie.findAll();
        List<NiveauFilieres> newClasses = new ArrayList<>();

        for (NiveauFilieres nivFiliere : list) {
            boolean hasClass = false;
            for (StudentsClasse studentsClasse : classesS) {
                if(nivFiliere.getIdFiliere().getId() == studentsClasse.getIdFiliere().getId()){
                    hasClass = true;
                    break;
                }
            }
            if (!hasClass) {
                newClasses.add(nivFiliere);  // Ajouter seulement si aucune association n'est trouvée
            }
        }
        return newClasses;
    }
//----------------------------------------------------methode create filiere-----------------------------
    public Filiere create(Filiere filiere){
        Filiere filiereExist = filiere_repositorie.findByNomFiliere(filiere.getNomFiliere());
        if (filiereExist != null){
            filiereExist.setNomFiliere(filiere.getNomFiliere());
            return filiere_repositorie.save(filiereExist);

        }else {
            return filiere_repositorie.save(filiere);
        }
    }

//----------------------------------------method update niveau filiere
    public Object update(NiveauFilieres niveauFilieres){

         NiveauFilieres niveauExist = niveauFiliere_repositorie.findByIdFiliereIdAndIdNiveauId(
                niveauFilieres.getIdFiliere().getId(),niveauFilieres.getIdNiveau().getId());

        if(niveauExist != null){

            Filiere filiereExist = filiere_repositorie.findById(niveauExist.getIdFiliere().getId());

            if(filiereExist != null){
                filiereExist.setNomFiliere(niveauFilieres.getIdFiliere().getNomFiliere());
                filiere_repositorie.save(niveauFilieres.getIdFiliere());
                niveauExist.setIdFiliere(filiereExist);
            }
            Niveau nvExist = niveau_repositorie.findById(niveauExist.getIdNiveau().getId());
            if (nvExist != null){
                nvExist.setNom(niveauFilieres.getIdNiveau().getNom());
                niveau_repositorie.save(nvExist);
                niveauExist.setIdNiveau(nvExist);
            }

            niveauFiliere_repositorie.save(niveauExist);

            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        }
        throw new NoteFundException("Element n'existe pas ");

    }
}
