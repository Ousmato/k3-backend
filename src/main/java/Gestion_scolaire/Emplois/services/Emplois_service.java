package Gestion_scolaire.Emplois.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Emplois.dtos.DtoEmploiByWeeks;
import Gestion_scolaire.Emplois.dtos.EmploiListForDtoEmploiWeek;
import Gestion_scolaire.Emplois.dtos.TeacherEmploiDTO;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.entity.Journee;
import Gestion_scolaire.Emplois.repositorie.Emplois_repositorie;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Teachers.dtos.SimpleTeacherDto;
import Gestion_scolaire.Teachers.dtos.TeacherDTO;
import Gestion_scolaire.Teachers.dtos.TeacherSemaineDTO;
import Gestion_scolaire.Teachers.dtos.TeacherVolHoraireDTO;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.services.Commom_methods;
import Gestion_scolaire.configuration.NoteFundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Emplois_service {

    private final Shared_repositories shared_repositories;

    private  final Commom_methods commom_methods;

    private final Shared_methods_service shared_methods_service;
    private final Emplois_repositorie emplois_repositorie;

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
    public boolean hasSeances(long idEmplois){
        return shared_repositories.getJournee_repositorie().existsByIdEmploisId(idEmplois);
    }
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

//    public List<Emplois> listEmploisActifOfAllClasses(long idAdmin){
//        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAdministraAndActive(idAdmin, true);
//
//        List<Emplois> emplois = shared_repositories.getEmplois_repositorie().findAllEmploisActif(LocalDate.now());
//        if (emplois.isEmpty()){
//            return  new ArrayList<>();
//        }
//        if(administrationUsers != null && administrationUsers.getIdRole().getTypeFiliere().equals(TypeFiliere.GESTIONS)){
//            return emplois.stream().filter(emploi ->
//                    emploi.getIdClasse().getIdFiliere().getIdFiliere().getTypeFiliere().equals(TypeFiliere.GESTIONS) ).toList();
//        }else if (administrationUsers != null &&  administrationUsers.getIdRole().getTypeFiliere().equals(TypeFiliere.SCIENTIFIQUES)){
//            return emplois.stream().filter(emploi ->
//                    emploi.getIdClasse().getIdFiliere().getIdFiliere().getTypeFiliere().equals(TypeFiliere.SCIENTIFIQUES) ).toList();
//        }else {
//            return emplois;
//        }
//
//
//    }

    public TeacherDTO allEmploisOfTeacherByIdAnnee(long idAnnee, long idTeacher) {
        // Récupérer les emplois et le prof
        List<Emplois> emplois = shared_repositories.getEmplois_repositorie()
                .getAllEmploiByOfTeacherAndIdAnnee(idAnnee, idTeacher);

        Teachers teacher = shared_repositories.getTeacher_repositorie()
                .findByIdAndActive(idTeacher, true);

        if (teacher == null) {
            return null;
        }

        // Init du DTO enseignant
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setNom(teacher.getNom());
        teacherDTO.setPrenom(teacher.getPrenom());

        // Map pour regrouper les emplois par semaine
        Map<String, List<TeacherEmploiDTO>> emploisParSemaine = new HashMap<>();
        int totalHeures = 0;

        // Toutes les journées de l'année pour le prof
        List<Journee> journees = shared_repositories.getJournee_repositorie()
                .allJourneesOfTeacherByPromotion(idTeacher, idAnnee);

        for (Emplois emp : emplois) {
            // Format semaine (corrigé)
            String semaineKey = commom_methods.transformDate(emp.getDateDebut(), emp.getDateFin());

            // Préparer le DTO emploi
            TeacherEmploiDTO emploiDTO = new TeacherEmploiDTO();
            emploiDTO.setNomModule(emp.getIdModule().getNomModule());
            emploiDTO.setFiliere(emp.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
            emploiDTO.setNiveau(shared_methods_service.abregNiveauName(
                    emp.getIdClasse().getIdFiliere().getIdNiveau().getNom()
            ));
            emploiDTO.setSemestre(emp.getIdSemestre().getNomSemetre());

            // Associer les journées à cet emploi (corrigé avec equals)
            List<Journee> associatedJournees = journees.stream()
                    .filter(j -> Objects.equals(j.getIdEmplois().getId(), emp.getId()))
                    .toList();

            List<TeacherVolHoraireDTO> volHoraires = new ArrayList<>();

            if (!associatedJournees.isEmpty()) {
                // CM
                List<Journee> cmJournees = associatedJournees.stream()
                        .filter(j -> j.getSeanceType().equals(Seance_type.cm))
                        .toList();

                if (!cmJournees.isEmpty()) {
                    TeacherVolHoraireDTO cmDTO = new TeacherVolHoraireDTO();
                    cmDTO.setTypeCours("CM");
                    cmDTO.setVolumeHoraire(emp.getIdModule().getVolHCM());
                    volHoraires.add(cmDTO);
                    totalHeures += emp.getIdModule().getVolHCM();
                }

                // TD
                List<Journee> tdJournees = associatedJournees.stream()
                        .filter(j -> j.getSeanceType().equals(Seance_type.td))
                        .toList();

                if (!tdJournees.isEmpty()) {
                    int vlHTD = commom_methods.getVolHoraire(tdJournees);
                    TeacherVolHoraireDTO tdDTO = new TeacherVolHoraireDTO();
                    tdDTO.setTypeCours("TD");
                    tdDTO.setVolumeHoraire(vlHTD);
                    volHoraires.add(tdDTO);
                    totalHeures += vlHTD;
                }

                emploiDTO.setVolHoraires(volHoraires);
            }

            // Ajouter à la semaine correspondante
            emploisParSemaine.computeIfAbsent(semaineKey, k -> new ArrayList<>()).add(emploiDTO);
        }

        // Convertir la Map en Liste de semaines
        List<TeacherSemaineDTO> semaines = emploisParSemaine.entrySet().stream()
                .map(entry -> {
                    TeacherSemaineDTO semaineDTO = new TeacherSemaineDTO();
                    semaineDTO.setPeriode(entry.getKey());
                    semaineDTO.setEmplois(entry.getValue());
                    return semaineDTO;
                })
                .toList();

        teacherDTO.setHeureTotal(totalHeures);
        teacherDTO.setSemaines(semaines);

        return teacherDTO;
    }


    public TeacherDTO allEmploiOfTeacherOfCurrentYear(long idTeacher){
        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(LocalDate.now());
        return allEmploisOfTeacherByIdAnnee(currentYear.getId(), idTeacher);

    }

    public List<Emplois> currentEmploiWithoutExamWithSeance(){
         List<Emplois> emploisList = shared_repositories.getEmplois_repositorie().findEmploisActifWithouExam(Seance_type.examen);
         if(emploisList.isEmpty()){
             return new ArrayList<>();
         }

         return emploisList;

    }

    public List<DtoEmploiByWeeks> currentEmploiHaveJourne(String value) {
        List<Emplois> emploisList = new ArrayList<>();
        if (value.equalsIgnoreCase("Default")){
            emploisList = shared_repositories
                    .getEmplois_repositorie()
                    .findEmploisWithAtLeastOneJournee();
        }else {
            emploisList = shared_repositories
                    .getEmplois_repositorie()
                    .findEmploisWithoutJournee();
        }


        if (emploisList.isEmpty()) {
            return new ArrayList<>();
        }

        // Grouper les Emplois par semaine (lundi de la semaine)
        Map<LocalDate, List<Emplois>> groupedByWeek = emploisList.stream()
                .collect(Collectors.groupingBy(e -> e.getDateDebut().with(DayOfWeek.MONDAY)));

        List<DtoEmploiByWeeks> result = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Emplois>> entry : groupedByWeek.entrySet()) {
            LocalDate weekStart = entry.getKey();
            LocalDate weekEnd = weekStart.plusDays(5); // Lundi à Samedi


            List<EmploiListForDtoEmploiWeek> emploisDTOs = new ArrayList<>();

            for (Emplois emploi : entry.getValue()) {
                List<Teachers> teachers = shared_repositories
                        .getTeacher_repositorie()
                        .getPrincipalProf(emploi.getId(), Seance_type.cm);

                EmploiListForDtoEmploiWeek dto = EmploiListForDtoEmploiWeek.toDto(emploi);

                if (!teachers.isEmpty()) {
                    dto.setNomTeacher(teachers.getFirst().getNom() + " " + teachers.getFirst().getPrenom()); // Prof principal (CM)
                } else {
                    dto.setNomTeacher(null); // Aucun prof principal trouvé
                }

                emploisDTOs.add(dto);
            }

            DtoEmploiByWeeks weekDto = new DtoEmploiByWeeks();
            weekDto.setWeekStart(weekStart);
            weekDto.setWeekEnd(weekEnd);
            weekDto.setEmplois(emploisDTOs);
            LocalDate today = LocalDate.now();

            if (!today.isBefore(weekStart) && !today.isAfter(weekEnd)) {
                weekDto.setStatus("En cours");
            } else if (today.isAfter(weekEnd)) {
                weekDto.setStatus("Dépassé");
            } else {
                weekDto.setStatus("En Attente");
            }

            result.add(weekDto);
        }

        result.sort(Comparator.comparing(DtoEmploiByWeeks::getWeekEnd).reversed());
        return result;
    }

    public List<SimpleTeacherDto> allTeachersHaveEmploisByIdAnnee(long idAnnee, long idSemestre) {
        return shared_repositories.getTeacher_repositorie()
                .geAllTeacherHaveJournees(idAnnee, idSemestre).stream()
                .map(SimpleTeacherDto::toDto)
                .sorted(Comparator.comparing(SimpleTeacherDto::getNom))
                .toList();
    }


}
