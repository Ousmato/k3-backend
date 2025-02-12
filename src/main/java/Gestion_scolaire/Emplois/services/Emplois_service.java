package Gestion_scolaire.Emplois.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Emplois.dtos.TeacherEmploiDTO;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.Emplois.repositorie.Emplois_repositorie;
import Gestion_scolaire.Emplois.repositorie.Journee_repositorie;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Repositories.AnneeScolaire_repositorie;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Teachers.dtos.TeacherDTO;
import Gestion_scolaire.Teachers.dtos.TeacherVolHoraireDTO;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.Teachers.services.Commom_methods;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class Emplois_service {

//    @Autowired
//    private Emplois_repositorie emplois_repositorie;
//    @Autowired
//    private Validator validator;
//
//    @Autowired
//    private AnneeScolaire_repositorie anneeScolaire_repositorie;
//
//    @Autowired
//    private Journee_repositorie journee_repositorie;
//
    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private Commom_methods commom_methods;

    @Autowired
    private Shared_methods_service shared_methods_service;
    
    public Object add(Emplois emplois) {
//        Set<ConstraintViolation<Emplois>> violation = validator.validate(emplois);
//        if (!violation.isEmpty()) {
//            throw new ConstraintViolationException(violation);
//        }
//        System.out.println("---------------" + emplois);

        List<Emplois> emplois_de_la_classe = shared_repositories.getEmplois_repositorie().findEmploisActifByIdClass(LocalDate.now(), emplois.getIdClasse().getId());

//        // Vérification des dates par rapport au semestre
        LocalDate dateDebut = emplois.getDateDebut();
        LocalDate dateFin = emplois.getDateFin();
//        LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
//        LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();
//
//        System.out.println("dateDebutSemestre: " + dateDebutSemestre);
//        System.out.println("dateFinSemestre: " + dateFinSemestre);
//
        System.out.println("dateDebut: " + dateDebut);
        System.out.println("dateFin: " + dateFin);
////        if(dateDebut.isBefore(LocalDate.now())){
////            throw new NoteFundException("invalide la date du début ne peut pas etre inferieur a aujourd'hui");
////        }
//        if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
//            throw new NoteFundException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
//        }

        // Vérification de l'existence d'un emploi pour la classe
        if (!emplois_de_la_classe.isEmpty()) {
            if (dateDebut.isBefore(emplois_de_la_classe.getLast().getDateFin())) {
                throw new NoteFundException("Il existe déjà un emploi en cours. Veuillez attendre cette date " + emplois_de_la_classe.getLast().getDateFin() + " ou modifier l'emploi du temps.");
            }
        }
        // Vérification finale sur les dates de début et de fin
        if (dateFin.isBefore(dateDebut)) {
            throw new NoteFundException("La date de fin ne peut pas être avant la date de début.");
        }

        // Si toutes les vérifications sont passées, enregistrez l'emploi
        shared_repositories.getEmplois_repositorie().save(emplois);
        return DTO_response_string.fromMessage("Ajout effectué avec succès");
    }

    //    -----------------------------------------mehode pour modifier-------------------------
    public  Object update(Emplois emplois) {
        Emplois emploisExist = shared_repositories.getEmplois_repositorie().findById(emplois.getId());
        if (emploisExist != null) {

            List<Journee> journees = shared_repositories.getJournee_repositorie().findByIdEmploisId(emploisExist.getId());
            if(!journees.isEmpty()){
                if(emploisExist.getDateDebut().isBefore(LocalDate.now())){
                    throw new NoteFundException("Impossible de modifier l'emploi du temps, des séances sont déjà programmées pour cet emploi.");

                }
            }
            LocalDate dateDebut = emplois.getDateDebut();
            LocalDate dateFin = emplois.getDateFin();
//            LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
//            LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();
//
//            if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
//                throw new NoteFundException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
//            }


            // Vérification finale sur les dates de début et de fin
            if (dateFin.isBefore(dateDebut)) {
                throw new NoteFundException("La date de fin ne peut pas être avant la date de début.");
            }

            emploisExist.setDateDebut(emplois.getDateDebut());
            emploisExist.setDateFin(emplois.getDateFin());
            emploisExist.setIdSemestre(emplois.getIdSemestre());
            emploisExist.setIdModule(emplois.getIdModule());

            shared_repositories.getEmplois_repositorie().save(emploisExist);
            return DTO_response_string.fromMessage("Modification effectué avec succès");
        }
        throw new NoteFundException("emplois n'existe pas");
    }

    //get all emplois by id class and id semestre and id module
    public List<Emplois> listEmploisByIdClassAndIdSemestreAndModule(long idClass, long idSemestre, long idModule) {
        List<Emplois> oldEmplois = shared_repositories.getEmplois_repositorie().findAllOldEmploisOfClassBySemestre(idClass, idSemestre,LocalDate.now().minusWeeks(1));
        if (oldEmplois.isEmpty()) {
            return new ArrayList<>();
        }
        return oldEmplois;
    }

//    --------------------------------method get by id emplois--------------------
    public Emplois getById(long id){
        Emplois emplois = shared_repositories.getEmplois_repositorie().findById(id);
        if (emplois != null){
            return emplois;
        }
        throw new RuntimeException("Auccune correspondance");
    }
//    ----------------------methode pour verifier l'existence des seances sur l'emplois du temps
//    public boolean hasSeances(long idEmplois){
//        return seance_repositorie.existsByIdEmploisId(idEmplois);
//    }
//    -------------------------------method to validate emplois
    public boolean validated(long idEmplois){
        Emplois emploiExist =shared_repositories.getEmplois_repositorie().findById(idEmplois);
        if(emploiExist !=null){
               emploiExist.setValid(!emploiExist.isValid());
            shared_repositories.getEmplois_repositorie().save(emploiExist);
                return  true;

        }
        return  false;
    }
//    -------------------methode de verification if emplois is valid or no
    public boolean isValid(long idEmplois) {
        Emplois emploiExist = shared_repositories.getEmplois_repositorie().findById(idEmplois);

        if (emploiExist != null) {
            return emploiExist.isValid();
        }
        return false;
    }
//--------------------------------get all emplois
    public List<Emplois> listEmploisActifs(long idClasse){
        List<Emplois>  list = shared_repositories.getEmplois_repositorie().findEmploisInActif(idClasse);
        if (list.isEmpty()){
            return  new ArrayList<>();
        }
        return list;
    }
//    ------------------------------get emplois active with seances

    public List<Emplois> listEmploisActifOfAllClasses(long idAdmin){
        Admin admin = shared_repositories.getAdminRepositorie().getByIdAdministraAndActive(idAdmin, true);

        List<Emplois> emplois = shared_repositories.getEmplois_repositorie().findAllEmploisActif(LocalDate.now());
        if (emplois.isEmpty()){
            return  new ArrayList<>();
        }
        if(admin != null && admin.getIdRole().getTypeFiliere().equals(TypeFiliere.GESTIONS)){
            return emplois.stream().filter(emploi ->
                    emploi.getIdClasse().getIdFiliere().getIdFiliere().getTypeFiliere().equals(TypeFiliere.GESTIONS) ).toList();
        }else if (admin != null &&  admin.getIdRole().getTypeFiliere().equals(TypeFiliere.SCIENTIFIQUES)){
            return emplois.stream().filter(emploi ->
                    emploi.getIdClasse().getIdFiliere().getIdFiliere().getTypeFiliere().equals(TypeFiliere.SCIENTIFIQUES) ).toList();
        }else {
            return emplois;
        }


    }

    public TeacherDTO allEmploisOfTeacherByIdAnnee(long idAnnee, long idTeacher) {
        List<Emplois> emplois = shared_repositories.getEmplois_repositorie().getAllEmploiByOfTeacherAndIdAnnee(idAnnee, idTeacher);
        Teachers teacher = shared_repositories.getTeacher_repositorie().findByIdEnseignantAndActive(idTeacher, true);
        if (teacher == null){
            return null;
        }
        TeacherDTO teacherDTO = new TeacherDTO();

        teacherDTO.setNom(teacher.getNom());
        teacherDTO.setPrenom(teacher.getPrenom());
        teacherDTO.setDiplome(teacher.getDiplome().toString());
        teacherDTO.setDateNaissance(teacher.getDateNaissance());
        teacherDTO.setDateNaissance(teacherDTO.getDateNaissance());
        teacherDTO.setStatus(teacher.getStatus());

        List<TeacherEmploiDTO> listEmplois = new ArrayList<>();
        // Récupérer toutes les journées d'un enseignant et d'une année scolaire en une seule requête
        List<Journee> journees = shared_repositories.getJournee_repositorie().allJourneesOfTeacherByPromotion(idTeacher, idAnnee);
        int totalHeures = 0;
        for (Emplois emp : emplois) {
            TeacherEmploiDTO emploiDTO = new TeacherEmploiDTO();

            emploiDTO.setSemaines(commom_methods.transformDate(emp.getDateDebut(), emp.getDateFin()));
            emploiDTO.setFiliere(emp.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
            emploiDTO.setNiveau(shared_methods_service.abregNiveauName(emp.getIdClasse().getIdFiliere().getIdNiveau().getNom()));
            emploiDTO.setSemestre(emp.getIdSemestre().getNomSemetre());

            emploiDTO.setNomModule(emp.getIdModule().getNomModule());

            List<Journee> associatedJournees = journees.stream()
                    .filter(j -> j.getIdEmplois().getId() == emp.getId()).toList();

            if (!associatedJournees.isEmpty()) {
                // Liste des volumes horaires pour l'emploi
                List<TeacherVolHoraireDTO> volHoraires = new ArrayList<>();

                // Filtrer les journées par type de séance CM
                List<Journee> cmJournees = associatedJournees.stream()
                        .filter(journee -> journee.getSeanceType().equals(Seance_type.cm))
                        .toList();
                if (!cmJournees.isEmpty()) {
                    TeacherVolHoraireDTO cmDTO = new TeacherVolHoraireDTO();
                    cmDTO.setTypeCours("CM");
                    cmDTO.setVolumeHoraire(emp.getIdModule().getVolHCM()); // Volume horaire CM du module
                    volHoraires.add(cmDTO);
                    totalHeures += emp.getIdModule().getVolHCM();
                }

                // Filtrer les journées par type de séance TD
                List<Journee> tdJournees = associatedJournees.stream()
                        .filter(journee -> journee.getSeanceType().equals(Seance_type.td))
                        .toList();
                if (!tdJournees.isEmpty()) {
                    TeacherVolHoraireDTO tdDTO = new TeacherVolHoraireDTO();
                    tdDTO.setTypeCours("TD");
                    int vlHTD = commom_methods.getVolHoraire(tdJournees); // Calculer le volume horaire TD
                    tdDTO.setVolumeHoraire(vlHTD);
                    volHoraires.add(tdDTO);
                    totalHeures += vlHTD;
                }

                // Ajouter la liste des volumes horaires au DTO de l'emploi
                emploiDTO.setVolHoraires(volHoraires);

            }

            listEmplois.add(emploiDTO);
            teacherDTO.setHeureTotal(totalHeures);
//            System.out.println("-----------------------nombre total : " + teacherDTO.getHeureTotal());

            teacherDTO.setTeacherEmploiList(listEmplois );
        }
        return teacherDTO;
    }

    public TeacherDTO allEmploiOfTeacherOfCurrentYear(long idTeacher){
        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(LocalDate.now());
        return allEmploisOfTeacherByIdAnnee(currentYear.getId(), idTeacher);

    }
}
