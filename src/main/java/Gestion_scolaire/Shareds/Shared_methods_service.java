package Gestion_scolaire.Shareds;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Models.Notes;
import Gestion_scolaire.Niveaux_Filieres.dtos.SousFiliereDTO;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.Student_DTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.InscriptionSousFilieres;
import Gestion_scolaire.students.entity.StudentSession;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.students.enumClass.TypeStatusDeserializer;
import Gestion_scolaire.students.enumClass.Type_status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class Shared_methods_service {

    private final Shared_repositories shared_repositories;

    public String abrevigateName(String nom) {
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
        UE ue = shared_repositories.getUe_repositorie().findById(idUE);
        if (ue == null) {
            throw new NoteFundException("Ue est introuvable");
        }
        List<Modules> modulesList = shared_repositories.getModules_repositories().findByIdUeIdAndActive(idUE, true);
        List<Notes> notesList = shared_repositories.getNotes_repositorie().getByIdSemestreIdAndIdInscriptionId(idSemestre, idInscription);

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

                StudentSession session = shared_repositories.getSessions_repositorie().findByIdInscritIdAndIdSemestreIdAndIdModuleId(idInscription, idSemestre, module.getId());
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

    public String getNextLevelClass(StudentsClasse classeExist) {
        Niveau oldNiv = classeExist.getIdFiliere().getIdNiveau();

// Trouver la classe correspondant à l'ancien niveau et retourner le niveau supérieur

        return switch (oldNiv.getNom()) {
            case "LICENCE 1" -> "LICENCE 2";
            case "LICENCE 2" -> "LICENCE 3";
            case "LICENCE 3" -> "MASTER 1";
            case "MASTER 1" -> "MASTER 2";
            case "MASTER 2" -> "Doctorat";
            default -> "Inconnu"; // Si l'ancien niveau ne correspond à aucun cas
        };
    }

    public StudentsClasse getPreviousClasseById(long idClasse){
        StudentsClasse classeExist = shared_repositories.getClasse_repositorie().findById(idClasse);
        if(classeExist == null){
            throw new NoteFundException("La promotion n'existe pas");
        }
        AnneeScolaire oldAnnee = classeExist.getIdAnneeScolaire();
        int nextYear = oldAnnee.getDebutAnnee().getYear() + 1;

        String niveauName = getNextLevelClass(classeExist);
        String filiereName = classeExist.getIdFiliere().getIdFiliere().getNomFiliere();

        if (!niveauName.equalsIgnoreCase("M2")){
          return shared_repositories.getClasse_repositorie().getNextClass(nextYear, niveauName, filiereName);
        }
        throw new NoteFundException("Le niveau supérieure est introuvable");


    }

    public String getNumInscription (long idStudent){
        List<Inscription> inscription = shared_repositories.getInscription_repositorie().findByIdEtudiantIdEtudiant(idStudent);
        return inscription.getFirst().getNumeroInscrit();
    }
    public SousFiliereDTO getSousFilieresAndOurInscrits(long idSouFiliere){
        SousFiliereDTO sousFiliereDTO = new SousFiliereDTO();
        List<GetInscriptionDto> dtoList = new ArrayList<>();
        List<InscriptionSousFilieres> inscriptionSousFilieres = shared_repositories.getInscriptionSousFiliere_repositorie().findByIdSousFiliereId(idSouFiliere);
        for (InscriptionSousFilieres filieres : inscriptionSousFilieres) {
            GetInscriptionDto dto = GetInscriptionDto.toDto(filieres.getIdInscription());
            dto.setIdEtudiant(Student_DTO.toDTO(filieres.getIdInscription().getIdEtudiant()));
            dtoList.add(dto);

        }
        sousFiliereDTO.setName(inscriptionSousFilieres.getFirst().getIdSousFiliere().getNomSousFiliere());
        sousFiliereDTO.setId(idSouFiliere);
        sousFiliereDTO.setInscriptions(dtoList);
        return sousFiliereDTO;
    }

    public void updateIfNotEmpty(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
    }

    //validate date naissance
    public Inscription  validateInscrit(Inscription inscrit){
        String dateString = inscrit.getIdEtudiant().getDateNaissance();

        if(dateString.toLowerCase().contains("vers")){
            // Remplacer les espaces multiples par un seul espace
            String cleanedDateString = dateString.replaceAll("\\s+", " ").trim();

            // Split sur le premier espace pour récupérer l'année
            String yearString = cleanedDateString.split(" ")[1].trim();

            try {
                int year = Integer.parseInt(yearString);
                LocalDate date = LocalDate.of(year, 1, 1);

                LocalDate diff = LocalDate.now().minusYears(15);
                if (diff.isBefore(date)) {
                    throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
                }

            } catch (NumberFormatException e) {
                throw new NoteFundException("L'année fournie n'est pas valide.");
            }
        }else {
           processDate(inscrit.getIdEtudiant().getDateNaissance());
        }

        return inscrit;
    }

    // validate scolarite by type of status
    public boolean validateScolariteByStatus(Double payer, Type_status type, Double seuil) {

        return switch (type) {
            case PROFESSIONNEL_PRIVEE, REGULIER, CANDIDAT_LIBRE, PROFESSIONNEL_ETAT, PROFESSIONNEL_COLLECTIVITE , FORMATION_CONTINUE-> payer <= seuil;

            default -> throw new IllegalArgumentException("Statut non pris en charge : " + type);
        };
    }
    public Double getSeuilScolarite(Type_status status){
      return   switch (status){
            case PROFESSIONNEL_COLLECTIVITE, PROFESSIONNEL_ETAT -> 150000.0;
            case PROFESSIONNEL_PRIVEE -> 200000.0;
            case REGULIER -> 6000.0;
            case CANDIDAT_LIBRE -> 75000.0;
            case FORMATION_CONTINUE -> 300000.0;
            default -> throw new IllegalArgumentException("Statut non pris en charge : " + status);
        };
    }

    //converti string en enum type status
    public Type_status convertStringToTypeStatus(String status) {
        // Créer un ObjectMapper et enregistrer le deserializer personnalisé
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Type_status.class, new TypeStatusDeserializer());
        mapper.registerModule(module);

        try {
            // Convertir la chaîne en Type_status
            return mapper.readValue("\"" + status + "\"", Type_status.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la conversion du statut : " + status, e);
        }
    }

}
