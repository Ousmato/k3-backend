package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Seance_service {
    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private ListPresenceTeacher_repositorie listPresenceTeacher_repositorie;

    @Autowired
    private Common_service common_service;

    @Autowired
    private SeancType_repositorie seancType_repositorie;

    @Autowired
    private Students_repositorie studentsRepositorie;

//    ----------------------------------------------------appels tout les seances par id---------------
public List<SeanceDTO> readByIdEmplois(long idEmplois) {
    // Récupérer l'emploi par ID
    Emplois emploisExist = emplois_repositorie.findById(idEmplois);

    // Vérifier si l'emploi existe
    if (emploisExist == null) {
        throw new NoteFundException("L'emploi n'existe pas");
    }

    // Récupérer les séances associées à l'emploi
    List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploisExist.getId());

    // Vérifier si la liste des séances n'est pas vide
    if (seancesList.isEmpty()) {
        // Retourner une liste vide si aucune séance n'est trouvée
        return new ArrayList<>();
    }

    // Vérifier si la date de fin de l'emploi est après la date actuelle
    if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
        // Trier les séances par date et heure de début
        seancesList.sort(Comparator
                .comparing(Seances::getDate)  // Trier par date
                .thenComparing(Seances::getHeureDebut));


        // Convertir les séances en DTO et ajouter les pauses
        return seancesList.stream().map(seance -> {
            SeanceDTO dto = SeanceDTO.toSeanceDTO(seance);
            LocalTime heureDebut = seance.getHeureDebut();
            LocalTime heureFin = seance.getHeureFin();

            List<String> plagesHoraires = common_service.calculerPlagesHoraires(heureDebut,heureFin);


            dto.setPlageHoraire(plagesHoraires);
            return dto;
        }).collect(Collectors.toList());
    }

    // Retourner une liste vide si la date de fin de l'emploi est passée
    return new ArrayList<>();
}


    //    ----------------------------------nombre d'heure d'un enseignant present par seance d'un emplois-------------------
    public List<Integer> getNbreBySeance(long idTeacher){
        return seance_repositorie.findNbreHeureBySeanceIdTeacher(idTeacher,LocalDate.now().getMonthValue());
    }
//-----------------------------method get seance by Id teacher-----------------------------
    public List<Seances> readSeanceByIdTeacher(long idTeacher){
        List<Seances> listExist = seance_repositorie.findAll_ByIdTeacher(idTeacher,LocalDate.now());
        if (listExist.isEmpty()){
            return new ArrayList<>();
        }
        return listExist;
    }
//--------------------------------------method add seance
    public Object createSeance(List<Seances> seancesList) {
        // Vérifie la date par rapport à l'emploi du temps
        for (Seances seances : seancesList) {
            if(seances.getHeureFin().equals(seances.getHeureDebut())){
                throw new NoteFundException("Invalid, verifier les heures");
            }
            if(seances.getHeureFin().isBefore(seances.getHeureDebut())){
                throw new NoteFundException("Invalid, l'heure de fin est inférieur a heure de début");
            }

            Emplois emploisExist = emplois_repositorie.findById(seances.getIdEmplois().getId());
            LocalDate dateSeance = seances.getDate();

            if (dateSeance.isBefore(emploisExist.getDateDebut()) || dateSeance.isAfter(emploisExist.getDateFin())) {
                throw new RuntimeException("La date de la séance doit être comprise entre la date de début et de fin de l'emploi du temps");
            }

            // Vérifie l'existence d'une séance similaire

            Seances seances1 = seance_repositorie.getByDateAndIdModuleIdAndIdEmploisId(
                    seances.getDate(), seances.getIdModule().getId(), seances.getIdEmplois().getId());
            if(seances1 != null){
//                if(seances1.getDate())
                throw new RuntimeException("Ce module existe deja pour cette date" );

            }

            List<Seances> existingSeances = seance_repositorie.getAllByDateAndIdTeacherIdEnseignant(seances.getDate(),seances.getIdTeacher().getIdEnseignant());

            for (Seances existingSeance : existingSeances) {
                if (common_service.isOverlapping(seances, existingSeance)) {
                    throw new RuntimeException("La séance chevauche une séance existante pour cet enseignant");
                }
            }
            List<Salles> sallesList = common_service.salle_occuper_toDay(seances.getDate());
            if(!sallesList.isEmpty()){
                for (Salles salle : sallesList) {
                    if (salle.getId() == seances.getIdSalle().getId()) {
                        throw new NoteFundException("Cette salle est déjà occupée");
                    }
                }

            }


            List<Seances> listExist = seance_repositorie.getAllByDate(seances.getDate());
            if (!listExist.isEmpty()) {
                for (Seances sc : listExist) {
                    boolean hasHeureIntervale = true;
                    if (seances.getHeureFin().isAfter(sc.getHeureDebut()) && seances.getHeureFin().isBefore(sc.getHeureFin())) {
                        hasHeureIntervale = false;
                        throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                    }
                }
            }

            seance_repositorie.save(seances);
            TeachersPresence tp = new TeachersPresence();
            tp.setDate(seances.getDate());
            tp.setIdSeance(seances);
            tp.setObservation(false);
            listPresenceTeacher_repositorie.save(tp);

        }
        return DTO_response_string.fromMessage("Ajoute effectué avec succès",200);
    }


//-------================================================================================
    public List<TeacherSeancesDTO> all_teacher() {
        List<Emplois> emploisList = emplois_repositorie.findAllEmploisActif(LocalDate.now());
        Map<Long, TeacherSeancesDTO> teacherMap = new HashMap<>();

        for (Emplois emploi : emploisList) {
            List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploi.getId());

            for (Seances seance : seancesList) {
                Long teacherId = seance.getIdTeacher().getIdEnseignant();
                TeacherSeancesDTO teacherSeancesDTO = teacherMap.get(teacherId);


                if (teacherSeancesDTO == null) {
                    teacherSeancesDTO = new TeacherSeancesDTO();
                    teacherSeancesDTO.setTeacher(TeacherDTO.toTeacherDTO(seance.getIdTeacher()));
                    teacherSeancesDTO.setClassRoom(new ArrayList<>());
                    teacherSeancesDTO.setSeances(new ArrayList<>());
                    teacherSeancesDTO.setEmplois(new ArrayList<>());
                    teacherMap.put(teacherId, teacherSeancesDTO);
                }

                boolean emploisExist = teacherSeancesDTO.getEmplois().contains(EmploisDTO.toEmploisDTO(emploi));
                if(!emploisExist){
                    teacherSeancesDTO.getEmplois().add(EmploisDTO.toEmploisDTO(emploi));
                }
                // Vérifier si la classe existe déjà dans teacherSeancesDTO.getClasses()
                boolean classeExists = teacherSeancesDTO.getClassRoom().contains(ClasseDTO.toClasseDTO(emploi.getIdClasse()));

                if (!classeExists) {
                    // Ajouter la classe seulement si elle n'existe pas déjà
                    teacherSeancesDTO.getClassRoom().add(ClasseDTO.toClasseDTO(emploi.getIdClasse()));
                }
                teacherSeancesDTO.getSeances().add(SeanceDTO.toSeanceDTO(seance));
            }
        }

        return new ArrayList<>(teacherMap.values());
    }
//    ------------------------------------------------------------------------
public Page<TeacherSeancesDTO> all_teachers_seance_active(int page, int pageSize) {
    Pageable pageable = PageRequest.of(page, pageSize);
    List<Emplois> emploisList = emplois_repositorie.findAllEmploisActif(LocalDate.now());
    Map<Long, TeacherSeancesDTO> teacherMap = new HashMap<>();

    for (Emplois emploi : emploisList) {
        List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploi.getId());

        for (Seances seance : seancesList) {
            Long teacherId = seance.getIdTeacher().getIdEnseignant();
            TeacherSeancesDTO teacherSeancesDTO = teacherMap.get(teacherId);

            if (teacherSeancesDTO == null) {
                teacherSeancesDTO = new TeacherSeancesDTO();
                teacherSeancesDTO.setTeacher(TeacherDTO.toTeacherDTO(seance.getIdTeacher()));
                teacherSeancesDTO.setClassRoom(new ArrayList<>());
                teacherSeancesDTO.setSeances(new ArrayList<>());
                teacherSeancesDTO.setEmplois(new ArrayList<>());
                teacherSeancesDTO.setStatus(seance.getIdTeacher().getStatus());
                System.out.println("---------------------"+teacherSeancesDTO);
                teacherMap.put(teacherId, teacherSeancesDTO);
            }

            boolean emploisExist = teacherSeancesDTO.getEmplois().contains(EmploisDTO.toEmploisDTO(emploi));
            if (!emploisExist) {
                teacherSeancesDTO.getEmplois().add(EmploisDTO.toEmploisDTO(emploi));
            }

            boolean classeExists = teacherSeancesDTO.getClassRoom().contains(ClasseDTO.toClasseDTO(emploi.getIdClasse()));
            if (!classeExists) {
                teacherSeancesDTO.getClassRoom().add(ClasseDTO.toClasseDTO(emploi.getIdClasse()));
            }

            teacherSeancesDTO.getSeances().add(SeanceDTO.toSeanceDTO(seance));
        }
    }

    List<TeacherSeancesDTO> teacherSeancesDTOList = new ArrayList<>(teacherMap.values());

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), teacherSeancesDTOList.size());

    List<TeacherSeancesDTO> subList = teacherSeancesDTOList.subList(start, end);

    return new PageImpl<>(subList, pageable, teacherSeancesDTOList.size());
}

//-----------------------------------------------------------------get teacherDTO by id
    public TeacherSeancesDTO getDetail(long idTeacher) {
        List<TeacherSeancesDTO> detail = all_teacher();
        for (TeacherSeancesDTO tsDto : detail) {
            if (tsDto.getTeacher().getIdEnseignant() == idTeacher) {
                tsDto.getSeances().sort(Comparator.comparing(SeanceDTO::getDate)
                        .thenComparing(SeanceDTO::getHeureFin, Comparator.nullsLast(Comparator.naturalOrder())));
                    System.out.println(tsDto);
                return tsDto;
            }
        }
        return null; // Retourner null si aucun enseignant ne correspond à l'id fourni
    }
//    -----------------------------------------methode delete
    public Object deletedSeance(long idSeance){
        Seances seanceExist = seance_repositorie.getById(idSeance);
        if(seanceExist != null){
           List<TeachersPresence> tpresent = listPresenceTeacher_repositorie.getByIdSeanceId(idSeance);
           if (tpresent.isEmpty()){
               seance_repositorie.delete(seanceExist);
               return DTO_response_string.fromMessage("Success la seance a ete supprimer", 200);

           }else {
               throw new NoteFundException("Impossible de supprimer des presences son deja associer");

           }

        }
        return DTO_response_string.fromMessage("La seance n'existe pas", 400);

    }
//-------------------------------------update method
    public Object update(Seances seances){
        Seances seanceExist = seance_repositorie.getById(seances.getId());
        if(seanceExist != null) {
            TeachersPresence tpresent = listPresenceTeacher_repositorie.findByIdSeanceId(seanceExist.getId());
//            System.out.println("t presence --------------------------------"+tpresent);

            if (!tpresent.isObservation()) {
                if(seances.getIdTeacher() != null){
                    List<Seances> existingSeances = seance_repositorie.getAllByDateAndIdTeacherIdEnseignant(seances.getDate(),seances.getIdTeacher().getIdEnseignant());
                    for (Seances existingSeance : existingSeances) {
                        if (common_service.isOverlapping(seances, existingSeance)) {
                            throw new RuntimeException("La séance chevauche une séance existante pour cet enseignant");
                        }
                    }
                }

                List<Seances> listExist = seance_repositorie.getAllByDate(seances.getDate());
                if (!listExist.isEmpty()) {
                    for (Seances sc : listExist) {
                        boolean hasHeureIntervale = true;
                        if (seances.getHeureFin().isAfter(sc.getHeureDebut()) && seances.getHeureFin().isBefore(sc.getHeureFin())) {
                            hasHeureIntervale = false;
                            throw new NoteFundException("L'heure de fin de la séance se trouve dans l'intervalle d'une autre séance existante.");
                        }
                    }
                }

                if (seances.getHeureDebut() != null) {
                    seanceExist.setHeureDebut(seances.getHeureDebut());
                }
                if (seances.getHeureFin() != null) {
                    seanceExist.setHeureFin(seances.getHeureFin());
                }
                if (seances.getIdModule() != null) {
                    seanceExist.setIdModule(seances.getIdModule());
                }
                if (seances.getIdTeacher() != null) {
                    seanceExist.setIdTeacher(seances.getIdTeacher());
                }
                if (seances.getIdSalle() != null) {
                    seanceExist.setIdSalle(seances.getIdSalle());
                }
                if(seances.getDate() != null){
                    seanceExist.setDate(seances.getDate());
                }
                if(seances.getIdEmplois() != null){
                    seanceExist.setIdEmplois(seances.getIdEmplois());
                }
                seance_repositorie.save(seanceExist);
            } else {
                throw new NoteFundException("Impossible de modifier la séance car des présences y sont déjà associées.");

            }
            return DTO_response_string.fromMessage("Modification effectuée avec succès", 200);
        }
       throw new NoteFundException("seance does not exist");
    }


//    --------------------------get seance by id
    public SeanceDTO get_by_id(long idSeance){
        Seances seanceExist = seance_repositorie.getById(idSeance);
        if (seanceExist != null) {

            return SeanceDTO.toSeanceDTO(seanceExist);
        }
        throw new NoteFundException("La seance n'existe pas");
    }
//------------------------------------------------------------------------------------------

//    ----------------------------get all seance config
    public List<DTO_Config> getAllSeanceConfig(long idEmplois) {
        List<SeanceConfig> configList = seancType_repositorie.getByIdSeanceIdEmploisId(idEmplois);
        List<DTO_Config> dtoConfigsList = new ArrayList<>(); // Créer une liste pour stocker les DTO

        for (SeanceConfig sc : configList) {
            DTO_Config dtoConfig = DTO_Config.toConfig(sc);
            String heureDebut = sc.getHeureDebut().toString();
            String heureFin = sc.getHeureFin().toString();
            dtoConfig.setPlageHoraire(heureDebut + " - " + heureFin);

            dtoConfigsList.add(dtoConfig);
        }
        return dtoConfigsList;
    }

    //    -----------------------------get seance config by id
    public SeanceConfig getSeanceConfig(long idConfig){
        SeanceConfig seanceExist = seancType_repositorie.findById(idConfig);
        if(seanceExist != null){
            return seanceExist;
        }
        throw new NoteFundException("La configuration pour cette séance n'existe pas");
    }
//    --------------------------------get seance configuration by Exam and session
    public List<DTO_Config> getAllSeanceConfig_byExaAndSess(){
        List<SeanceConfig> list = seancType_repositorie.findBySeanceTypeAndDate(Seance_type.examen, Seance_type.session, LocalDate.now());
        List<DTO_Config> dtoConfigsList = new ArrayList<>();

       if (list.isEmpty()){
            return new ArrayList<>();
        }

        for (SeanceConfig sc : list) {
            DTO_Config dtoConfig = DTO_Config.toConfig(sc);
            String heureDebut = sc.getHeureDebut().toString();
            String heureFin = sc.getHeureFin().toString();
            dtoConfig.setPlageHoraire(heureDebut + " - " + heureFin);
            dtoConfigsList.add(dtoConfig);
        }
        return dtoConfigsList;
    }

}



