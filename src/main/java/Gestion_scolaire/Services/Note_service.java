package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.NoteDTO;
import Gestion_scolaire.Dto_classe.NoteModuleDTO;
import Gestion_scolaire.Dto_classe.StudentsNotesDTO;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private ClasseModule_repositorie classeModule_repositorie;

//    public double calculerMoyenneGenerale(long idStudent, long idSemestre) {
//        // Récupérer toutes les notes de l'étudiant pour le semestre spécifié
//        List<Notes> notesList = notes_repositorie.findByIdInscriptionIdEtudiantIdEtudiantAndIdSemestreId(idStudent, idSemestre);
//
//        // Vérifier si la liste de notes est vide
//        if (notesList.isEmpty()) {
//            return 0.0;
//        }
//
//        // Calculer la somme des coefficients
//        double sommeCoef = notes_repositorie.findTotalCoefByIdStudent(idStudent);
//
//        // Calculer la somme des notes d'examen
//        double sommeNoteExamen = notes_repositorie.findTotalNoteByIdStudent(idStudent);
//
//        // Calculer la somme des notes de classe
//        double sommeNoteClass = notes_repositorie.findTotalClassByIdStudent(idStudent);
//
//        // Calculer la moyenne pondérée générale
//        double moyenCoef = (sommeNoteExamen * 2) + (sommeNoteClass / 3);
//
//        // Calculer la moyenne générale en divisant la moyenne pondérée par la somme des coefficients
//        double moyeneGeneral = moyenCoef / sommeCoef;
//
//        return moyeneGeneral;
//    }
//----------------------methode de cacule des moyens de tous les etudiants--------------------------
//    public List<Double> calculerMoyennesClasse(long idClasse, long idSemestre) {
//        // get all students
//        List<Inscription> etudiants = inscription_repositorie.findByIdClasseIdAndActive(idClasse, true);
//
//        // new list for moyen
//        List<Double> moyennes = new ArrayList<>();
//
//        for (Inscription etudiant : etudiants) {
//            double moyenne = calculerMoyenneGenerale(etudiant.getIdEtudiant().getIdEtudiant(), idSemestre);
//            moyennes.add(moyenne);
//        }
//        return moyennes;
//    }
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

//    ---------------------------------------------mehode pour ajouter une note----------------------
    public Object addNote(Notes notes) {
       Notes noteExist = notes_repositorie.findByIdInscriptionIdEtudiantIdEtudiantAndIdModuleIdAndIdSemestreId(
               notes.getIdInscription().getIdEtudiant().getIdEtudiant(),notes.getIdModule().getId(),notes.getIdSemestre().getId());
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
        List<ClasseModule> classeModuleList = classeModule_repositorie.findByIdNiveauFiliereId(idClasse);
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
    public Page<StudentsNotesDTO> listNotes(int page, int pageSize, long idClasse, long idSemestre) {
        List<Inscription> students = inscription_repositorie.findByIdClasseIdAndActive(idClasse, true);
        List<StudentsNotesDTO> studentsNotesDTOList = new ArrayList<>();

        // Pour chaque étudiant, calculer les notes et remplir l'objet StudentsNotesDTO
        for (Inscription student : students) {
            List<ClasseModule> classeWithModule = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(
                    idClasse, idSemestre);

            List<Notes> notesList = notes_repositorie.findByIdInscriptionIdEtudiantIdEtudiantAndIdSemestreId(student.getIdEtudiant().getIdEtudiant(), idSemestre);

            boolean hasAllModuleNotes = true;

            // Vérifier que l'étudiant a des notes pour tous les modules
            for (ClasseModule clm : classeWithModule) {
                UE ue = clm.getIdUE();
                List<Modules> modulesList = modules_repositories.findByIdUeId(ue.getId());

                for (Modules module : modulesList) {
                    Notes moduleNote = notesList.stream()
                            .filter(note -> note.getIdModule().equals(module))
                            .findFirst()
                            .orElse(null);

                    if (moduleNote == null) {
                        hasAllModuleNotes = false;  // Si un module n'a pas de note, on exclut cet étudiant
                        break;
                    }
                }

                if (!hasAllModuleNotes) break; // Stopper si un module sans note est trouvé
            }

            if (hasAllModuleNotes) {
                StudentsNotesDTO studentsNotesDTO = new StudentsNotesDTO();
                studentsNotesDTO.setNom(student.getIdEtudiant().getNom());

                studentsNotesDTO.setPrenom(student.getIdEtudiant().getPrenom());
                studentsNotesDTO.setDate_naissance(student.getIdEtudiant().getDateNaissance());
                studentsNotesDTO.setLieuNaissance(student.getIdEtudiant().getLieuNaissance());

                // Calcul des notes de l'étudiant et association avec le DTO
                List<NoteDTO> noteDTOList = moyenOfStudent(student.getIdEtudiant().getIdEtudiant(), idSemestre);
                studentsNotesDTO.setNoteDTO(noteDTOList);

                studentsNotesDTOList.add(studentsNotesDTO);
            }
        }

        if (studentsNotesDTOList.isEmpty()) {
            return Page.empty();
        }

        // Logique de pagination pour StudentsNotesDTO
        int start = Math.min(page * pageSize, studentsNotesDTOList.size());
        int end = Math.min((page + 1) * pageSize, studentsNotesDTOList.size());
        List<StudentsNotesDTO> pageContent = studentsNotesDTOList.subList(start, end);

        // Retourner la page avec StudentsNotesDTO
        return new PageImpl<>(pageContent, PageRequest.of(page, pageSize), studentsNotesDTOList.size());
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

//    -----------------------------------
public List<NoteDTO> moyenOfStudent(long idStudent, long idSemestre) {
    // Vérification de l'existence de l'étudiant
    Inscription studentExist = inscription_repositorie.findByIdEtudiantIdEtudiant(idStudent);
    if (studentExist == null) {
        throw new RuntimeException("L'étudiant n'existe pas");
    }

    // Récupération des ClasseModule associées
    List<ClasseModule> classeWithModule = classeModule_repositorie.getAllByIdNiveauFiliereIdAndIdSemestreId(
            studentExist.getIdClasse().getIdFiliere().getId(), idSemestre);

    // Récupération des notes associées à l'étudiant
    List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdInscriptionIdEtudiantIdEtudiant(idSemestre, idStudent);

    List<NoteDTO> noteDTOList = new ArrayList<>();

    // Parcours des ClasseModule (qui sont associées aux UEs)
    for (ClasseModule classeModule : classeWithModule) {
        UE ue = classeModule.getIdUE(); // On récupère l'UE associée

        NoteDTO nDTO = new NoteDTO();
        nDTO.setIdUe(ue.getId());
        nDTO.setNomUE(ue.getNomUE());

        // Récupération des modules associés à l'UE
        List<Modules> modulesList = modules_repositories.findByIdUeId(ue.getId());
        List<NoteModuleDTO> noteModuleDTOList = new ArrayList<>();

        int totalCoefficients = 0;
        double totalSum = 0;

        // Pour chaque module, on cherche la note correspondante
        for (Modules module : modulesList) {
            Notes moduleNote = notesList.stream()
                    .filter(note -> note.getIdModule().equals(module))
                    .findFirst()
                    .orElse(null);

            // Si le module a une note, on l'ajoute au calcul
            if (moduleNote != null) {
                NoteModuleDTO noteModuleDTO = new NoteModuleDTO();
                noteModuleDTO.setNomModule(module.getNomModule());
                noteModuleDTO.setIdModule(module.getId());
                noteModuleDTO.setIdUe(ue.getId());
                noteModuleDTO.setCoefficient(module.getCoefficient());

                // Calcul de la note du module pondérée par le coefficient
                double noteModule = ((moduleNote.getExamNote() * 2) + moduleNote.getClasseNote()) / 3;
                double notePonderee = noteModule * module.getCoefficient();

                noteModule = Math.round(noteModule * 100.0)/100.0;
                noteModuleDTO.setNoteModule( noteModule); // Ajout de la note au DTO

                // Ajout à la somme pondérée des notes
                totalSum += notePonderee;
                totalCoefficients += module.getCoefficient();

                // Ajout du module au DTO uniquement s'il a une note
                noteModuleDTOList.add(noteModuleDTO);
            }
        }

        // Si des modules avec des notes sont trouvés, on calcule la note de l'UE
        if (!noteModuleDTOList.isEmpty()) {
            nDTO.setModules(noteModuleDTOList);
            nDTO.setCoefficientUe(totalCoefficients);
            double noteUe = (totalSum / totalCoefficients);
            noteUe = Math.round(noteUe * 100.0) / 100.0;
            double noteUeCoef = noteUe * totalCoefficients;
            nDTO.setNoteUeCoefficient(noteUeCoef);
            nDTO.setNoteUE(noteUe);
        } else {
            // Si aucun module avec des notes n'a été trouvé, on peut décider de ne pas ajouter cet UE
            continue;
        }

        // Ajouter le DTO de l'UE à la liste des résultats
        noteDTOList.add(nDTO);
    }


    return noteDTOList; // Retourner la liste de tous les NoteDTO
}


}
