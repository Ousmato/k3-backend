package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.dtos.ModuleDTO;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.NoteDTO;
import Gestion_scolaire.Dto_classe.NoteModuleDTO;
import Gestion_scolaire.Dto_classe.UeValidateDTO;
import Gestion_scolaire.Models.Moyenne;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Repositories.Semestre_repositorie;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.Repositories.Moyenne_repositorie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Moyenne_service {

    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private Shared_services sharedservices;

    @Autowired
    private Common_service common_service;

    @Autowired
    private Note_service note_service;

    //calculate note for all students student
    @Transactional
    public void moyenOfStudent(long idClasse, long idSemestre) {
        // Récupérer les IDs des étudiants ayant des notes pour tous les modules
        List<Long> idsInscritHaveNotForAllModule = common_service.getIdsOfStudents(idSemestre, idClasse);

        // Récupérer les UEs associées à la classe et au semestre
        List<UE> ues = shared_repositories.getUe_repositorie().findByIdClasse(idClasse, idSemestre);

        if (idsInscritHaveNotForAllModule.isEmpty()) {
            throw new NoteFundException("Aucun étudiant trouvé avec des notes pour tous les modules.");
        }

        // Parcourir chaque étudiant
        for (Long inscrit : idsInscritHaveNotForAllModule) {
            Inscription studentExist = sharedservices.getInscription_service().getInscription(inscrit);
            Semestres semestreExist = sharedservices.getSemestre_service().getSemestre(idSemestre);
            List<Notes> notesList = shared_repositories.getNotes_repositorie().getByIdSemestreIdAndIdInscriptionId(idSemestre, inscrit);

            System.out.println("-------id de l'inscrit---------------" + inscrit);
            // Variables pour calculer les moyennes
            List<NoteDTO> noteDTOList = new ArrayList<>();

            // Parcourir chaque UE
            for (UE ue : ues) {
                if(ue.getNomUE().toLowerCase().contains("lib")){
                    continue;
                }
                List<Modules> modules = shared_repositories.getModules_repositories().findByIdUeIdAndActive(ue.getId(), true);

                NoteDTO nDTO = new NoteDTO();
                nDTO.setIdUe(ue.getId());
                nDTO.setNomUE(ue.getNomUE());

                int totalCoefficients = 0;
                double totalSumNoteModule = 0;
                int CoefCuntNumber = 0;

                List<NoteModuleDTO> noteModuleDTOList = new ArrayList<>();

                // Parcourir chaque module actif dans l'UE
                for (Modules module : modules) {
                    CoefCuntNumber++;
                    Notes moduleNote = notesList.stream()
                            .filter(note -> note.getIdModule().equals(module))
                            .findFirst()
                            .orElse(null);

                    // Si le module a une note, on l'ajoute au calcul
                    if (moduleNote != null) {
                        totalSumNoteModule += moduleNote.getNoteModule();
                        totalCoefficients += module.getCoefficient();

                        NoteModuleDTO noteModuleDTO = new NoteModuleDTO();
                        noteModuleDTO.setNomModule(module.getNomModule());
                        noteModuleDTO.setIdModule(module.getId());
                        noteModuleDTO.setIdUe(ue.getId());
                        noteModuleDTO.setCoefficient(module.getCoefficient());
                        noteModuleDTO.setNoteModule(moduleNote.getNoteModule());

                        noteModuleDTOList.add(noteModuleDTO);
                    }
                }

                // Si des modules avec des notes sont trouvés, on calcule la note de l'UE
                if (!noteModuleDTOList.isEmpty()) {
                    nDTO.setModules(noteModuleDTOList);
                    nDTO.setCoefficientUe(totalCoefficients);
                    double noteUe = totalSumNoteModule / CoefCuntNumber;
                    noteUe = Math.round(noteUe * 100.0) / 100.0;
                    double noteUeCoef = noteUe * totalCoefficients;
                    nDTO.setNoteUeCoefficient(noteUeCoef);
                    nDTO.setNoteUE(noteUe);

                    noteDTOList.add(nDTO);
                }
            }

            // Calculer la moyenne générale et l'enregistrer dans la base de données
            calculateMoyen(noteDTOList, studentExist, semestreExist);
        }
    }

    // Méthode pour calculer la moyenne générale
    public void calculateMoyen(List<NoteDTO> dtos, Inscription inscription, Semestres semestre) {
        // Vérification : La liste de notes des UEs ne doit pas être vide
        if (dtos.isEmpty()) {
            throw new NoteFundException("Aucune note trouvée pour ce calcul.");
        }

        // Initialisation des variables pour le calcul de la moyenne
        double totalMoyenUe = 0.0;
        int totalCoefficients = 0;

        // Parcours de chaque UE pour calculer la somme pondérée et la somme des coefficients
        for (NoteDTO dto : dtos) {
            if (dto.getNomUE().toLowerCase().contains("lib")) {
                continue;
            }
            totalMoyenUe += dto.getNoteUeCoefficient(); // Ajout de la note pondérée de l'UE
            totalCoefficients += dto.getCoefficientUe(); // Ajout du coefficient de l'UE
        }
        System.out.println("---------------total coef : " + totalCoefficients);
        // Vérification : Les coefficients doivent être valides (non nuls)
        if (totalCoefficients == 0) {
            throw new RuntimeException("Les coefficients des UEs sont invalides. Impossible de calculer la moyenne générale.");
        }

        // Calcul de la moyenne générale
        double moyenneGenerale = totalMoyenUe / totalCoefficients;
        moyenneGenerale = Math.round(moyenneGenerale * 100.0) / 100.0; // Arrondi à 2 décimales

        // Recherche d'une moyenne existante dans la base de données
        Moyenne myExist = shared_repositories.getMoyenne_repositorie().findByIdSemestreIdAndIdInscriptionId(semestre.getId(), inscription.getId());

        if (myExist == null) {
            // Si aucune moyenne existante, créer un nouvel enregistrement
            Moyenne moyenne = new Moyenne();
            moyenne.setMoyenGenerale(moyenneGenerale);
            moyenne.setIdSemestre(semestre);
            moyenne.setIdInscription(inscription);

            // Sauvegarde dans la base de données
//            System.out.println("_____________la moyene : " + moyenne);
            shared_repositories.getMoyenne_repositorie().save(moyenne);
        } else {
            // Si une moyenne existe déjà, mettre à jour l'enregistrement
            myExist.setMoyenGenerale(moyenneGenerale);
            shared_repositories.getMoyenne_repositorie().save(myExist);
        }
    }

    public List<Moyenne> getMoyenneByIdClassAndSemestre(long idClass, long semestre) {
        List<Long> idStudentsHaveNoteForAllModules = common_service.getIdsOfStudents(semestre, idClass);
        System.out.println("--------------------liste ids : " + idStudentsHaveNoteForAllModules);

        List<Moyenne> moyenneList = new ArrayList<>();
        if (idStudentsHaveNoteForAllModules.isEmpty()) {
            return new ArrayList<>();
        }
        for (Long idStudent : idStudentsHaveNoteForAllModules) {

            Moyenne moy = shared_repositories.getMoyenne_repositorie().findByIdSemestreIdAndIdInscriptionId(semestre,idStudent);

            if (moy == null) {
                continue;
            }
//            System.out.println("--------------------id inscrit : " + idStudent);
//            System.out.println("--------------------id inscrit : " + moy.getMoyenGenerale());

            moyenneList.add(moy);
        }
        return moyenneList;
    }

    // Récupérer les modules en session
    public List<Long> getModuleInSession(long idClasse, long semestre) {
        // Récupération des IDs des étudiants ayant des notes pour ce semestre et classe
        List<Long> idStudentsHaveNoteForAllModules = common_service.getIdsOfStudents(semestre, idClasse);

        // Utilisation d'un Set pour éviter les doublons d'IDs
        Set<Long> allIdsModulesInSession = new HashSet<>();

        for (Long idInscrit : idStudentsHaveNoteForAllModules) {
            // Récupération des notes de l'étudiant pour ce semestre
            List<Notes> notesList = shared_repositories.getNotes_repositorie()
                    .sessionNotesByIdinscription(idInscrit, semestre);

            if (!notesList.isEmpty()) {
                // Vérification de l'existence de la moyenne pour l'étudiant
                Moyenne moyenne = shared_repositories.getMoyenne_repositorie()
                        .findAllAjourneesByStudent(semestre, idInscrit);

                if (moyenne != null) {
                    // Ajout des IDs des modules des notes
                    notesList.forEach(note -> allIdsModulesInSession.add(note.getIdModule().getId()));
                }
            }
        }

        // Retourner une liste (conversion du Set pour éviter les doublons)
        return new ArrayList<>(allIdsModulesInSession);
    }

    public List<InscriptionNoteDTO> getListSemestreMoyennes(long idClasse){

        List<InscriptionNoteDTO> inscriptionNoteDTOList = new ArrayList<>();
        List<Inscription> inscriptions = common_service.getStudentsHaveMoyenByClassId(idClasse);
        for (Inscription inscription : inscriptions) {
            InscriptionNoteDTO inscriptionNoteDTO = new InscriptionNoteDTO();
            List<UeValidateDTO> ueValidateDTOS = note_service.getUeValideByStudentBySemestre(inscription.getId(), idClasse);
            inscriptionNoteDTO.setPrenom(inscription.getIdEtudiant().getPrenom());
            inscriptionNoteDTO.setNom(inscription.getIdEtudiant().getNom());
            inscriptionNoteDTO.setLieuNaissance(inscription.getIdEtudiant().getLieuNaissance());

            String sexe = inscription.getIdEtudiant().getSexe();
            inscriptionNoteDTO.setSexe(Objects.equals(sexe, "FEMME") ? "F" : "M");
            inscriptionNoteDTO.setDateNaissance(inscription.getIdEtudiant().getDateNaissance());
            inscriptionNoteDTO.setUeValidate(ueValidateDTOS);
            inscriptionNoteDTO.setIdClasse(inscription.getIdClasse());
            inscriptionNoteDTO.setId(inscription.getIdEtudiant().getId());
            inscriptionNoteDTOList.add(inscriptionNoteDTO);

        }
        return inscriptionNoteDTOList.stream().distinct().toList();
    }


}
