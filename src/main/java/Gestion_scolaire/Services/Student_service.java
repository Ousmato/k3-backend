package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.EnumClasse.Type_status;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class Student_service {

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private AnneeScolaire_repositorie annee_repositorie;

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Admin_repositorie admin_repositorie;

    @Autowired
    MessaSender messaSender;

    @Autowired
    private Validator validator;

    @Autowired
    private Classe_service classe_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private fileManagers fileManagers;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Transactional
    public Object add(InscriptionDTO inscrit, MultipartFile file) throws IOException {
        Set<ConstraintViolation<Studens>> violations = validator.validate(inscrit.getIdEtudiant());
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
//        System.out.printf("%d-------------------------students---------%n", inscrit.getIdEtudiant());
        String telephone = String.valueOf(inscrit.getIdEtudiant().getTelephone());
        if (telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }

        LocalDate dateNaissance = LocalDate.now().minusYears(15);
        if (dateNaissance.isBefore(inscrit.getIdEtudiant().getDateNaissance())) {
            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
        }
        if (inscrit.getIdEtudiant().getMatricule().length() != 12) {
            throw new NoteFundException("Le matricule n'est pas valide");
        }
        Studens studentExist = students_repositorie.findByMatriculeAndTelephone(inscrit.getIdEtudiant().getMatricule(), inscrit.getIdEtudiant().getTelephone());
        if (studentExist == null) {

            String urlPhoto = fileManagers.saveFile(file);
            inscrit.getIdEtudiant().setUrlPhoto(urlPhoto);

            String plainPassword = inscrit.getIdEtudiant().getPassword();
            inscrit.getIdEtudiant().setPassword(passwordEncoder.encode(plainPassword));


            if (inscrit.getIdClasse() != null) {

                Inscription newIncription = new Inscription();
                newIncription.setDate(LocalDate.now());
                if (newIncription.getDate().isBefore(inscrit.getIdClasse().getIdAnneeScolaire().getDebutAnnee()) || newIncription.getDate().isAfter(inscrit.getIdClasse().getIdAnneeScolaire().getFinAnnee())) {
                    throw new NoteFundException("Inscription non valide veillez choisir une année scolaire en cours");
                }

                inscrit.getIdClasse().setEffectifs(inscrit.getIdClasse().getEffectifs() + 1);
                classe_repositorie.save(inscrit.getIdClasse());

               Studens newStudent = students_repositorie.save(inscrit.getIdEtudiant());
                newIncription.setIdClasse(inscrit.getIdClasse());
                newIncription.setIdEtudiant(newStudent);
                newIncription.setIdAdmin(inscrit.getIdAdmin());
                inscription_repositorie.save(newIncription);

//                PendingEmail emailPend = new PendingEmail();
//                emailPend.setToSend(inscrit.getEmail());
//                emailPend.setFromAdmin(adminEmail);
//                emailPend.setBody("Bonjour M. %s %s%s,".formatted(inscrit.getNom(), inscrit.getPrenom(), messaSender.message(inscrit, plainPassword)));
//                emailPend.setSubject("Confirmation");
//
//                messaSender.sendSimpleMail(emailPend);
            }
            return DTO_response_string.fromMessage("Inscription effectuée avec succé ", 200);
        } else {
            throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");
        }

    }

    //    -----------------------------------------------------------------------------------
    @Transactional
    public Object update(Studens studens, MultipartFile file) throws IOException {
        String telephone = String.valueOf(studens.getTelephone());
        if (telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }
        LocalDate dateNaissance = LocalDate.now().minusYears(15);
        if (dateNaissance.isBefore(studens.getDateNaissance())) {
            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
        }
        if (studens.getMatricule().length() != 12) {
            throw new NoteFundException("Le matricule n'est pas valide");
        }

        Studens studentExist = students_repositorie.findByIdEtudiant(studens.getIdEtudiant());
        if (studentExist == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }

        System.out.println("--------------------je suis ici");
        // Vérification du mot de passe
        if (studens.getPassword() != null && !studens.getPassword().isEmpty()) {
            studentExist.setPassword(passwordEncoder.encode(studens.getPassword()));
        }else {
            studentExist.setPassword(studentExist.getPassword());
        }
        // Mise à jour de la photo si le fichier est fourni
        if (file != null && !file.isEmpty()) {
            String urlPhoto = fileManagers.updateFile(file, studentExist.getUrlPhoto());
            studentExist.setUrlPhoto(urlPhoto);
        }

//        studentExist.setDate(LocalDate.now());
        studentExist.setMatricule(studens.getMatricule());
//        studentExist.setScolarite(studens.getScolarite());
        studentExist.setSexe(studens.getSexe());
        studentExist.setEmail(studens.getEmail());
        studentExist.setDateNaissance(studens.getDateNaissance());
        studentExist.setLieuNaissance(studens.getLieuNaissance());
        studentExist.setNom(studens.getNom());
        studentExist.setPrenom(studens.getPrenom());

        students_repositorie.save(studentExist);
        return DTO_response_string.fromMessage("Modification effectuée avec succé", 200);

    }

    //    -----------------------------methode pour desactiver un etudiant-------------------------------------------
    public Object desable(long id) {
        Studens studensExist = students_repositorie.findByIdEtudiant(id);
        if (studensExist != null) {
            studensExist.setActive(!studensExist.isActive());
            students_repositorie.save(studensExist);
            return DTO_response_string.fromMessage("Changement d'etat effectué avec succé", 200);
        }
        throw new NoteFundException("Student does not exist");
    }

    //    -----------------------------------------------------methode pour appeller tous les etudiants active----------------
    public Page<Inscription> readAll(int page, int pageSize) {
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, pageSize,sort);
        Page<Inscription> studensPages = inscription_repositorie.findStudentOfCurrentYear(pageable);
        if (studensPages.isEmpty()) {
            return Page.empty(pageable);
        }

//        List<Studens> students = studensPages.stream().map(Inscription::getIdEtudiant).toList();
//        PageImpl<>(students, pageable, studensPages.getTotalElements());

        return studensPages;
    }

    //    ----------------------------find all student
    public List<Studens> find_all() {
        return students_repositorie.findAll();
    }

    //    --------------------------------------------------methode appeler un etudiant par id----------------------
    public Studens studenById(long id) {
        Studens studensExist = students_repositorie.findByIdEtudiant(id);
        if (studensExist != null) {
            return studensExist;
        } else {
            throw new NoteFundException("l'étudiant est introuvable");
        }
    }

    //    ---------------------------method les etudiant de la classe par page-----------------------------------
    public Page<Inscription> readAllByClassId(int page, int pageSize, long idClass) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Inscription> pages = inscription_repositorie.findByIdClasseId(idClass, pageable);
        if (pages.isEmpty()) {
            return Page.empty(pageable);
        }

//        List<Studens> students = pages.stream().map(Inscription::getIdEtudiant).toList();

        return pages;
    }

    //-----------------------------update scolarite
    public Object update_scolarite(long idIncription, long idAdmin, double scolarite) {
        Inscription studentInscrit = inscription_repositorie.findById(idIncription);

        if (studentInscrit != null) {
            if(scolarite > studentInscrit.getIdClasse().getIdFiliere().getScolarite()){
                throw new NoteFundException("Montant trop élevé");
            }

            if(studentInscrit.getIdEtudiant().getStatus() == Type_status.régulier && scolarite > 6000){
                throw new NoteFundException("Le frais de scolarité est invalide");
            }
            double montant = studentInscrit.getScolarite() + scolarite;
            studentInscrit.setScolarite(montant);
            studentInscrit.setPayer(true);
            studentInscrit.setAdminPaye(idAdmin);
            inscription_repositorie.save(studentInscrit);
            return DTO_response_string.fromMessage("Modification effectuer avec succé", 200);
        }
        throw new NoteFundException("Student does not exist");
    }

    //    ----------------------------get list student by class id
    public List<Studens> get_by_classId(long idClass) {
        List<Inscription> list = inscription_repositorie.getByIdClasseIdAndPayer(idClass, true);
        System.out.println("-----------------" + list + "--------------------------");
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(Inscription::getIdEtudiant).toList();
    }

    //    -----------------------------get by id annee scolaire
    public Page<Inscription> get_by_idAnneeScolaire(int page, int pageSize, long idAnneeScolaire) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Inscription> list = inscription_repositorie.getByIdClasseIdAnneeScolaireId(idAnneeScolaire, pageable);
        if (list.isEmpty()) {
            return Page.empty(pageable);
        }
//        List<Studens> students = list.stream().map(Inscription::getIdEtudiant).toList();

        return list;
    }

    //    --------------------------------get scolarite annuelle of all student
    public double get_scolarite_annuel() {
        return inscription_repositorie.sumScolariteForCurrentYear();
    }

    public Inscription getInscriptionById(long idInscrit){
        Inscription inscrit = inscription_repositorie.findById(idInscrit);
        if (inscrit == null) {
            throw new NoteFundException("L'inscription pour cet étudiant est introuvable");
        }
        return inscrit;
    }
    //    ----------------------get all reliquat of current year
    public double getAll_reliquat() {
        return inscription_repositorie.getReliquatForCurrentYear();
    }

    //    ------------------cunt all student inscrit
    public CuntStudentDTO cunt_student_inscrit() {
        int inscrit = inscription_repositorie.countAllByPayer(true);
        int non_inscrit = inscription_repositorie.countAllByPayer(false);
        return CuntStudentDTO.getCount(inscrit, non_inscrit);
    }

    //-------------------------------------------------------------------------------------
    @Transactional
    public Object reinscription(Inscription inscrit, long idClasse, long idAdmin) {

        StudentsClasse newClass = classe_repositorie.findById(idClasse);

        // Trouver la classe
        if (newClass == null) {
            throw new NoteFundException("La classe est introuvable");
        }
        List<Inscription> inscriptionListExist = getListByIdAnneeAndIdClase(newClass.getIdAnneeScolaire().getId(), idClasse);
        if (!inscriptionListExist.isEmpty()) {
//            System.out.println("il est rentre dans dif de null");
//            boolean hasEquals = false;
            for (Inscription inscriptionExist : inscriptionListExist) {
//                System.out.println("------------le nouveaux-------"+inscrit.getIdEtudiant());
//                System.out.println("-------------les inscrits---------"+ inscriptionExist.getIdEtudiant());
                if (inscriptionExist.getIdEtudiant().equals(inscrit.getIdEtudiant())){
                    desabledInscription(inscriptionExist.getId(),idClasse);

                    return DTO_response_string.fromMessage("L'inscription existante a été désactivée pour cet étudiant.", 200);

                }
//                break;
            }

        }

//        System.out.println("-----------------liste-----des ----danscallse sup------"+inscriptionListExist + "\n");
        // Trouver l'étudiant
        Inscription studentExist = inscription_repositorie.findByIdEtudiantIdEtudiant(inscrit.getIdEtudiant().getIdEtudiant());
        if (studentExist == null) {
            throw new NoteFundException("L'étudiant est introuvable");
        }



        Admin adminExist = admin_repositorie.findByIdAdministra(idAdmin);
        if (adminExist == null) {
            throw new NoteFundException("L'administrateur est introuvable");

        }
//        System.out.println("-------------------------inscrit------------" + inscrit);

        // Trouver l'année scolaire
        AnneeScolaire newYear = annee_repositorie.findById(newClass.getIdAnneeScolaire().getId());
        if (newYear == null) {
            throw new NoteFundException("L'année scolaire est introuvable");
        }

        // Vérifier si l'étudiant est déjà inscrit dans la nouvelle classe et l'année scolaire
        if (studentExist.getIdClasse() != null && studentExist.getIdClasse().equals(newClass) && studentExist.getIdClasse().getIdAnneeScolaire().equals(newYear)) {
            throw new NoteFundException("L'étudiant est déjà inscrit dans cette classe pour cette année scolaire");
        }


        // Vérifier que le niveau de la nouvelle classe est supérieur à l'ancien niveau
        if (studentExist.getIdClasse() != null && studentExist.getIdClasse().getIdFiliere().getIdNiveau().equals(newClass.getIdFiliere().getIdNiveau())) {
            throw new NoteFundException("Réinscription invalide, veuillez choisir un niveau supérieur");
        }

        // Vérifier la filière
        if (studentExist.getIdClasse() != null && !studentExist.getIdClasse().getIdFiliere().getIdFiliere().equals(newClass.getIdFiliere().getIdFiliere())) {
            throw new NoteFundException("Réinscription invalide, la filière ne correspond pas");
        }

        // Création d'un nouvel étudiant pour la réinscription


        newClass.setEffectifs(newClass.getEffectifs() + 1);
       StudentsClasse classSaved = classe_repositorie.save(newClass);
        Inscription newInscription = new Inscription();
        newInscription.setIdEtudiant(studentExist.getIdEtudiant());
//        newInscription.setDate(LocalDate.now());
        newInscription.setDate(classSaved.getIdAnneeScolaire().getDebutAnnee());
        newInscription.setIdClasse(classSaved);

        newInscription.setIdAdmin(adminExist);
//        inscription_repositorie.save(newInscription);
        return DTO_response_string.fromMessage("Inscription effectuée avec succès", 200);
    }


    //    -------------------------------------------get all student by id annee and idClasse
    public Page<Studens> getStudentByIDAnneeAndIdClasse(long idAnnee, long idClasse, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Inscription> studensPage = inscription_repositorie.findByIdClasseIdAnneeScolaireIdAndIdClasseId(idAnnee, idClasse, pageable);
        if (studensPage.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Studens> students = studensPage.stream().map(Inscription::getIdEtudiant).toList();
        return new PageImpl<>(students, pageable, studensPage.getTotalElements());
    }

    //    -------------------------------------------------
    public List<Inscription> getListByIdAnneeAndIdClase(long idAnnee, long idClasse) {
        List<Inscription> list = inscription_repositorie.findByIdClasseIdAnneeScolaireIdAndIdClasseId(idAnnee, idClasse);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
//        List<Studens> studensList = new ArrayList<>(list.stream().map(Inscription::getIdEtudiant).toList());
        list.sort(Comparator.comparing(inscription -> inscription.getIdEtudiant().getNom()));
        return list;

    }

    @Transactional
    public Object addStudentsImport(List<Inscription> inscriptionList){
        for (Inscription inscrit : inscriptionList) {
            Set<ConstraintViolation<Studens>> violations = validator.validate(inscrit.getIdEtudiant());
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            Studens studens = inscrit.getIdEtudiant();
            StudentsClasse cl = inscrit.getIdClasse();
            StudentsClasse classe = classe_repositorie.findById(cl.getId());
            LocalDate dateNaissance = LocalDate.now().minusYears(15);
            if (dateNaissance.isBefore(studens.getDateNaissance())) {
                throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
            }
            if (studens.getMatricule().length() != 12) {
                throw new NoteFundException("Le matricule n'est pas valide");
            }

            Studens studentExist = students_repositorie.findByMatriculeAndTelephone(studens.getMatricule(), studens.getTelephone());
            if (studentExist != null) {
                throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");

            }

            studens.setUrlPhoto("urlPhoto.png");
            LocalDate dateInscription = classe.getIdAnneeScolaire().getDebutAnnee();


            String plainPassword = studens.getPassword();
            studens.setPassword(passwordEncoder.encode(plainPassword));
            Studens savedStudent = students_repositorie.save(studens);
            classe.setEffectifs(classe.getEffectifs() + 1);
            StudentsClasse SavedClasse =  classe_repositorie.save(classe);

            Inscription newInscription = new Inscription();
            newInscription.setIdAdmin(inscrit.getIdAdmin());
            newInscription.setDate(dateInscription);
            newInscription.setIdClasse(SavedClasse);
            newInscription.setIdEtudiant(savedStudent);
            inscription_repositorie.save(newInscription);


        }

        return DTO_response_string.fromMessage("Ajout effectuer avec succès", 200);

    }

    public Page<Inscription> readAllByEtat(int value, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, size, sort);
        if(value == 1){
            Page<Inscription> studensList = inscription_repositorie.findStudentOfCurrentYearByEtat(pageable, true);
            if (studensList.isEmpty()) {
                return Page.empty(pageable);
            }
//            List<Studens> students = studensList.stream().map(Inscription::getIdEtudiant).toList();
            return studensList;
        }

            Page<Inscription> studensList = inscription_repositorie.findStudentOfCurrentYearByEtat(pageable, false);
        if (studensList.isEmpty()) {
            return Page.empty(pageable);
        }
//        List<Studens> students = studensList.stream().map(Inscription::getIdEtudiant).toList();
        return studensList;
    }

    //--------------disabled inscription
    public Object desabledInscription(long idInscription, long idClasse){
        System.out.println("je suis de dans");
        Inscription inscription = inscription_repositorie.getByIdAndIdClasseId(idInscription, idClasse);
        if (inscription != null) {
            System.out.println("---------------------inscription-------------"+inscription);

           inscription.setActive(!inscription.isActive());
           inscription_repositorie.save(inscription);
           System.out.println("---------------is active------"+inscription.isActive());
           return DTO_response_string.fromMessage("Mises à jour éffectué avec succès", 200);
        }
        throw new NoteFundException("L'inscription n'existe pas");

    }
}