package Gestion_scolaire.SharedService;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Repositories.AnneeScolaire_repositorie;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Notes_repositorie;
import Gestion_scolaire.Repositories.Ue_repositorie;
import Gestion_scolaire.Services.Modules_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.StudentSession;
import Gestion_scolaire.students.repositories.Sessions_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class Shared_service {

    @Autowired
    private Sessions_repositorie sessions_repositorie;

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private Notes_repositorie notes_repositorie;

    public String abrevigateRoleName(String nom) {
        // Si le nom est vide ou null, on retourne une chaîne vide
        if (nom == null || nom.isEmpty()) {
            return "";
        }

        // Split le nom par les espaces
        String[] words = nom.split(" ");
        StringBuilder abbreviation = new StringBuilder();

        // Parcours des mots
        for (String word : words) {
            // Si le mot a plus de 3 caractères, on prend la première lettre
            if (word.length() > 3) {
                abbreviation.append(word.charAt(0)); // Ajouter la première lettre du mot
            }
        }

        // Retourner l'abréviation en majuscules
        if (abbreviation.toString().equals("EEER")){
            return "3ER";
        }
        return abbreviation.toString().toUpperCase();
    }

    public int extraireNumeroSemestre(String nomSemestre) {
        if (nomSemestre == null || nomSemestre.isEmpty()) {
            throw new IllegalArgumentException("Le nom du semestre ne peut pas être vide ou null.");
        }

        // Extraire le dernier caractère
        char dernierCaractere = nomSemestre.charAt(nomSemestre.length() - 1);

        // Vérifier si c'est un chiffre et convertir en nombre
        if (Character.isDigit(dernierCaractere)) {
            return Character.getNumericValue(dernierCaractere);
        } else {
            throw new IllegalArgumentException("Le dernier caractère du nom du semestre n'est pas un chiffre.");
        }
    }

    private LocalDate parseDate(String dateString) throws NoteFundException {
        // Liste des formats de date possibles
        String[] possibleFormats = {"dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy", "dd-MM-yyyy"};

        for (String format : possibleFormats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Continuer avec le prochain format
            }
        }
        throw new NoteFundException("Format de date invalide : " + dateString);
    }

    public void processDate(String dateString) throws NoteFundException {
        LocalDate date = parseDate(dateString);

        // Vérifier si l'étudiant a au moins 15 ans
        LocalDate diff = LocalDate.now().minusYears(15);
        if (diff.isBefore(date)) {
            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
        }
        System.out.println("Date valide : " + date);
    }

    public String abrevigateSerie(String nom) {
        // Si le nom est vide ou null, on retourne une chaîne vide
        if (nom == null || nom.isEmpty()) {
            return "";
        }

        // Split le nom par les espaces
        String[] words = nom.split("_");
        StringBuilder abbreviation = new StringBuilder();

        // Parcours des mots
        for (String word : words) {
            // Si le mot a plus de 3 caractères, on prend la première lettre
            if (word.length() > 3) {
                abbreviation.append(word.charAt(0)); // Ajouter la première lettre du mot
            }
        }

        // Retourner l'abréviation en majuscules
        if(abbreviation.length() > 1){
            return abbreviation.toString().toUpperCase();
        }
        return nom.toUpperCase();
    }

    //calcul note ue to validate ue
    public double noteUe(long idUE, long idSemestre, long idInscription) {
        UE ue = ue_repositorie.findById(idUE);
        if (ue == null) {
            throw new NoteFundException("Ue est introuvable");
        }
        List<Modules> modulesList = modules_repositories.findByIdUeId(idUE);
        List<Notes> notesList = notes_repositorie.getByIdSemestreIdAndIdInscriptionId(idSemestre, idInscription);

        double totalSumNoteModule = 0;
        int moduleCuntNumber = 0;
        double sessionNote = 0.0;

        // Pour chaque module, on cherche la note correspondante
        for (Modules module : modulesList) {

            Notes moduleNote = notesList.stream()
                    .filter(note -> note.getIdModule().equals(module))
                    .findFirst()
                    .orElse(null);

            // Si le module a une note, on l'ajoute au calcul
            if (moduleNote != null) {

                StudentSession session = sessions_repositorie.findByIdInscritIdAndIdSemestreIdAndIdModuleId(idInscription, idSemestre, module.getId());
                if (session !=null){
                    sessionNote = session.getNoteSession();
                }
                    totalSumNoteModule += moduleNote.getNoteModule();

                 moduleCuntNumber++;

            }
        }
        double noteUePondere = (totalSumNoteModule / moduleCuntNumber);

        if (noteUePondere > sessionNote){
            return Math.round(noteUePondere * 100.0) / 100.0;
        }else {
            return Math.round(sessionNote * 100.0) / 100.0;
        }
    }

    public String abregNiveauName(String name){
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (name.equalsIgnoreCase("LICENCE 1")){
            return "L1";
        }else if (name.equalsIgnoreCase("LICENCE 2")){
            return "L2";
        }else if (name.equalsIgnoreCase("LICENCE 3")){
            return "L3";
        }
        return name;

    }

}
