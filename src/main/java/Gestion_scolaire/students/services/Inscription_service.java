package Gestion_scolaire.students.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import Gestion_scolaire.Repositories.AnneeScolaire_repositorie;
import Gestion_scolaire.Services.Common_service;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Shareds.Shared_services;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.DTO_scolarite;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.InscriptionDTO;
import Gestion_scolaire.students.dtos.Student_DTO;
import Gestion_scolaire.students.entity.*;
import Gestion_scolaire.students.enumClass.StudentDiplome;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Students_repositorie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class Inscription_service {

    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private MehodShared mehodShared;

    @Autowired
    private Shared_methods_service shared_methods_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;


    @Transactional
    public Object add(InscriptionDTO inscrit, MultipartFile file) throws IOException {

        int currentYear = LocalDate.now().getYear();

        String plainPassword = "IUFP- "+currentYear+"@";
        inscrit.getIdEtudiant().setPassword(passwordEncoder.encode(plainPassword));

        Students student = mehodShared.validateSudent(inscrit.getIdEtudiant());
        Students studentExist = shared_repositories.getStudents_repositorie().findByTelephone( student.getTelephone());
        if (studentExist == null) {

            if(file != null && !file.isEmpty()) {
                String urlPhoto = fileManagers.saveFile(file);
                student.setUrlPhoto(urlPhoto);
            }else {
                student.setUrlPhoto("photo");
            }

            if (inscrit.getIdClasse() != null) {

                String niveauName = inscrit.getIdClasse().getIdFiliere().getIdNiveau().getNom();
                if(student.getDiplome().equals(StudentDiplome.DUT) && niveauName.equals("LICENCE 1") || niveauName.equals("LICENCE 2") ){
                    throw new NoteFundException("Atteintion un diplome DUT ne peut pas s'inscrire en " +niveauName);
                }

                Inscription newIncription = new Inscription();
                newIncription.setDate(LocalDate.now());
                if (newIncription.getDate().isBefore(inscrit.getIdClasse().getIdAnneeScolaire().getDebutAnnee()) || newIncription.getDate().isAfter(inscrit.getIdClasse().getIdAnneeScolaire().getFinAnnee())) {
                    throw new NoteFundException("Inscription non valide veillez choisir une année scolaire en cours");
                }

                shared_repositories.getClasse_repositorie().save(inscrit.getIdClasse());

                Students newStudent = shared_repositories.getStudents_repositorie().save(student);
                newIncription.setIdClasse(inscrit.getIdClasse());
                newIncription.setIdEtudiant(newStudent);
                newIncription.setPayer(false);
                newIncription.setIdAdmin(inscrit.getIdAdmin());
                shared_repositories.getInscription_repositorie().save(newIncription);

//                PendingEmail emailPend = new PendingEmail();
//                emailPend.setToSend(inscrit.getEmail());
//                emailPend.setFromAdmin(adminEmail);
//                emailPend.setBody("Bonjour M. %s %s%s,".formatted(inscrit.getNom(), inscrit.getPrenom(), messaSender.message(inscrit, plainPassword)));
//                emailPend.setSubject("Confirmation");
//
//                messaSender.sendSimpleMail(emailPend);
            }
            return DTO_response_string.fromMessage("Inscription effectuée avec succés ");
        } else {
            throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");
        }

    }

    public DTO_scolarite getScolariteAndReliquatByIdIncrit(long idInscrit){
        Inscription inscription = shared_repositories.getInscription_repositorie().findById(idInscrit);
        if (inscription == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }
        DTO_scolarite dto = DTO_scolarite.getScolarite_student(inscription);
        System.out.println("---------------dto 11 : "+ dto);

        Double montantPayer = shared_repositories.getPaiement_repositorie().sumMontant(idInscrit);
        if(montantPayer != null){

            dto.setPayer(montantPayer);
//            dto.setUpdateDate();
            dto.setReliquat(dto.getScolarite() - montantPayer);
            dto.setType(inscription.getIdEtudiant().getStatus());
        }else {
            dto.setType(inscription.getIdEtudiant().getStatus());
            dto.setReliquat(shared_methods_service.getSeuilScolarite(inscription.getIdEtudiant().getStatus()));
            dto.setPayer(0.0);
        }
//        System.out.println("---------------dto : "+ dto);
        return dto;
    }

    public List<GetInscriptionDto> getAllInscritByYear(long idAnnee){
        List<Inscription> inscriptionList = shared_repositories.getInscription_repositorie().findByIdClasseIdAnneeScolaireId(idAnnee);
        if (inscriptionList.isEmpty()){
            return new ArrayList<>();
        }
        return inscriptionList.stream()
        .map(list -> {
            GetInscriptionDto dto = GetInscriptionDto.toDto(list);
            dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant())); // Correction ici
            return dto;
        })
        .sorted(Comparator.comparing(dto -> dto.getIdEtudiant().getNom())) // Tri par nom
        .toList();
    }

    public List<GetInscriptionDto> getInscritCurrentYear(){
        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(LocalDate.now());
        return getAllInscritByYear(currentYear.getId());
    }

    public Inscription getInscription(long id) {
        Inscription inscription = shared_repositories.getInscription_repositorie().findById(id);
        if (inscription == null) {
            throw new RuntimeException("l'etudiant n'existe pas");
        }
        return inscription;
    }

    public Inscription getById(Long id) {
        Optional<Inscription> inscription = shared_repositories.getInscription_repositorie().findById(id);
        if (inscription.isEmpty()) {
            throw new RuntimeException("L'étudiant n'existe pas");
        }
        return inscription.get();  // Retourne l'objet Inscription directement
    }

    @Transactional
    public Object reinscription(List<Students> students, long idClasse, long idAdmin) {
        StudentsClasse newClass = shared_methods_service.getPreviousClasseById(idClasse);

        // Trouver la classe
        if (newClass == null) {
            throw new NoteFundException("La classe est introuvable");
        }
        for (Students student : students){
            System.out.println("--------------me voila" + student.getNom());

          // Trouver l'étudiant
            Students studentExist = shared_repositories.getStudents_repositorie().findByIdEtudiant(student.getIdEtudiant());
            if (studentExist == null) {
                throw new NoteFundException("L'étudiant est introuvable");
            }



            Admin adminExist = shared_repositories.getAdminRepositorie().findByIdAdministra(idAdmin);
            if (adminExist == null) {
                throw new NoteFundException("L'administrateur est introuvable");

            }
//            System.out.println("-------------------------inscrit------------" + inscrit);

            // Trouver l'année scolaire
            AnneeScolaire newYear = shared_repositories.getAnneeScolaire_repositorie().findById(newClass.getIdAnneeScolaire().getId());
            if (newYear == null) {
                throw new NoteFundException("L'année scolaire est introuvable");
            }
            // Création d'un nouvel étudiant pour la réinscription
            String numInscrit = shared_methods_service.getNumInscription(student.getIdEtudiant());
//
//            newClass.setEffectifs(newClass.getEffectifs() + 1);
            StudentsClasse classSaved = shared_repositories.getClasse_repositorie().save(newClass);
            Inscription newInscription = new Inscription();
            newInscription.setIdEtudiant(student);
            newInscription.setNumeroInscrit(numInscrit);
            newInscription.setDate(LocalDate.now());
            newInscription.setDate(classSaved.getIdAnneeScolaire().getDebutAnnee());
            newInscription.setIdClasse(classSaved);

            newInscription.setIdAdmin(adminExist);
            shared_repositories.getInscription_repositorie().save(newInscription);
        }

        return DTO_response_string.fromMessage("Inscription effectuée avec succès");
    }

    public Object addInscrit_to_souFiliere(long idInscrit, long idSousFiliere){
       Inscription inscrit = getInscription(idInscrit);
        SousFilieres sousFiliere = shared_repositories.getSousFilieres_repositorie().findById(idSousFiliere);
        if (sousFiliere == null) {
            throw new NoteFundException("Cette Filière est introuvable");
        }
        InscriptionSousFilieres inscriptionSousFilieresExist = shared_repositories.getInscriptionSousFiliere_repositorie().findByIdSousFiliereIdAndIdInscriptionId(idSousFiliere, idInscrit);
        if (inscriptionSousFilieresExist != null) {
            throw new NoteFundException("L'étudiant est déjà inscrit dans cette spécialité");
        }
        InscriptionSousFilieres inscriptionSouFiliere = new InscriptionSousFilieres();
        inscriptionSouFiliere.setIdInscription(inscrit);
        inscriptionSouFiliere.setIdSousFiliere(sousFiliere);
        shared_repositories.getInscriptionSousFiliere_repositorie().save(inscriptionSouFiliere);
        return DTO_response_string.addMessage();
    }

}
