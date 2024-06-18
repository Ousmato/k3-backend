package Gestion_scolaire.Services;

import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Notes_repositorie;
import Gestion_scolaire.Repositories.Students_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//    ------------------------------methode pour modifier le note d'un etudiant---------------------
    public Notes update(Notes notes){
        Notes noteExist = notes_repositorie.findById(notes.getId());
        if (noteExist == null){
            throw new RuntimeException("la note n'existe pas");
        }
        noteExist.setExamNote(notes.getExamNote());
        noteExist.setClasseNote(notes.getClasseNote());
        return notes_repositorie.save(noteExist);
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
    public List<Notes> addNote(List<Notes> notes) {
        List<Notes> listNoteSaved = new ArrayList<>();

        for (Notes note : notes) {
            // Vérifiez l'existence de l'étudiant pour chaque note
            Studens studentExist = students_repositorie.findByIdEtudiant(note.getIdStudents().getIdEtudiant());

            if (studentExist == null) {
                throw new RuntimeException("Aucun étudiant n'est associé à cette note : " + note);
            }

            listNoteSaved.add(note);
        }

        // Sauvegarder toutes les notes après avoir vérifié l'existence de l'étudiant pour chaque note
        return notes_repositorie.saveAll(listNoteSaved);
    }
//--------------------------------------methode pour appeler tout les module de la
//    classe de l'etudiant qui on deja etait programmer pour un emplois du temps
    public ArrayList<Modules> readAllByAllEmplois(long idClasse){
        List<Emplois> emploisList = emplois_repositorie.findByIdClasseModuleIdStudentClasseId(idClasse);
        Set<Modules> modulesSet = new HashSet<>();

        for(Emplois emplois : emploisList){
            UE ue = emplois.getIdClasseModule().getIdUE();

            if (ue != null) {
                List<Modules> toutModule = modules_repositories.findByIdUeId(ue.getId());
                modulesSet.addAll(toutModule);

            }

        }
        return new ArrayList<>(modulesSet);
    }
}
