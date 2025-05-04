package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.entity.Postes;
import Gestion_scolaire.Classes.dtos.StudentClasseDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentsClasse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Classe_service {

    private final Shared_repositories shared_repositories;


    private final Shared_methods_service shared_methods_service;

//    ------------------------------------------------------------------------------------------

    public Object create(NiveauFilieres nvF){

        Niveau niveau = shared_repositories.getNiveau_repositorie().findById(nvF.getIdNiveau().getId());
        if(niveau == null){
            throw new NoteFundException("Le niveau n'existe pas");
        }
        Filiere filiere = shared_repositories.getFiliere_repositorie().findById(nvF.getIdFiliere().getId());
        if(filiere == null){
            throw new NoteFundException("La filière n'existe pas");
        }
      NiveauFilieres nivFiliereExist = shared_repositories.getNiveauFiliere_repositorie().findByIdFiliereIdAndIdNiveauId(filiere.getId(), niveau.getId());
        if(nivFiliereExist != null){
            throw new NoteFundException("Cette mention : " +  nivFiliereExist.getIdNiveau().getNom() + nivFiliereExist.getIdFiliere().getNomFiliere() + "existe déjà");

        }

        shared_repositories.getNiveauFiliere_repositorie().save(nvF);
        return DTO_response_string.fromMessage("Ajout effectué avec succé");
    }


//    =====================================method pour fermer une classe====================================================

    public String fermer(long id){
        StudentsClasse classeExist = shared_repositories.getClasse_repositorie().findById(id);
        if (classeExist != null){
            classeExist.setFermer(!classeExist.isFermer());
            shared_repositories.getClasse_repositorie().save(classeExist);
        }
        return "Ajout effectué  avec succès";
    }
    //method pour appeler tous les classes ouverte-------
    public List<StudentClasseDTO> readAllClass(long idAdmin){

        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(LocalDate.now());
        //System.out.println("----------------current Year-----------" + currentYear);

      return readAllClassIdAnneeId(currentYear.getId(), idAdmin);

    }

    //---------------------------------------------------------------------------
    public List<StudentClasseDTO> readAllClassIdAnneeId(long idAnnee, long idAdmin) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        List<StudentsClasse> classes = shared_repositories.getClasse_repositorie().findByIdAnneeScolaireId(idAnnee);
        List<StudentClasseDTO> classeList = new ArrayList<>();

        Set<Long> addedClasseIds = new HashSet<>(); // pour éviter les doublons

        boolean hasFiliereType = false;

            TypeFiliere typeFiliere = administrationUsers.getIdPoste().getTypeFiliere();
            if (typeFiliere != null) {
                hasFiliereType = true;
                for (StudentsClasse cl : classes) {
                    TypeFiliere classeFiliereType = cl.getIdFiliere().getIdFiliere().getTypeFiliere();
                    if (classeFiliereType.equals(typeFiliere) && !addedClasseIds.contains(cl.getId())) {
                        int effectifs = shared_repositories.getInscription_repositorie().cuntByIdClasse(cl.getId());
                        StudentClasseDTO classDTO = StudentClasseDTO.toDto(cl);
                        classDTO.setEffectifs(effectifs);
                        classeList.add(classDTO);
                        addedClasseIds.add(cl.getId());
                    }
                }
            }
//        }

        // Si aucun des postes n'a de typeFiliere => on retourne toutes les classes
        if (!hasFiliereType) {
            for (StudentsClasse cl : classes) {
                if (!addedClasseIds.contains(cl.getId())) {
                    int effectifs = shared_repositories.getInscription_repositorie().cuntByIdClasse(cl.getId());
                    StudentClasseDTO classDTO = StudentClasseDTO.toDto(cl);
                    classDTO.setEffectifs(effectifs);
                    List<SousFilieres> specialiteFilieres = shared_repositories.getSousFilieres_repositorie().findByIdClasseId(cl.getId());
                    classDTO.setSpecialites(specialiteFilieres);
                    classeList.add(classDTO);
                    addedClasseIds.add(cl.getId());
                }
            }
        }

        return classeList;
    }





    //    ----------------------------------cunt number of class
    public int cunt_class(){
        LocalDate today = LocalDate.now();
        return shared_repositories.getClasse_repositorie().countAllByFermer(today.getYear(), false);
    }
    //-------------------------------------------methode pour appeler une classe par id---------------
    public StudentsClasse readByIdClasse(long id){
        StudentsClasse classe = shared_repositories.getClasse_repositorie().findById(id);
        if(classe == null){
            throw new NoteFundException("Le classe n'existe pas");
        }
        String nameNivabreg = shared_methods_service.abregNiveauName(classe.getIdFiliere().getIdNiveau().getNom());
        classe.getIdFiliere().getIdNiveau().setNom(nameNivabreg);
        return classe;
    }

    public StudentsClasse readByIdNivFiliere(long idNivFiliere){
        return shared_repositories.getClasse_repositorie().findStudentsClasseByIdFiliereId(idNivFiliere);
    }

//    ------------------------------------------------------update student classe methode
    public Object update(long idClasse, long idAnnee){

        StudentsClasse classExist = shared_repositories.getClasse_repositorie().findById(idClasse);
        AnneeScolaire anneExist = shared_repositories.getAnneeScolaire_repositorie().findById(idAnnee);
        if(anneExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        if (classExist != null){
            List<Inscription> list = shared_repositories.getInscription_repositorie().findByIdClasseIdAndActive(classExist.getId(), true);
            if(!list.isEmpty()){
                throw new NoteFundException("La promotion ne peut pas etre modifier, des étudiants sont déjà inscrit ");

            }

           classExist.setIdAnneeScolaire(anneExist);
            shared_repositories.getClasse_repositorie().save(classExist);

            return DTO_response_string.fromMessage("Mise à effectué  avec succès");
        }
        throw new NoteFundException("classe  exist pas");
    }

//    public List<StudentsClasse> getAllArchivesById(long idClasse){
//       List<StudentsClasse> classeArchives = shared_repositories.getClasse_repositorie().getAllArchivesByIdClasse(LocalDate.now().getYear(), idClasse);
//        if(classeArchives.isEmpty()){
//            return new ArrayList<>();
//        }
//        return classeArchives;
//    }

    //    --------------------------all niveau/filiere
    public List<NiveauFilieres> getAllNiveauFilieres(){

        return  shared_repositories.getNiveauFiliere_repositorie().findAll();
    }

    public StudentsClasse addProClasse( long nivFiliere, long idAnnee){
        AnneeScolaire anneExist = shared_repositories.getAnneeScolaire_repositorie().findById(idAnnee);
        if(anneExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        //System.out.println( "----------------------------------------" + anneExist);
        NiveauFilieres nivFilieweExist = shared_repositories.getNiveauFiliere_repositorie().findById(nivFiliere);
        if(nivFilieweExist == null){
            throw new NoteFundException("La mention n'existe pas");
        }
        StudentsClasse classeExist = shared_repositories.getClasse_repositorie().findByIdFiliereIdAndIdAnneeScolaireId(nivFiliere,idAnnee);
        if(classeExist != null){
            throw new NoteFundException("La promotion pour cette mention existe déjà");
        }
        StudentsClasse newClasse = new StudentsClasse();
        newClasse.setIdAnneeScolaire(anneExist);
        newClasse.setIdFiliere(nivFilieweExist);
       return shared_repositories.getClasse_repositorie().save(newClasse);


    }
    //    ----------------------------------------------------------

    public Object deleteProClasse(long idClasse){
        StudentsClasse classeExist = shared_repositories.getClasse_repositorie().findById(idClasse);
        if(classeExist == null){
            throw new NoteFundException("La promotion n'existe pas");

        }
        if(classeExist.getEffectifs() == 0){
            shared_repositories.getClasse_repositorie().delete(classeExist);
            return DTO_response_string.fromMessage("Suppression effectuer avec sucès");
        }
        throw new NoteFundException("Impossible de supprimer une promotion qui a déjà des étudiants");
    }

    //  -----------------------------------------
    public Object updateNivFiliere(NiveauFilieres nvF){
        NiveauFilieres nvFExist = shared_repositories.getNiveauFiliere_repositorie().findById(nvF.getId());
        if(nvFExist == null){
            throw new NoteFundException("La mention n'existe pas");
        }
//        nvFExist.setScolarite(nvF.getScolarite());
        shared_repositories.getNiveauFiliere_repositorie().save(nvFExist);
        return DTO_response_string.fromMessage("Mise à jours effectué avec succès");
    }
    //---------------------------------------------
    public List<StudentsClasse> getListClassForDepotDoc(long type){
        String nomNiveau1 = "LICENCE 2";
        String nomNiveau2 = "LICENCE 3";

        int currentYear = LocalDate.now().getYear();
        int earlyYear = currentYear - 2;
        LocalDate dateTroisAns = LocalDate.now().minusYears(3);

        if (type == 1){
            System.out.println("currentYear: " + currentYear + ", earlyYear: " + earlyYear + ", nomNiveau1: " + nomNiveau1);
            List<StudentsClasse> listClasse = shared_repositories.getClasse_repositorie().findClassesFromLastThreeYears(dateTroisAns, nomNiveau1);

            System.out.println("Résultats de la requête : " + listClasse);
           if (listClasse.isEmpty()){
               return new ArrayList<>();
           }
//           System.out.println("-------------licence 2-------------" + listClasse);
         listClasse.sort(Comparator.comparing(classe -> classe.getIdAnneeScolaire().getFinAnnee()));
           return listClasse;
        }
        if (type == 2){
            List<StudentsClasse> listClasse = shared_repositories.getClasse_repositorie().findClassesFromLastThreeYears(dateTroisAns,nomNiveau2);
            if (listClasse.isEmpty()){
                return new ArrayList<>();
            }
            listClasse.sort(Comparator.comparing(classe -> classe.getIdAnneeScolaire().getFinAnnee()));

            return listClasse;
        }
        throw new NoteFundException("Aucune classe trouver");
    }

}
