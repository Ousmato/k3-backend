package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Note_service {
    @Autowired
    private Notes_repositorie notes_repositorie;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

    public double calculerMoyenneGenerale(long idStudent, long idSemestre) {
        // Récupérer toutes les notes de l'étudiant pour le semestre spécifié
        List<Notes> notesList = notes_repositorie.findByIdStudentsIdEtudiantAndIdSemestreId(idStudent, idSemestre);

        // Vérifier si la liste de notes est vide
        if (notesList.isEmpty()) {
            return 0.0;
        }

        // Calculer la somme des coefficients
        double sommeCoef = notes_repositorie.findTotalCoefByIdStudent(idStudent);

        // Calculer la somme des notes d'examen
        double sommeNoteExamen = notes_repositorie.findTotalNoteByIdStudent(idStudent);

        // Calculer la somme des notes de classe
        double sommeNoteClass = notes_repositorie.findTotalClassByIdStudent(idStudent);

        // Calculer la moyenne pondérée générale
        double moyenCoef = (sommeNoteExamen * 2) + (sommeNoteClass / 3);

        // Calculer la moyenne générale en divisant la moyenne pondérée par la somme des coefficients
        double moyeneGeneral = moyenCoef / sommeCoef;

        return moyeneGeneral;
    }
//----------------------methode de cacule des moyens de tous les etudiants--------------------------
    public List<Double> calculerMoyennesClasse(long idClasse, long idSemestre) {
        // get all students
        List<Studens> etudiants = students_repositorie.findByIdClasseIdAndActive(idClasse, true);

        // new list for moyen
        List<Double> moyennes = new ArrayList<>();

        for (Studens etudiant : etudiants) {
            double moyenne = calculerMoyenneGenerale(etudiant.getIdEtudiant(), idSemestre);
            moyennes.add(moyenne);
        }
        return moyennes;
    }
//    ------------------------------methode pour modifier la note d'un etudiant---------------------
    public Object update(Notes notes){
        Notes noteExist = notes_repositorie.findById(notes.getId());
        if (noteExist == null){
            throw new RuntimeException("la note n'existe pas");
        }
        noteExist.setExamNote(notes.getExamNote());
        noteExist.setClasseNote(notes.getClasseNote());
        notes_repositorie.save(noteExist);
        return DTO_response_string.fromMessage("Mise à jour effectué avec succè", 200);
    }
//-------------------------------------------------------methode pour appeler les note d'un etudiant du semestre en cours---------
    public List<Notes> readByIdCurrentSemestre(long idStudent, long idSemestre){
        List<Notes> list = notes_repositorie.findByIdStudentsIdEtudiantAndIdSemestreId(idStudent, idSemestre);
        if (list.isEmpty()){
            return new ArrayList<>();
        }
        return list;
    }
//    ---------------------------------------------mehode pour ajouter une note----------------------
    public Object addNote(Notes notes) {
       Notes noteExist = notes_repositorie.findByIdStudentsIdEtudiantAndIdModuleIdAndIdSemestreId(
               notes.getIdStudents().getIdEtudiant(),notes.getIdModule().getId(),notes.getIdSemestre().getId());
        if(noteExist != null){
            throw new RuntimeException("cet etudian a deja de note pour ce module dans cette semestre");
        }

        notes_repositorie.save(notes);
        return DTO_response_string.fromMessage("Ajout effectué avec succè", 200);
    }
//--------------------------------------methode pour appeler tout les module de la
//    classe de l'etudiant qui on deja etait programmer pour un emplois du temps
    public ArrayList<Modules> readAllByAllEmplois(long idClasse){
//        Emplois emploisList = emplois_repositorie.getEmploisByDateFinAfterAndIdClasseId(LocalDate.now(), idClasse);
        List<ClasseModule> classeModuleList = classeModule_repositorie.findByIdStudentClasseId(idClasse);
        Set<Modules> modulesSet = new HashSet<>();

            for (ClasseModule clm : classeModuleList){
                UE ue = clm.getIdUE();
            if (ue != null) {
                List<Modules> toutModule = modules_repositories.findByIdUeId(ue.getId());
                modulesSet.addAll(toutModule);

            }

        }
        return new ArrayList<>(modulesSet);
    }
//    ----------------------------------method get all notes by idClasse and current semestre
    public Page<Notes> listNotes(int page, int pageSize, long idClasse){
        Semestres currentSemestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Notes> list = notes_repositorie.getByIdSemestreIdAndIdStudentsIdClasseId(currentSemestre.getId(), idClasse, pageable);
        if(list.isEmpty()){
            return Page.empty(pageable);
        }
        return list;
    }
//    ----------------------------------------methode pour appler les notes par id du module
    public List<Notes> getNotesByIdModule(long idModule){
        Semestres currentSemestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdModuleId(currentSemestre.getId(), idModule);
        if(!notesList.isEmpty()){
            return notesList;
        }
        return new ArrayList<>();
    }

}
