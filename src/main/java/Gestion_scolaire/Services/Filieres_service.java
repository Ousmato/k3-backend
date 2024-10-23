package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.ClasseDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.NivauFilierDTO;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    @PostConstruct
    public void init() {
        if (filiere_repositorie.findAll().isEmpty()) {
            defaultFiliere().forEach(filiereName -> {
                Filiere filiere = new Filiere();
                filiere.setNomFiliere(filiereName);
                filiere_repositorie.save(filiere);
            });
        }
    }


    public NiveauFilieres add(Filiere filiere, Niveau niveau, AnneeScolaire annee){
         // Vérification de la duplication de la relation NiveauFilieres
       List<NiveauFilieres> existingNivFiliere = niveauFiliere_repositorie.getAllByIdFiliereIdAndIdNiveauId(filiere.getId(), niveau.getId());
       if(!existingNivFiliere.isEmpty()){
           boolean hasExist = false;
           for(NiveauFilieres nivFiliere : existingNivFiliere){
               StudentsClasse classe = classe_repositorie.findByIdFiliereIdAndIdAnneeScolaireId(nivFiliere.getId(),annee.getId());
              if(classe != null){

                  hasExist = true;
                  break;
              }

           }
           if (hasExist) {
               throw new NoteFundException("Attention la classe " + niveau.getNom() +" "+  filiere.getNomFiliere()+" existe déjà");
           }
       }

        // Créer une nouvelle relation NiveauFilieres
        NiveauFilieres niveauFilieres = new NiveauFilieres();
        niveauFilieres.setIdNiveau(niveau);
        niveauFilieres.setIdFiliere(filiere);
        niveauFilieres.setScolarite(300000);


        // Sauvegarder la nouvelle relation
       return niveauFiliere_repositorie.save(niveauFilieres);
    }
//    -------------------------------------------list of all niveau filiere-----------------------
    public List<NiveauFilieres> readNivFil(){
        List<NiveauFilieres> list =  niveauFiliere_repositorie.findAll();


        List<StudentsClasse> classesS = classe_repositorie.getClasseForCurrentYear(LocalDate.now().getYear());
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
    public Object create(Filiere filiere){
        Filiere filiereExist = filiere_repositorie.findByNomFiliere(filiere.getNomFiliere());
        if (filiereExist == null){
            filiere_repositorie.save(filiere);
            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);

        }
        throw new NoteFundException("Attention la filière existe déjà");
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

    //------------------------update filiere
    public Object updateFilirer(Filiere filiere){
        Filiere filiExist = filiere_repositorie.findById(filiere.getId());
        if(filiExist != null){
            List<StudentsClasse> classeList = classe_repositorie.findByIdFiliereId(filiere.getId());
            if (!classeList.isEmpty()) {
                throw new NoteFundException("La filière ne peut pas etre modifier des classes sont déjà associé");
            }
            if(filiere.getNomFiliere().equals(filiExist.getNomFiliere())){
                throw new NoteFundException("Aucune Mises à jours n'est effectué ");
            }
            filiExist.setNomFiliere(filiere.getNomFiliere());
            filiere_repositorie.save(filiExist);
            return DTO_response_string.fromMessage("Mises à jour effectué avec succès", 200);

        }
        throw new NoteFundException("La filière n'existe pas ");
    }

    //   --------------------------------get all filiere
    public List<Filiere> getFilieres(){

        List<Filiere> list = filiere_repositorie.findAll();
        list.sort(Comparator.comparing(Filiere::getNomFiliere));
        return list;
    }

    //-------------------------------------------delete filiere
    public Object deleteFiliere(long idFiliere){
        Filiere filiere = filiere_repositorie.findById(idFiliere);
        List<StudentsClasse> classe = classe_repositorie.findByIdFiliereId(idFiliere);
        if(filiere != null){
            if(!classe.isEmpty()){
                throw new NoteFundException("Suppression impossible des classes sont déjà associé");
            }
            List<NiveauFilieres> niveauFilieres = niveauFiliere_repositorie.findByIdFiliereId(filiere.getId());

            if(!niveauFilieres.isEmpty()){
                throw new NoteFundException("Suppression impossible des niveaux sont déjà associé");
            }
            filiere_repositorie.delete(filiere);
            return DTO_response_string.fromMessage("Suppression effectué avec succès", 200);
        }
        throw new NoteFundException("La filière n'existe pas ");
    }


    private List<String> defaultFiliere(){
        return Arrays.asList(
               "Marketing et Communication",
               "Comptabilité Finance et Audit",
               "Machinisme Agricole",
               "Eaux Environnement et Energies Rénouvelable",
               "Hotellerie et Tourisme",
               "Génie Informatique",
               "Assistant de Gestion",
               "Agro Business"
       );
    }


}
