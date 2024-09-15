package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.AddClassDTO;
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
import java.util.stream.Collectors;

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


    public Object add(DTO_ClassModule dto) {

        StudentsClasse clm = classe_repositorie.findById(
                dto.getIdStudentClasse().getId());

        List<UE> ues = classeModule_repositorie.findAllByIdNiveauFiliereId(clm.getId())
                .stream()
                .map(ClasseModule::getIdUE)
                .toList();

        List<UE> listeUE = dto.getIdUE();
        List<UE> uesToAdd = new ArrayList<>();

        for (UE ueDto : listeUE) {
            if (!ues.contains(ueDto)) {
                uesToAdd.add(ueDto);
            }
        }

        for (UE ue : uesToAdd) {
            ClasseModule clmodule = new ClasseModule();
            clmodule.setIdNiveauFiliere(clm.getIdFiliere());
            clmodule.setIdUE(ue);
            classeModule_repositorie.save(clmodule);

        }

        return DTO_response_string.fromMessage("Ajout effectué  avec succès", 200);
    }


//    ------------------------------------------------------------------------------------------

    public Object create(AddClassDTO dto){
        Filiere filiere = filiere_repositorie.findById(dto.getIdFiliere());
        if(filiere == null){
            throw new NoteFundException("La filière n'existe pas");
        }
        Niveau niveau = niveau_repositorie.findById(dto.getIdNiveau());
        if(niveau == null){
            throw new NoteFundException("Le niveau n'existe pas");
        }
        AnneeScolaire anneeScolaire = annee_scolaire_repositorie.findById(dto.getIdAnnee());
        if(anneeScolaire == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        NiveauFilieres niveauFilieresSaved = filieresService.add(filiere, niveau, anneeScolaire);


        if(niveauFilieresSaved != null){
            StudentsClasse classe = new StudentsClasse();
            classe.setIdFiliere(niveauFilieresSaved);
            classe.setScolarite(dto.getScolarite());
            classe.setIdAnneeScolaire(anneeScolaire);
            classe_repositorie.save(classe);

        }
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
    //    ------------------------------------------------------method pour appeler tout les classes ouverte-------
    public List<StudentsClasse> readAllClass(){
        List<StudentsClasse> classeList = classe_repositorie.getClasseForCurrentYear(LocalDate.now().getYear());
        if(!classeList.isEmpty()){
            classeList.sort(Comparator.comparing(classe ->classe.getIdFiliere().getIdFiliere().getNomFiliere()));

        }
        return classeList;
    }

//    ----------------------------------cunt number of class
    public int cunt_class(){
        return classe_repositorie.countAllByFermer(false);
    }
    //-------------------------------------------methode pour appeler une classe par id---------------
    public StudentsClasse readByIdClasse(long id){
        return classe_repositorie.findById(id);
    }
// ----------------------------------------get ue list by id class in classe module---------------
    public ClasseModule getClass(long id){
        return classeModule_repositorie.findStudentsClasseWithUEsById(id);
    }

//    ------------------------------------------------------update student classe methode
    public Object update(StudentsClasse classe){
        StudentsClasse classExist = classe_repositorie.findById(classe.getId());
        if (classExist != null){
            List<Studens> list = students_repositorie.findByIdClasseIdAndActive(classExist.getId(), true);
            if(!list.isEmpty()){
                Studens studentscolariteMax = list.getFirst();

                for (Studens st: list){
                    if(st.getScolarite() > studentscolariteMax.getScolarite());
                    studentscolariteMax = st;

                }
                if (studentscolariteMax.getScolarite() > classe.getScolarite()) {
                    throw new NoteFundException("La scolarité ne doit pas être inférieure à la scolarité maximale des étudiants");
                }

            }

            classExist.setScolarite(classe.getScolarite());
          classe_repositorie.save(classExist);

            return DTO_response_string.fromMessage("Mise à effectué  avec succès", 200);
        }
        throw new NoteFundException("classe  exist pas");
    }
}
