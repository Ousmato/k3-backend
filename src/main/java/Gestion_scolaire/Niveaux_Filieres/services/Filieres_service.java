package Gestion_scolaire.Niveaux_Filieres.services;

import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Classes.services.JsonDataService;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Niveaux_Filieres.dtos.FiliereDTO;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Niveaux_Filieres.repositories.Filiere_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.NiveauFiliere_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.Niveau_repositorie;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.students.entity.TranchePaiement;
import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.students.repositories.TranchementPaiement_repositorie;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Filieres_service {

    private final Shared_repositories shared_repositories;
    private final JsonDataService jsonDataService;


    @Transactional
    @PostConstruct
    public void init() {
        try {
            if (shared_repositories.getFiliere_repositorie().findAll().isEmpty()) {
                List<Type_status> tpes = Arrays.asList(Type_status.values());
                List<TranchePaiement> tranchePaiements = TranchePaiement.init(tpes);
                shared_repositories.getTranchementPaiement_repositorie().saveAll(tranchePaiements);
                System.out.println(tranchePaiements.size());
                List<FiliereDTO> filieresJson = jsonDataService.readJsonFilieres();
                filieresJson.stream().forEach(filiereName -> {
                    try {
                        Filiere filiere = new Filiere();
                        filiere.setFaculte(filiereName.getFaculte());
                        filiere.setNomFiliere(filiereName.getNomFiliere());
                        shared_repositories.getFiliere_repositorie().save(filiere);
                    } catch (Exception e) {
                        // Log de l'erreur pour la filière qui pose problème
                        System.err.println("Erreur lors de l'ajout de la filière: " + filiereName);
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            // Log d'erreur global
            e.printStackTrace();
        }
    }



    public NiveauFilieres add(Filiere filiere, Niveau niveau, AnneeScolaire annee){
         // Vérification de la duplication de la relation NiveauFilieres
       List<NiveauFilieres> existingNivFiliere = shared_repositories.getNiveauFiliere_repositorie().getAllByIdFiliereIdAndIdNiveauId(filiere.getId(), niveau.getId());
       if(!existingNivFiliere.isEmpty()){
           boolean hasExist = false;
           for(NiveauFilieres nivFiliere : existingNivFiliere){
               StudentsClasse classe = shared_repositories.getClasse_repositorie().findByIdFiliereIdAndIdAnneeScolaireId(nivFiliere.getId(),annee.getId());
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

        // Sauvegarder la nouvelle relation
       return shared_repositories.getNiveauFiliere_repositorie().save(niveauFilieres);
    }
//----------------------------------------------------methode create filiere-----------------------------
    public Object create(Filiere filiere){
        Filiere filiereExist = shared_repositories.getFiliere_repositorie().findByNomFiliere(filiere.getNomFiliere());
        if (filiereExist == null){
            shared_repositories.getFiliere_repositorie().save(filiere);
            return DTO_response_string.fromMessage("Ajout effectué avec succès");

        }
        throw new NoteFundException("Attention la filière existe déjà");
    }

//----------------------------------------method update niveau filiere
    public Object update(NiveauFilieres niveauFilieres){

         NiveauFilieres niveauExist = shared_repositories.getNiveauFiliere_repositorie().findByIdFiliereIdAndIdNiveauId(
                niveauFilieres.getIdFiliere().getId(),niveauFilieres.getIdNiveau().getId());

        if(niveauExist != null){

            Filiere filiereExist = shared_repositories.getFiliere_repositorie().findById(niveauExist.getIdFiliere().getId());

            if(filiereExist != null){
                filiereExist.setNomFiliere(niveauFilieres.getIdFiliere().getNomFiliere());
                shared_repositories.getFiliere_repositorie().save(niveauFilieres.getIdFiliere());
                niveauExist.setIdFiliere(filiereExist);
            }
            Niveau nvExist = shared_repositories.getNiveau_repositorie().findById(niveauExist.getIdNiveau().getId());
            if (nvExist != null){
                nvExist.setNom(niveauFilieres.getIdNiveau().getNom());
                shared_repositories.getNiveau_repositorie().save(nvExist);
                niveauExist.setIdNiveau(nvExist);
            }

            shared_repositories.getNiveauFiliere_repositorie().save(niveauExist);

            return DTO_response_string.fromMessage("Ajout effectué avec succès");
        }
        throw new NoteFundException("Element n'existe pas ");

    }

    //------------------------update filiere
    public Object updateFilirer(Filiere filiere){
        Filiere filiExist = shared_repositories.getFiliere_repositorie().findById(filiere.getId());
        if(filiExist != null){
            List<StudentsClasse> classeList = shared_repositories.getClasse_repositorie().findByIdFiliereId(filiere.getId());
            if (!classeList.isEmpty()) {
                throw new NoteFundException("La filière ne peut pas etre modifier des classes sont déjà associé");
            }
            if(filiere.getNomFiliere().equals(filiExist.getNomFiliere())){
                throw new NoteFundException("Aucune Mises à jours n'est effectué ");
            }
            filiExist.setNomFiliere(filiere.getNomFiliere());
            shared_repositories.getFiliere_repositorie().save(filiExist);
            return DTO_response_string.fromMessage("Mises à jour effectué avec succès");

        }
        throw new NoteFundException("La filière n'existe pas ");
    }

    //   --------------------------------get all filiere
    public List<Filiere> getFilieres(){

        List<Filiere> list = shared_repositories.getFiliere_repositorie().findAll();
        list.sort(Comparator.comparing(Filiere::getNomFiliere));
        return list;
    }

    public List<Filiere> getFilieresByFaculte(Facultes faculte){

        List<Filiere> list = shared_repositories.getFiliere_repositorie().findByFaculte(faculte);
        list.sort(Comparator.comparing(Filiere::getNomFiliere));
        return list;
    }

    //-------------------------------------------delete filiere
    public Object deleteFiliere(long idFiliere){
        Filiere filiere = shared_repositories.getFiliere_repositorie().findById(idFiliere);
        List<StudentsClasse> classe = shared_repositories.getClasse_repositorie().findByIdFiliereId(idFiliere);
        if(filiere != null){
            if(!classe.isEmpty()){
                throw new NoteFundException("Suppression impossible des classes sont déjà associé");
            }
            List<NiveauFilieres> niveauFilieres = shared_repositories.getNiveauFiliere_repositorie().findByIdFiliereId(filiere.getId());

            if(!niveauFilieres.isEmpty()){
                throw new NoteFundException("Suppression impossible des niveaux sont déjà associé");
            }
            shared_repositories.getFiliere_repositorie().delete(filiere);
            return DTO_response_string.fromMessage("Suppression effectué avec succès");
        }
        throw new NoteFundException("La filière n'existe pas ");
    }


}
