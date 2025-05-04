package Gestion_scolaire.students.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.students.dtos.DTO_scolarite;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.Student_DTO;
import Gestion_scolaire.students.entity.*;
import Gestion_scolaire.students.enumClass.Type_status;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.CuntStudentDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Student_service {

    private final Shared_repositories shared_repositories;

    private  final Validator validator;

    private final Shared_methods_service shared_methods_service;

    private final  PasswordEncoder passwordEncoder;

    private final Gestion_scolaire.Services.fileManagers fileManagers;

    String adminEmail = "ousmatotoure98@gmail.com";
    //    -----------------------------------------------------------------------------------
    @Transactional
    public Object update(Inscription inscrit, MultipartFile file) throws IOException {
        Students students = inscrit.getIdEtudiant();
        Inscription inscriptionExist = shared_repositories.getInscription_repositorie().findById(inscrit.getId());
        if (inscriptionExist == null) {
            throw new NoteFundException("L'inscription n'existe pas");
        }
        String telephone = String.valueOf(students.getTelephone());
        if (telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }

        inscrit = shared_methods_service.validateInscrit(inscrit);
//        if (students.getMatricule() != null && students.getMatricule().length() != 12) {
//            throw new NoteFundException("Le matricule n'est pas valide");
//        }

        Students studentExist = shared_repositories.getStudents_repositorie().findById(inscriptionExist.getIdEtudiant().getId());
        if (studentExist == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }
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


        inscriptionExist.setIdAdministrationUsers(inscrit.getIdAdministrationUsers());
//        inscriptionExist.setIdClasse(inscrit.getIdClasse());
        shared_repositories.getInscription_repositorie().save(inscriptionExist);

//        studentExist.setDate(LocalDate.now());
        shared_methods_service.updateIfNotEmpty(students.getMatricule(), studentExist::setMatricule);
        shared_methods_service.updateIfNotEmpty(students.getSexe(), studentExist::setSexe);
        shared_methods_service.updateIfNotEmpty(students.getEmail(), studentExist::setEmail);
        shared_methods_service.updateIfNotEmpty(students.getTelephone(), studentExist::setTelephone);
        shared_methods_service.updateIfNotEmpty(students.getDateNaissance(), studentExist::setDateNaissance);
        shared_methods_service.updateIfNotEmpty(students.getLieuNaissance(), studentExist::setLieuNaissance);
        shared_methods_service.updateIfNotEmpty(students.getNom(), studentExist::setNom);
        shared_methods_service.updateIfNotEmpty(students.getPrenom(), studentExist::setPrenom);
        shared_methods_service.updateIfNotEmpty(students.getCercleNaissance(), studentExist::setCercleNaissance);
        shared_methods_service.updateIfNotEmpty(students.getCommNaissance(), studentExist::setCommNaissance);
        shared_methods_service.updateIfNotEmpty(students.getLastNameFather(), studentExist::setLastNameFather);
        shared_methods_service.updateIfNotEmpty(students.getMotherName(), studentExist::setMotherName);
        shared_methods_service.updateIfNotEmpty(students.getNationalite(), studentExist::setNationalite);
        shared_methods_service.updateIfNotEmpty(students.getResidenceParent(), studentExist::setResidenceParent);
        shared_methods_service.updateIfNotEmpty(students.getAcademies(), studentExist::setAcademies);
        shared_methods_service.updateIfNotEmpty(students.getSeries(), studentExist::setSeries);
        shared_methods_service.updateIfNotEmpty(students.getQuartier(), studentExist::setQuartier);
        // shared_methods_service.updateIfNotEmpty(students.get);

        shared_repositories.getStudents_repositorie().save(studentExist);
        return DTO_response_string.updateMessage();

    }

    // methode pour desactiver un etudiant
    public Object desable(long id) {
        Students studentsExist = shared_repositories.getStudents_repositorie().findById(id);
        if (studentsExist != null) {
            studentsExist.setActive(!studentsExist.isActive());
            shared_repositories.getStudents_repositorie().save(studentsExist);
            return DTO_response_string.fromMessage("Changement d'etat effectué avec succé");
        }
        throw new NoteFundException("Student does not exist");
    }

    //    -----------------------------------------------------methode pour appeller tous les etudiants active----------------
    public Page<GetInscriptionDto> readAll(int page, int pageSize) {
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, pageSize,sort);
        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(LocalDate.now());
        Page<GetInscriptionDto> studensPages  = get_by_idAnneeScolaire(page,pageSize, currentYear.getId());
        if (studensPages.isEmpty()) {
            return Page.empty(pageable);
        }
        return studensPages;
    }

    //    ----------------------------find all student
    public List<Students> find_all() {
        return shared_repositories.getStudents_repositorie().findAll();
    }

    //    --------------------------------------------------methode appeler un etudiant par id----------------------
    public Students studenById(long id) {
        Students studentsExist = shared_repositories.getStudents_repositorie().findById(id);
        if (studentsExist != null) {
            return studentsExist;
        } else {
            throw new NoteFundException("l'étudiant est introuvable");
        }
    }

    //    ---------------------------method les etudiant de la classe par page-----------------------------------
    public Page<GetInscriptionDto > readAllByClassId(int page, int pageSize, long idClass) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Inscription> pages = shared_repositories.getInscription_repositorie().findByIdClasseId(idClass, pageable);
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
            List<SousFilieres> filiereSpecialites = shared_repositories.getSousFilieres_repositorie().findByIdClasseId(idClass);
            dto.getIdClasse().setSpecialites(filiereSpecialites);
            return dto;
        });
    }

    //update scolarite
    @Transactional
    public DTO_response_string update_scolarite(DTO_scolarite dto, long idAdmin) {
        LocalDate date = LocalDate.now();
        dto.setUpdateDate(date);

        Inscription studentInscrit = shared_repositories.getInscription_repositorie().findById(dto.getId());
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);

        if (studentInscrit == null) {
            throw new NoteFundException("Student does not exist");
        }

        if (administrationUsers == null) {
            throw new NoteFundException("Admin not found or inactive");
        }
        double seuilScolaire = shared_methods_service.getSeuilScolarite(studentInscrit.getIdEtudiant().getStatus());

        // Vérification si l'étudiant est REGULIER et si le paiement existe déjà
        if (studentInscrit.getIdEtudiant().getStatus().equals(Type_status.REGULIER)) {
            if (dto.getPayer() != seuilScolaire){
                throw new NoteFundException("Montant invalide pour ce type " + dto.getType());
            }
            Paiement pExist = shared_repositories.getPaiement_repositorie().getByIdInscriptionId(dto.getId());
            if (pExist != null) {
                throw new NoteFundException("L'étudiant a déjà payé");
            }
        }


        // Validation du paiement par rapport au seuil scolaire
        boolean isvalid = shared_methods_service.validateScolariteByStatus(dto.getPayer(), dto.getType(), seuilScolaire);
        if (!isvalid) {
            throw new NoteFundException("Montant invalide, le seuil est fixé à " + seuilScolaire);
        }

        // Création du nouvel objet Paiement
        Paiement newPaie = new Paiement();
        newPaie.setDateDePaiement(date);
        newPaie.setMontant(dto.getPayer());

        // Sauvegarde de l'inscription et du paiement
        studentInscrit.setPayer(true);
        newPaie.setIdInscription(studentInscrit);
        newPaie.setIdAdministrationUsers(administrationUsers);
        shared_repositories.getPaiement_repositorie().save(newPaie);
        Double sumTotal = shared_repositories.getPaiement_repositorie().sumMontant(dto.getId());

        if ( (sumTotal != null && sumTotal.equals(seuilScolaire)) || (dto.getPayer() != 0 && dto.getPayer() == seuilScolaire)) {
            studentInscrit.setTotalPayer(true);
        }
        shared_repositories.getInscription_repositorie().save(studentInscrit);

        return DTO_response_string.addMessage(); // Retour du message de succès
    }


    //    ----------------------------get list student by class id
    public List<GetInscriptionDto> get_by_classId(long idAnnee,long idClass) {
        List<Inscription> list = shared_repositories.getInscription_repositorie().getByIdClasseIdAndPayer(idAnnee,idClass, true);
//        System.out.println("-----------------" + list + "--------------------------");
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
        Page<Inscription> list = shared_repositories.getInscription_repositorie().getByIdClasseIdAnneeScolaireId(idAnneeScolaire, pageable);
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
        Inscription inscrit = shared_repositories.getInscription_repositorie().findById(idInscrit);
        if (inscrit == null) {
            throw new NoteFundException("L'inscription pour cet étudiant est introuvable");
        }
        GetInscriptionDto dto = GetInscriptionDto.toDto(inscrit);
        dto.setIdEtudiant(Student_DTO.toDTO(inscrit.getIdEtudiant()));
        return dto;
    }

    //get all reliquat of current year
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
        int inscrit = shared_repositories.getInscription_repositorie().countAllByPayer(true);
        int non_inscrit = shared_repositories.getInscription_repositorie().countAllByPayer(false);
        return CuntStudentDTO.getCount(inscrit, non_inscrit);
    }
    //    -------------------------------------------------
    public List<GetInscriptionDto> getListByIdAnneeAndIdClasse(long idAnnee, long idClasse) {
        // Récupérer les inscriptions actuelles pour la classe et l'année spécifiées
        List<Inscription> list = shared_repositories.getInscription_repositorie().findByIdClasseIdAnneeScolaireIdAndIdClasseId(idAnnee, idClasse);

        List<Students> studentsList = list.stream().map(Inscription::getIdEtudiant).toList();

        List<Inscription> inscriptionList = new ArrayList<>();

        // Récupérer la classe actuelle
        StudentsClasse nowClass = shared_repositories.getClasse_repositorie().findById(idClasse);

        // Calculer l'année suivante pour la réinscription
        int nextYear = nowClass.getIdAnneeScolaire().getFinAnnee().getYear() + 1;
        // Vérifier le niveau d'étude de la classe actuelle
        String currentNiveau = nowClass.getIdFiliere().getIdNiveau().getNom();

        // Si la classe est en LICENCE 1 (L1)
        if ("LICENCE 1".equals(currentNiveau)) {
            // Récupérer les étudiants inscrits en LICENCE 2 (L2) pour l'année suivante
            List<Inscription> inscriptionsL2 = shared_repositories.getInscription_repositorie().getListInscritByNiveauNameAndIdAnnee(
                    "LICENCE 2",
                    nextYear,
                    nowClass.getIdFiliere().getIdFiliere().getNomFiliere()
            );
            // Vérifier si la liste d'étudiants L2 n'est pas vide et ne contient pas déjà les étudiants L1
            if (!inscriptionsL2.isEmpty()) {
                List<Students> studentsListL2 = inscriptionsL2.stream()
                        .map(Inscription::getIdEtudiant).toList();
                List<String> listMatriculesL2 = studentsListL2.stream().map(Students::getTelephone).toList();

                // Exclure les étudiants L1 déjà inscrits en L2 pour l'année suivante en fonction de leur matricule
                List<Inscription> studentsNotReinscribed = new ArrayList<>(list);
                studentsNotReinscribed.removeIf(inscription ->
                        listMatriculesL2.contains(inscription.getIdEtudiant().getTelephone()));

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
//            System.out.println("----------------------------" + nowClass.getIdFiliere().getIdFiliere().getNomFiliere());

            List<Inscription> inscriptionsL3 = shared_repositories.getInscription_repositorie().getListInscritByNiveauNameAndIdAnnee(
                    nowClass.getIdFiliere().getIdNiveau().getNom(),
                    nextYear,
                    nowClass.getIdFiliere().getIdFiliere().getNomFiliere()
            );
//            System.out.println("-------------inscription l3---------------" +inscriptionsL3);

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
                inscrit = shared_methods_service.validateInscrit(inscrit);

                Students students = inscrit.getIdEtudiant();
                StudentsClasse cl = inscrit.getIdClasse();
                StudentsClasse classe = shared_repositories.getClasse_repositorie().findById(cl.getId());

//            if (students.getMatricule() != null && students.getMatricule().length() != 11) {
//                throw new NoteFundException("Le matricule n'est pas valide  " + students.getMatricule());
//            }

//                System.out.println("------------------voila--------------------" + students.getTelephone());
                if(students.getTelephone() == null || students.getTelephone().isEmpty()){
                    students.setTelephone("Neant");
                }
                if(students.getMatricule() != null && students.getTelephone() != null){
                    Students studentExist = shared_repositories.getStudents_repositorie().findByMatriculeAndTelephone(students.getMatricule(), students.getTelephone());
                    if (studentExist != null) {
                        throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");

                    }
                }


                students.setUrlPhoto("urlPhoto.png");
                LocalDate dateInscription = classe.getIdAnneeScolaire().getDebutAnnee();

                Students savedStudent = shared_repositories.getStudents_repositorie().save(students);
                StudentsClasse SavedClasse =  shared_repositories.getClasse_repositorie().save(classe);

                Inscription newInscription = new Inscription();
                newInscription.setIdAdministrationUsers(inscrit.getIdAdministrationUsers());
                newInscription.setDate(dateInscription);
                newInscription.setIdClasse(SavedClasse);
                newInscription.setIdEtudiant(savedStudent);
                newInscription.setNumeroInscrit(inscrit.getNumeroInscrit());
                shared_repositories.getInscription_repositorie().save(newInscription);

            processed++;
            } catch (Exception e) {
                skipped++;
                System.err.println("Skipped student: " + inscrit.getIdEtudiant().getNom());
                e.printStackTrace();
            }
        }

        return DTO_response_string.fromMessage("Importations effectuer avec succès");

    }

    public Page<GetInscriptionDto> readAllByEtat(int value, int page, int size){
        Sort sort = Sort.by(Sort.Order.asc("idEtudiant.nom"));
        Pageable pageable = PageRequest.of(page, size, sort);
        if(value == 1){
            Page<Inscription> studensList = shared_repositories.getInscription_repositorie().findStudentByEtat(LocalDate.now(), true, pageable);
            if (studensList.isEmpty()) {
                return Page.empty(pageable);
            }
            return studensList.map(list ->{
                GetInscriptionDto dto = GetInscriptionDto.toDto(list);
                dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant()));
                return dto;
            });
        }

            Page<Inscription> studensList = shared_repositories.getInscription_repositorie().findStudentByEtat(LocalDate.now(), false, pageable);
        if (studensList.isEmpty()) {
            return Page.empty(pageable);
        }
        return studensList.map(list ->{
            GetInscriptionDto dto = GetInscriptionDto.toDto(list);
            dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant()));
            return dto;
        });
    }

    //disabled inscription
    public Object desabledInscription(long idInscription, long idClasse){
        System.out.println("je suis de dans");
        Inscription inscription = shared_repositories.getInscription_repositorie().getByIdAndIdClasseId(idInscription, idClasse);
        if (inscription != null) {
           inscription.setActive(!inscription.isActive());
            shared_repositories.getInscription_repositorie().save(inscription);
           return DTO_response_string.fromMessage("Mises à jour éffectué avec succès");
        }
        throw new NoteFundException("L'inscription n'existe pas");

    }

    //get student by idInscrit
    public List<Students> getStudentsNotInscribed(List<Long> idsInscrit, long idClasse) {
        // Récupérer directement les étudiants non inscrits dans la classe
        List<Students> studentsNotInscribed = shared_repositories.getStudents_repositorie().getStudentsNotInscribed(idsInscrit);

        // Retourner la liste des étudiants non inscrits
        if(studentsNotInscribed.isEmpty()){
            return  new ArrayList<>();
        }

        return studentsNotInscribed;
    }

}