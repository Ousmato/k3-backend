package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_ClassModule;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class Classe_service {
    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private Filieres_service filieresService;

    @Autowired
    private Niveau_repositorie niveau_repositorie;

    @Autowired
    private Filiere_repositorie filiere_repositorie;


    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private AnneeScolaire_repositorie annee_scolaire_repositorie;

    @Autowired
    private NiveauFiliere_repositorie niveauFiliere_repositorie;

//    ------------------------------------------------------------------------------------------

    public Object create(NiveauFilieres nvF){

        Niveau niveau = niveau_repositorie.findById(nvF.getIdNiveau().getId());
        if(niveau == null){
            throw new NoteFundException("Le niveau n'existe pas");
        }
        Filiere filiere = filiere_repositorie.findById(nvF.getIdFiliere().getId());
        if(filiere == null){
            throw new NoteFundException("La filière n'existe pas");
        }
      NiveauFilieres nivFiliereExist = niveauFiliere_repositorie.findByIdFiliereIdAndIdNiveauId(filiere.getId(), niveau.getId());
        if(nivFiliereExist != null){
            throw new NoteFundException("Cette mention : " +  nivFiliereExist.getIdNiveau().getNom() + nivFiliereExist.getIdFiliere().getNomFiliere() + "existe déjà");

        }

        niveauFiliere_repositorie.save(nvF);
        return DTO_response_string.fromMessage("Ajout effectué  avec succès", 200);
    }


//    =====================================method pour fermer une classe====================================================

    public String fermer(long id){
        StudentsClasse classeExist = classe_repositorie.findById(id);
        if (classeExist != null){
            classeExist.setFermer(!classeExist.isFermer());
            classe_repositorie.save(classeExist);
        }
        return "Ajout effectué  avec succès";
    }
    //    --------------------method pour appeler tous les classes ouverte-------
    public List<StudentsClasse> readAllClass(){
        List<StudentsClasse> classeList = classe_repositorie.getClasseForCurrentYear(LocalDate.now().getYear());
        System.out.println("---------------------" + classeList);
        if(!classeList.isEmpty()){
            for(StudentsClasse classe : classeList){
                int studentInscrit = students_repositorie.countAllByIdClasseIdAndIdClasseIdAnneeScolaireId(classe.getId(), classe.getIdAnneeScolaire().getId());
                classe.setEffectifs(studentInscrit);
            }
            classeList.sort(Comparator.comparing(classe ->classe.getIdFiliere().getIdFiliere().getNomFiliere()));

        }
        return classeList;
    }

    //---------------------------------------------------------------------------
    public List<StudentsClasse> readAllClassIdAnneeId(long idAnnee){
        return classe_repositorie.findByIdAnneeScolaireId(idAnnee);

    }

//    ----------------------------------cunt number of class
    public int cunt_class(){
        return classe_repositorie.countAllByFermer(false);
    }
    //-------------------------------------------methode pour appeler une classe par id---------------
    public StudentsClasse readByIdClasse(long id){
        return classe_repositorie.findById(id);
    }

//    ------------------------------------------------------update student classe methode
    public Object update(long idClasse, long idAnnee){

        StudentsClasse classExist = classe_repositorie.findById(idClasse);
        AnneeScolaire anneExist = annee_scolaire_repositorie.findById(idAnnee);
        if(anneExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        if (classExist != null){
            List<Studens> list = students_repositorie.findByIdClasseIdAndActive(classExist.getId(), true);
            if(!list.isEmpty()){
                throw new NoteFundException("La promotion ne peut pas etre modifier, des étudiants sont déjà inscrit ");

            }

           classExist.setIdAnneeScolaire(anneExist);
          classe_repositorie.save(classExist);

            return DTO_response_string.fromMessage("Mise à effectué  avec succès", 200);
        }
        throw new NoteFundException("classe  exist pas");
    }

    public List<StudentsClasse> getAllArchivesById(long idClasse){
       List<StudentsClasse> classeArchives = classe_repositorie.getAllArchivesByIdClasse(LocalDate.now().getYear(), idClasse);
        if(classeArchives.isEmpty()){
            return new ArrayList<>();
        }
        return classeArchives;
    }

    //    --------------------------all niveau/filiere
    public List<NiveauFilieres> getAllNiveauFilieres(){
//        List<NiveauFilieres> list =  niveauFiliere_repositorie.findAll();
//
//        List<NiveauFilieres> niveauFilieres = new ArrayList<>();
//        for(NiveauFilieres nivFiliere : list){
//           List<StudentsClasse> stc = classe_repositorie.findByIdFiliereId(nivFiliere.getId());
//           if(stc.isEmpty()){
//               niveauFilieres.add(nivFiliere);
//           }
//        }
//        return niveauFilieres;
        return  niveauFiliere_repositorie.findAll();
    }

    public Object addProClasse( long nivFiliere, long idAnnee){
        AnneeScolaire anneExist = annee_scolaire_repositorie.findById(idAnnee);
        if(anneExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        System.out.println( "----------------------------------------" + anneExist);
        NiveauFilieres nivFilieweExist = niveauFiliere_repositorie.findById(nivFiliere);
        if(nivFilieweExist == null){
            throw new NoteFundException("La mention n'existe pas");
        }
        StudentsClasse classeExist = classe_repositorie.findByIdFiliereIdAndIdAnneeScolaireId(nivFiliere,idAnnee);
        if(classeExist != null){
            throw new NoteFundException("La promotion pour cette mention existe déjà");
        }
        StudentsClasse newClasse = new StudentsClasse();
        newClasse.setIdAnneeScolaire(anneExist);
        newClasse.setIdFiliere(nivFilieweExist);
        classe_repositorie.save(newClasse);

        return DTO_response_string.fromMessage("Ajout effectué  avec succès", 200);

    }
    //    ----------------------------------------------------------

    public Object deleteProClasse(long idClasse){
        StudentsClasse classeExist = classe_repositorie.findById(idClasse);
        if(classeExist == null){
            throw new NoteFundException("La promotion n'existe pas");

        }
        if(classeExist.getEffectifs() == 0){
            classe_repositorie.delete(classeExist);
            return DTO_response_string.fromMessage("Suppression effectuer avec sucès", 200);
        }
        throw new NoteFundException("Impossible de supprimer une promotion qui a déjà des étudiants");
    }

    //  -----------------------------------------
    public Object updateNivFiliere(NiveauFilieres nvF){
        NiveauFilieres nvFExist = niveauFiliere_repositorie.findById(nvF.getId());
        if(nvFExist == null){
            throw new NoteFundException("La mention n'existe pas");
        }
        nvFExist.setScolarite(nvF.getScolarite());
        niveauFiliere_repositorie.save(nvFExist);
        return DTO_response_string.fromMessage("Mise à jours effectué avec succès", 200);
    }

    //    --------------------------------------appeler les classe superieur a la classe actuelle
    public List<StudentsClasse> getPreviousClasseById(long idClasse){
        StudentsClasse classeExist = readByIdClasse(idClasse);
        if(classeExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        AnneeScolaire oldAnnee = classeExist.getIdAnneeScolaire();
        int nextYear = oldAnnee.getFinAnnee().getYear() + 1;

        List<Niveau> niveauList = niveau_repositorie.findAll();
        Niveau nextNiveau = new Niveau();
        List<Integer> niveauLevel = new ArrayList<>();
        for(Niveau niveau : niveauList){
            switch (niveau.getNom()) {
                case "LICENCE 1" -> niveauLevel.add(1);
                case "LICENCE 2" -> niveauLevel.add(2);
                case "LICENCE 3" -> niveauLevel.add(3);
                case "MASTER 1" -> niveauLevel.add(4);
                case "MASTER 2" -> niveauLevel.add(5);
            }

        }
         // Trouver le niveau supérieur
        int finalCurrentLevel = getFinalCurrentLevel(classeExist);
        Integer nextLevel = niveauLevel.stream()
                .filter(level -> level > finalCurrentLevel) // Filtrer pour les niveaux supérieurs
                .findFirst() // Prendre le premier niveau supérieur
                .orElse(null); // Si aucun niveau supérieur n'est trouvé

//        Niveau nextNiveau = null;
        if (nextLevel != null) {
            for (Niveau niveau : niveauList) {
                if ((nextLevel == 1 && niveau.getNom().equals("LICENCE 1")) ||
                        (nextLevel == 2 && niveau.getNom().equals("LICENCE 2")) ||
                        (nextLevel == 3 && niveau.getNom().equals("LICENCE 3")) ||
                        (nextLevel == 4 && niveau.getNom().equals("MASTER 1")) ||
                        (nextLevel == 5 && niveau.getNom().equals("MASTER 2"))) {
                    nextNiveau = niveau;
                    break;
                }
            }
        }

        long idFiliereId = classeExist.getIdFiliere().getIdFiliere().getId();

        // Utiliser la requête pour récupérer la classe du même filière pour l'année suivante
       List<StudentsClasse> nextClasse = classe_repositorie.findByIdFiliereIdFiliereIdAndNextYear(nextYear, idFiliereId, nextNiveau.getId());

       List<StudentsClasse> newClassSup = new ArrayList<>();
        if (nextClasse.isEmpty()) {
            throw new NoteFundException("Aucune classe trouvée pour l'année suivante");
        }
        newClassSup.add(nextClasse.getFirst());
        // Retourner la classe supérieure trouvée
        return newClassSup;

    }


    //------------------
    public List<StudentsClasse> getAllCurrentClasseWithUe(){
        List<StudentsClasse> list = readAllClass();
        if(list.isEmpty()){
            return new ArrayList<>();
        }
        List<StudentsClasse> studentsClasses = new ArrayList<>();
        List<ClasseModule> listCM = classeModule_repositorie.getCurrentClasseModule(LocalDate.now().getYear());

        for(StudentsClasse classe : list){
            for(ClasseModule classeModule : listCM){
                if (classe.getIdFiliere().equals(classeModule.getIdNiveauFiliere())){
                    studentsClasses.add(classe);
                }
            }

        }
        studentsClasses.sort(Comparator.comparing(classe -> classe.getIdFiliere().getIdFiliere().getNomFiliere()));
        return studentsClasses;
    }

    private  int getFinalCurrentLevel(StudentsClasse classeExist) {
        Niveau oldNiv = classeExist.getIdFiliere().getIdNiveau();
        int currentLevel = 0;

// Trouver le niveau correspondant à l'ancien niveau
        switch (oldNiv.getNom()) {
            case "LICENCE 1" -> currentLevel = 1;
            case "LICENCE 2" -> currentLevel = 2;
            case "LICENCE 3" -> currentLevel = 3;
            case "MASTER 1" -> currentLevel = 4;
            case "MASTER 2" -> currentLevel = 5;
        }

        // Trouver le niveau supérieur
        return currentLevel;
    }

    //---------------------------------------------
    public List<StudentsClasse> getListClassForDepotDoc(long type){
        String nomNiveau1 = "LICENCE 2";
        String nomNiveau2 = "LICENCE 3";
        if (type == 1){
           List<StudentsClasse> listClasse = classe_repositorie.findByIdFiliereIdNiveauNom(nomNiveau1);
           if (listClasse.isEmpty()){
               return new ArrayList<>();
           }
           System.out.println("-------------licence 2-------------" + listClasse);
           return listClasse;
        }
        if (type == 2){
            List<StudentsClasse> listClasse = classe_repositorie.findByIdFiliereIdNiveauNom(nomNiveau2);
            if (listClasse.isEmpty()){
                return new ArrayList<>();
            }
            return listClasse;
        }
        throw new NoteFundException("Aucune classe trouver");
    }

}
