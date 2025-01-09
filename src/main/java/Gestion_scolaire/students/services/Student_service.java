package Gestion_scolaire.students.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.SharedService.Shared_service;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.Student_DTO;
import Gestion_scolaire.students.entity.Paiement;
import Gestion_scolaire.students.enumClass.InscriptionSeries;
import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.Classes.services.Classe_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.CuntStudentDTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Paiement_repositorie;
import Gestion_scolaire.students.repositories.Students_repositorie;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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
    private AdminRepositorie adminRepositorie;

    @Autowired
    private Paiement_repositorie paiement_repositorie;

    @Autowired
    private Validator validator;

    @Autowired
    private Shared_service shared_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    String adminEmail = "ousmatotoure98@gmail.com";



    //    -----------------------------------------------------------------------------------
    @Transactional
    public Object update(Inscription inscrit, MultipartFile file) throws IOException {
        Students students = inscrit.getIdEtudiant();
        Inscription inscriptionExist = inscription_repositorie.findById(inscrit.getId());
        if (inscriptionExist == null) {
            throw new NoteFundException("L'inscription n'existe pas");
        }
        String telephone = String.valueOf(students.getTelephone());
        if (telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }
//        LocalDate dateNaissance = LocalDate.now().minusYears(15);
//        if (dateNaissance.isBefore(students.getDateNaissance())) {
//            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
//        }
        if (students.getMatricule().length() != 12) {
            throw new NoteFundException("Le matricule n'est pas valide");
        }

        Students studentExist = students_repositorie.findByIdEtudiant(inscriptionExist.getIdEtudiant().getIdEtudiant());
        if (studentExist == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }

//        System.out.println("--------------------je suis ici");
        // Vérification du mot de passe
        if (students.getPassword() != null && !students.getPassword().isEmpty()) {
            studentExist.setPassword(passwordEncoder.encode(students.getPassword()));
        }else {
            studentExist.setPassword(studentExist.getPassword());
        }
        // Mise à jour de la photo si le fichier est fourni
        if (file != null && !file.isEmpty()) {
            String urlPhoto = fileManagers.updateFile(file, studentExist.getUrlPhoto());
            studentExist.setUrlPhoto(urlPhoto);
        }

        inscriptionExist.setIdAdmin(inscrit.getIdAdmin());
        inscriptionExist.setIdClasse(inscrit.getIdClasse());
        inscription_repositorie.save(inscriptionExist);

//        studentExist.setDate(LocalDate.now());
        studentExist.setMatricule(students.getMatricule());
        studentExist.setSexe(students.getSexe());
        studentExist.setEmail(students.getEmail());
        studentExist.setTelephone(students.getTelephone());
        studentExist.setDateNaissance(students.getDateNaissance());
        studentExist.setLieuNaissance(students.getLieuNaissance());
        studentExist.setNom(students.getNom());
        studentExist.setPrenom(students.getPrenom());


        students_repositorie.save(studentExist);
        return DTO_response_string.fromMessage("Modification effectuée avec succé");

    }

    //    -----------------------------methode pour desactiver un etudiant-------------------------------------------
    public Object desable(long id) {
        Students studentsExist = students_repositorie.findByIdEtudiant(id);
        if (studentsExist != null) {
            studentsExist.setActive(!studentsExist.isActive());
            students_repositorie.save(studentsExist);
            return DTO_response_string.fromMessage("Changement d'etat effectué avec succé");
        }
        throw new NoteFundException("Student does not exist");
    }

    //    -----------------------------------------------------methode pour appeller tous les etudiants active----------------
    public Page<GetInscriptionDto> readAll(int page, int pageSize) {
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, pageSize,sort);
        AnneeScolaire currentYear = annee_repositorie.findCurrentYear(LocalDate.now());
        Page<GetInscriptionDto> studensPages  = get_by_idAnneeScolaire(page,pageSize, currentYear.getId());
        if (studensPages.isEmpty()) {
            return Page.empty(pageable);
        }
        return studensPages;
    }

    //    ----------------------------find all student
    public List<Students> find_all() {
        return students_repositorie.findAll();
    }

    //    --------------------------------------------------methode appeler un etudiant par id----------------------
    public Students studenById(long id) {
        Students studentsExist = students_repositorie.findByIdEtudiant(id);
        if (studentsExist != null) {
            return studentsExist;
        } else {
            throw new NoteFundException("l'étudiant est introuvable");
        }
    }

    //    ---------------------------method les etudiant de la classe par page-----------------------------------
    public Page<GetInscriptionDto > readAllByClassId(int page, int pageSize, long idClass) {
        System.out.println("-----------icc----------" + pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Inscription> pages = inscription_repositorie.findByIdClasseId(idClass, pageable);
        if (pages.isEmpty()) {
            return Page.empty(pageable);
        }

        return pages.map(ip ->{
            // Création du DTO principal
            GetInscriptionDto dto = GetInscriptionDto.toDto(ip);

            // Transformation de l'étudiant en DTO
            if (ip.getIdEtudiant() != null) {
                dto.setIdEtudiant(Student_DTO.toDTO(ip.getIdEtudiant()));
            }

            return dto;
        });
    }

    //-----------------------------update scolarite
    public Object update_scolarite(long idIncription, long idAdmin, double scolarite) {
        LocalDate date = LocalDate.now();
        Inscription studentInscrit = inscription_repositorie.findById(idIncription);


        if (studentInscrit != null) {
            Paiement paiement = paiement_repositorie.findByDateDePaiementAndIdInscriptionId(date, idIncription);
            if(paiement == null) {
                throw new NoteFundException("La date ne corresponde pas");
            }



            if(studentInscrit.getIdEtudiant().getStatus() == Type_status.REGULIER && scolarite > 6000){
                throw new NoteFundException("Le frais de scolarité est invalide");
            }
//            double montant = studentInscrit.getScolarite() + scolarite;
//            paiement.setDateDePaiement(LocalDate.now());
//            paiement.setIdInscription(studentInscrit);
//            studentInscrit.setPayer(true);
//            studentInscrit.setAdminPaye(idAdmin);
            inscription_repositorie.save(studentInscrit);
            return DTO_response_string.fromMessage("Modification effectuer avec succé");
        }
        throw new NoteFundException("Student does not exist");
    }

    //    ----------------------------get list student by class id
    public List<GetInscriptionDto> get_by_classId(long idAnnee,long idClass) {
        List<Inscription> list = inscription_repositorie.getByIdClasseIdAndPayer(idAnnee,idClass, true);
        System.out.println("-----------------" + list + "--------------------------");
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        return list.stream().map(inscription -> {
           GetInscriptionDto dto = GetInscriptionDto.toDto(inscription);
            dto.setIdEtudiant(Student_DTO.toDTO(inscription.getIdEtudiant()));
            return  dto;
        }).toList();
    }

    //    -----------------------------get by id annee scolaire
    public Page<GetInscriptionDto> get_by_idAnneeScolaire(int page, int pageSize, long idAnneeScolaire) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Inscription> list = inscription_repositorie.getByIdClasseIdAnneeScolaireId(idAnneeScolaire, pageable);
        if (list.isEmpty()) {
            return Page.empty(pageable);
        }

        return list.map(inscription -> {
            GetInscriptionDto dto = GetInscriptionDto.toDto(inscription);
            dto.setIdEtudiant(Student_DTO.toDTO(inscription.getIdEtudiant()));
            return  dto;
        });
    }


    public GetInscriptionDto getInscriptionById(long idInscrit){
        Inscription inscrit = inscription_repositorie.findById(idInscrit);
        if (inscrit == null) {
            throw new NoteFundException("L'inscription pour cet étudiant est introuvable");
        }
        GetInscriptionDto dto = GetInscriptionDto.toDto(inscrit);
        dto.setIdEtudiant(Student_DTO.toDTO(inscrit.getIdEtudiant()));
        return dto;
    }
    //    ----------------------get all reliquat of current year
    public MontantsCunt getAll_reliquat() {
        MontantsCunt montantsCunt = new MontantsCunt();
//        double reliquatPro = inscription_repositorie.getReliquatForCurrentYear(Type_status.PROFESSIONNEL_PRIVEE);
//        double reliquatReg = inscription_repositorie.getReliquatForCurrentYearREG(Type_status.REGULIER);
//        double sumSocolaritePro = inscription_repositorie.sumScolariteForCurrentYearPro(Type_status.PROFESSIONNEL_PRIVEE);
//        double sumScolariteReg = inscription_repositorie.sumScolariteForCurrentYearReg(Type_status.REGULIER);
//
//        montantsCunt.setReliquatPro(reliquatPro);
//        montantsCunt.setReliquatReg(reliquatReg);
//        montantsCunt.setSumScolaritePro(sumSocolaritePro);
//        montantsCunt.setSumScolariteReg(sumScolariteReg);
//        montantsCunt.setReliquatTotal(reliquatPro + reliquatReg);
//        montantsCunt.setSumScolariteTotal(sumSocolaritePro + sumScolariteReg);

        return montantsCunt;
    }

    //    ------------------cunt all student inscrit
    public CuntStudentDTO cunt_student_inscrit() {
        int inscrit = inscription_repositorie.countAllByPayer(true);
        int non_inscrit = inscription_repositorie.countAllByPayer(false);
        return CuntStudentDTO.getCount(inscrit, non_inscrit);
    }

    //-------------------------------------------------------------------------------------
    @Transactional
    public Object reinscription(List<Inscription> inscrits, long idClasse, long idAdmin) {

        for (Inscription inscrit : inscrits){


            StudentsClasse newClass = classe_repositorie.findById(idClasse);

            // Trouver la classe
            if (newClass == null) {
                throw new NoteFundException("La classe est introuvable");
            }
//            List<GetInscriptionDto> inscriptionListExist = getListByIdAnneeAndIdClasse(newClass.getIdAnneeScolaire().getId(), idClasse);
//            if (!inscriptionListExist.isEmpty()) {
////            System.out.println("il est rentre dans dif de null");
////            boolean hasEquals = false;
//                for (GetInscriptionDto inscriptionExist : inscriptionListExist) {
////                System.out.println("------------le nouveaux-------"+inscrit.getIdEtudiant());
////                System.out.println("-------------les inscrits---------"+ inscriptionExist.getIdEtudiant());
////                    if (inscriptionExist.getIdEtudiant().equals(inscrit.getIdEtudiant())){
////                        desabledInscription(inscriptionExist.getId(),idClasse);
////
////                        return DTO_response_string.fromMessage("L'inscription existante a été désactivée pour cet étudiant.");
////
////                    }
//                }
//
//            }

//        System.out.println("-----------------liste-----des ----danscallse sup------"+inscriptionListExist + "\n");
            // Trouver l'étudiant
            Inscription studentExist = inscription_repositorie.findByIdEtudiantIdEtudiant(inscrit.getIdEtudiant().getIdEtudiant());
            if (studentExist == null) {
                throw new NoteFundException("L'étudiant est introuvable");
            }



            Admin adminExist = adminRepositorie.findByIdAdministra(idAdmin);
            if (adminExist == null) {
                throw new NoteFundException("L'administrateur est introuvable");

            }
            System.out.println("-------------------------inscrit------------" + inscrit);

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
            newInscription.setDate(LocalDate.now());
            newInscription.setDate(classSaved.getIdAnneeScolaire().getDebutAnnee());
            newInscription.setIdClasse(classSaved);

            newInscription.setIdAdmin(adminExist);
            inscription_repositorie.save(newInscription);
        }

        return DTO_response_string.fromMessage("Inscription effectuée avec succès");
    }

    //    -------------------------------------------------
    public List<GetInscriptionDto> getListByIdAnneeAndIdClasse(long idAnnee, long idClasse) {
        // Récupérer les inscriptions actuelles pour la classe et l'année spécifiées
        List<Inscription> list = inscription_repositorie.findByIdClasseIdAnneeScolaireIdAndIdClasseId(idAnnee, idClasse);

        List<Students> studentsList = list.stream().map(Inscription::getIdEtudiant).toList();

        List<Inscription> inscriptionList = new ArrayList<>();

        // Récupérer la classe actuelle
        StudentsClasse nowClass = classe_repositorie.findById(idClasse);
        System.out.println("-------------list---------------" +list.size());

        // Calculer l'année suivante pour la réinscription
        int nextYear = nowClass.getIdAnneeScolaire().getFinAnnee().getYear() + 1;
        // Vérifier le niveau d'étude de la classe actuelle
        String currentNiveau = nowClass.getIdFiliere().getIdNiveau().getNom();

        System.out.println("------------- " + nextYear +"---------------" +currentNiveau);
        // Si la classe est en LICENCE 1 (L1)
        if ("LICENCE 1".equals(currentNiveau)) {
            // Récupérer les étudiants inscrits en LICENCE 2 (L2) pour l'année suivante
            List<Inscription> inscriptionsL2 = inscription_repositorie.getListInscritByNiveauNameAndIdAnnee(
                    "LICENCE 2",
                    nextYear,
                    nowClass.getIdFiliere().getIdFiliere().getNomFiliere()
            );


//            System.out.println("-------------students l2---------------" +inscriptionsL2.size());

            // Vérifier si la liste d'étudiants L2 n'est pas vide et ne contient pas déjà les étudiants L1
            if (!inscriptionsL2.isEmpty()) {
                List<Students> studentsListL2 = inscriptionsL2.stream()
                        .map(Inscription::getIdEtudiant).toList();
                List<String> listMatriculesL2 = studentsListL2.stream().map(Students::getTelephone).toList();

                System.out.println("-------------inscription l2---------------" +inscriptionsL2.size());



                // Exclure les étudiants L1 déjà inscrits en L2 pour l'année suivante en fonction de leur matricule
                List<Inscription> studentsNotReinscribed = new ArrayList<>(list);
                studentsNotReinscribed.removeIf(inscription ->
                        listMatriculesL2.contains(inscription.getIdEtudiant().getTelephone()));
                System.out.println("-------------studentsNotReinscribed---------------" +studentsNotReinscribed.size());

                System.out.println("-------------list a return---------------" +list.size());

                return studentsNotReinscribed.stream().map(stnr ->{
                    GetInscriptionDto dto  = GetInscriptionDto.toDto(stnr);
                    dto.setIdEtudiant(Student_DTO.toDTO(stnr.getIdEtudiant()));
                    return dto;
                }).toList();
            }else {
                return list.stream().map(lst ->{
                    GetInscriptionDto dto  = GetInscriptionDto.toDto(lst);
                    dto.setIdEtudiant(Student_DTO.toDTO(lst.getIdEtudiant()));
                    return dto;
                }).toList();
            }
        }
        // Si la classe est en LICENCE 2 (L2)
        else if ("LICENCE 2".equals(currentNiveau)) {
            // Récupérer les étudiants inscrits en LICENCE 3 (L3) pour l'année suivante
            System.out.println("----------------------------" + nowClass.getIdFiliere().getIdFiliere().getNomFiliere());

            List<Inscription> inscriptionsL3 = inscription_repositorie.getListInscritByNiveauNameAndIdAnnee(
                    nowClass.getIdFiliere().getIdNiveau().getNom(),
                    nextYear,
                    nowClass.getIdFiliere().getIdFiliere().getNomFiliere()
            );
            System.out.println("-------------inscription l3---------------" +inscriptionsL3);

            List<Students> studentsListL3 = inscriptionsL3.stream().map(Inscription::getIdEtudiant).toList();

            // Vérifier si la liste d'étudiants L3 n'est pas vide et ne contient pas déjà les étudiants L2
            if (!inscriptionsL3.isEmpty()) {
                // Exclure les étudiants L2 déjà inscrits en L3 pour l'année suivante
                List<Students> studentsNotReinscribed = new ArrayList<>(studentsList);
                studentsNotReinscribed.removeAll(studentsListL3);  // Supprimer ceux qui sont déjà en L3

                for (Students student : studentsNotReinscribed) {
                    // Trouver l'inscription de l'étudiant dans la liste d'inscriptions initiale
                    for (Inscription inscription : inscriptionsL3) {
                        // Si l'étudiant correspond à l'inscrit, ajouter l'inscription à la liste
                        if (inscription.getIdEtudiant().equals(student)) {
                            inscriptionList.add(inscription);
                        }
                    }
                }
                return inscriptionList.stream().map(inscription -> {
                    GetInscriptionDto dto  = GetInscriptionDto.toDto(inscription);
                    dto.setIdEtudiant(Student_DTO.toDTO(inscription.getIdEtudiant()));
                    return dto;
                }).toList();
            }
        }

        // Si aucun étudiant n'est trouvé, renvoyer une liste vide ou null
        return new ArrayList<>();
    }


    @Transactional
    public Object addStudentsImport(List<Inscription> inscriptionList){
        System.out.println("--------------------------------le tout debut------------------" + inscriptionList.size());
        int processed = 0;
        int skipped = 0;

        for (Inscription inscrit : inscriptionList) {
            try {
                // Logique existante
                int currentYear = LocalDate.now().getYear();
                String plainPassword = "IUFP- "+currentYear+"@";
                inscrit.getIdEtudiant().setPassword(passwordEncoder.encode(plainPassword));

                if(inscrit.getIdEtudiant().getSexe().equalsIgnoreCase("F")){
                    inscrit.getIdEtudiant().setSexe("FEMME");
                }else if(inscrit.getIdEtudiant().getSexe().equalsIgnoreCase("M")) {
                    inscrit.getIdEtudiant().setSexe("HOMME");
                }else {
                    inscrit.getIdEtudiant().setSexe(inscrit.getIdEtudiant().getSexe());
                }

                Set<ConstraintViolation<Students>> violations = validator.validate(inscrit.getIdEtudiant());
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
                String dateString = inscrit.getIdEtudiant().getDateNaissance();

                if(dateString.toLowerCase().contains("vers")){
                    System.out.println("__________date naissance nee vers" + dateString);
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
                    System.out.println("__________date naissance" + inscrit.getIdEtudiant().getDateNaissance());
                    shared_service.processDate(inscrit.getIdEtudiant().getDateNaissance());
                }
                if (inscrit.getIdEtudiant().getSeries() != null) {
                    String seriAbreg = shared_service.abrevigateSerie(inscrit.getIdEtudiant().getSeries().toString());
                    System.out.println("___________serie----brut------" + seriAbreg + "___________");
                    inscrit.getIdEtudiant().setSeries(InscriptionSeries.fromAbbreviation(seriAbreg));

                }



                Students students = inscrit.getIdEtudiant();
                StudentsClasse cl = inscrit.getIdClasse();
                StudentsClasse classe = classe_repositorie.findById(cl.getId());

//            if (students.getMatricule() != null && students.getMatricule().length() != 11) {
//                throw new NoteFundException("Le matricule n'est pas valide  " + students.getMatricule());
//            }

//                System.out.println("------------------voila--------------------" + students.getTelephone());
                if(students.getTelephone() == null || students.getTelephone().isEmpty()){
                    students.setTelephone("Neant");
                }
                if(students.getMatricule() != null && students.getTelephone() != null){
                    Students studentExist = students_repositorie.findByMatriculeAndTelephone(students.getMatricule(), students.getTelephone());
                    if (studentExist != null) {
                        throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");

                    }
                }


                students.setUrlPhoto("urlPhoto.png");
                LocalDate dateInscription = classe.getIdAnneeScolaire().getDebutAnnee();

                Students savedStudent = students_repositorie.save(students);
                StudentsClasse SavedClasse =  classe_repositorie.save(classe);

                Inscription newInscription = new Inscription();
                newInscription.setIdAdmin(inscrit.getIdAdmin());
                newInscription.setDate(dateInscription);
                newInscription.setIdClasse(SavedClasse);
                newInscription.setIdEtudiant(savedStudent);
                newInscription.setNumeroInscrit(inscrit.getNumeroInscrit());
                inscription_repositorie.save(newInscription);

            processed++;
            } catch (Exception e) {
                skipped++;
                System.err.println("Skipped student: " + inscrit.getIdEtudiant().getNom());
                e.printStackTrace();
            }
        }

        System.out.println("Processed: " + processed);
        System.out.println("Skipped: " + skipped);

        return DTO_response_string.fromMessage("Importations effectuer avec succès");

    }

    public Page<GetInscriptionDto> readAllByEtat(int value, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, size, sort);
        AnneeScolaire currentYear = annee_repositorie.findCurrentYear(LocalDate.now());
        if(value == 1){
            Page<Inscription> studensList = inscription_repositorie.findStudentByEtat(LocalDate.now(), true, pageable);
            if (studensList.isEmpty()) {
                return Page.empty(pageable);
            }
            return studensList.map(list ->{
                GetInscriptionDto dto = GetInscriptionDto.toDto(list);
                dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant()));
                return dto;
            });
        }

            Page<Inscription> studensList = inscription_repositorie.findStudentByEtat(LocalDate.now(), false, pageable);
        if (studensList.isEmpty()) {
            return Page.empty(pageable);
        }
        return studensList.map(list ->{
            GetInscriptionDto dto = GetInscriptionDto.toDto(list);
            dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant()));
            return dto;
        });
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
           return DTO_response_string.fromMessage("Mises à jour éffectué avec succès");
        }
        throw new NoteFundException("L'inscription n'existe pas");

    }
}