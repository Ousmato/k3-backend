package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.ProfilDTO;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.PaieDTO;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Profile;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Repositories.Paie_repositorie;
import Gestion_scolaire.Repositories.Profile_repositorie;
import Gestion_scolaire.Repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class Teachers_service {
    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private Paie_repositorie paie_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private Profile_repositorie profile_repositorie;

    @Autowired
    private MessaSender messaSender;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Autowired
    private fileManagers fileManagers;

    @Autowired
    private Validator validator;

    @Transactional
    public Object add(ProfilDTO dto){
        Set<ConstraintViolation<Teachers>> violations = validator.validate(dto.getTeachers());
        if (!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
//        System.out.println("---------dto-----------"+dto);
        if( dto.getFilieres() == null || dto.getFilieres().isEmpty() || dto.getFilieres().stream().allMatch(Objects::isNull)){
            throw new NoteFundException("Ajouter au moins une filière d'enseignement");
        }
        Teachers teachersExist = teacher_repositorie.findByEmail(dto.getTeachers().getEmail());

        if (teachersExist != null) {
            throw new RuntimeException("L'adresse email existe déjà");
        }

        dto.getTeachers().setUrlPhoto("no_image.jpg"); // Exemple de valeur par défaut pour l'image
        String plainPassword = dto.getTeachers().getPassword();
        dto.getTeachers().setPassword(passwordEncoder.encode(dto.getTeachers().getPassword()));

//        PendingEmail emailPend = new PendingEmail();
//        emailPend.setToSend(teachers.getEmail());
//        emailPend.setFromAdmin(adminEmail);
//        emailPend.setBody("Bonjour M. %s %s%s,".formatted(teachers.getNom(), teachers.getPrenom(), messaSender.messageTeacher(teachers, plainPassword)));
//        emailPend.setSubject("Confirmation");
//
//        messaSender.sendSimpleMail(emailPend);
       Teachers teacherSaved = teacher_repositorie.save(dto.getTeachers());
        System.out.println("-----------------je suis la-------------------");

       boolean hasProfil = false;
       for (Filiere fil : dto.getFilieres()) {

           Profile profile = new Profile();
           Profile profil = profile_repositorie.findByIdFiliereIdAndIdTeacherIdEnseignant(fil.getId(),teacherSaved.getIdEnseignant());
           if (profil != null) {
               hasProfil = true;
               break;
           }

           profile.setIdTeacher(teacherSaved);
           profile.setIdFiliere(fil);
           profile.setIdAdmin(dto.getIdAdmin());
           profile_repositorie.save(profile);
       }
       if (hasProfil){
           throw new NoteFundException("Attention duplication de profile de l'enseignant");
       }
        return DTO_response_string.fromMessage("Ajout effectué avec sucès", 200);
    }

    //    ---------------------------------------method pour desactiver un enseignant-------------------------
    public Object desactive(long id){
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(id, true);
        if (teachersExist != null){
            teachersExist.setActive(!teachersExist.isActive());
        }
        return "desactiver avec succes";
    }
//    -----------------------------------------------count number teacher
    public int countNumber(){
        return teacher_repositorie.countByActive(true);
    }
//    -------------------------------------methode pour modifier-----------------------------------------
    public Object update(Teachers t) throws IOException {
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(t.getIdEnseignant(),true);
        if(teachersExist != null){
            //           ------------------------- cas ou l'image ne pas changer--------------
            updateIfNotEmpty(t.getNom(), teachersExist::setNom);
            updateIfNotEmpty(t.getPrenom(), teachersExist::setPrenom);
            updateIfNotEmpty(t.getEmail(), teachersExist::setEmail);
            updateIfNotEmpty(t.getSexe(), teachersExist::setSexe);
            updateIfNotEmpty(t.getPassword(), teachersExist::setPassword);

            // Mise à jour conditionnelle pour les champs potentiellement nulls
            if (t.getDiplome() != null) {
                teachersExist.setDiplome(t.getDiplome());
            }
//
            if (t.getStatus() != null) {
                teachersExist.setStatus(t.getStatus());
            }
            if (t.getTelephone() != 0) {
                teachersExist.setTelephone(t.getTelephone());
            }

            teacher_repositorie.save(teachersExist);
            return DTO_response_string.fromMessage("Mise a jours effectuer avec sucès", 200);
        }else {
            throw new RemoteException("Enseignants n'existe pas");
        }

    }


//    --------------------------------method pour lire les enseignants acti------------------------------
    public Page<Teachers> readAll(int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("prenom"));

        Pageable pageable = PageRequest.of(page, size, sort);
        return teacher_repositorie.findAll(pageable);

    }

    public List<Teachers> readAll_teacher() {
        return teacher_repositorie.findAll();
    }

    public List<Teachers> readAll_byProfile(long idFiliere) {
        List<Profile> list = profile_repositorie.findByIdFiliereId(idFiliere);
        if(list == null){
            return new ArrayList<>();
        }
        List<Teachers> teachers = new ArrayList<>();
        for (Profile profile : list) {
            teachers.add(profile.getIdTeacher());
        }
        return teachers;

    }

//    -----------------------------------------------------mehod pour appeler un enseignant-------------
    public Teachers teachById(long id){
        return teacher_repositorie.findByIdEnseignant(id);
    }

//    -----------------------------------method to add teacher in presence list------------------
//    public Object addPresence(TeachersPresence presence) {
//        presence.setDate(LocalDate.now());
//        // Recherche de la présence existante
//        TeachersPresence teachersPresenceExist = listPresenceTeacher_repositorie.findByIdSeanceIdAndIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(
//                presence.getIdSeance().getId(),
//                presence.getIdSeance().getIdTeacher().getIdEnseignant(),
//                LocalDate.now(),
//                presence.getIdSeance().getHeureFin()
//        );
//
//        // Si aucune présence n'existe encore, ajouter la présence avec observation à true
//        if (teachersPresenceExist != null) {
//            if(!teachersPresenceExist.isObservation()){
//                teachersPresenceExist.setObservation(true);
//               listPresenceTeacher_repositorie.save(teachersPresenceExist);
//
//                return DTO_response_string.fromMessage("Ajout effectuer avec succes", 200);
//
//            }
////            throw new NoteFundException("Presence exist deja");
//
//        }
//        presence.setObservation(true);
//
//        // Sauvegarde de la présence dans tous les cas
//        listPresenceTeacher_repositorie.save(presence);
//        return DTO_response_string.fromMessage("Présence ajout effectuer avec succes", 200);
//    }
//
//
//
//    //    --------------------------------------------method change-observation un teacher
//    public Object change_observation(TeachersPresence presence){
//        TeachersPresence presenceExist = listPresenceTeacher_repositorie.findByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(
//                presence.getIdSeance().getIdTeacher().getIdEnseignant(), presence.getIdSeance().getDate() ,presence.getIdSeance().getHeureFin()
//        );
//        if(presenceExist != null){
//            presenceExist.setObservation(!presenceExist.isObservation());
//            listPresenceTeacher_repositorie.save(presenceExist);
//            return DTO_response_string.fromMessage("Mise à jour effectuer avec succè",200);
//        }
//        throw new NoteFundException("Echec, Enseignants n'existe pas");
//    }

//    ----------------------------------------method get status of teacher
//    public List<TeachersPresence> getStatus(long idTeacher){
//
//        LocalDate today = LocalDate.now();
//        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
//        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
//        System.out.println(endOfMonth);
//        List<TeachersPresence> list = listPresenceTeacher_repositorie.getByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateBetween(idTeacher, startOfMonth, endOfMonth);
//        System.out.println("----------------------"+ list + " "+ idTeacher);
//        if(!list.isEmpty()){
//
//            return list;
//
//        }
//       return new ArrayList<>();
//    }

    //    ----------------------------------- method pour appeler laliste de presences------------------------------
//    public Page<TeachersPresence> getListPresence(int page, int pageSize) {
//        Pageable pageable = PageRequest.of(page, pageSize);
//        LocalDate today = LocalDate.now();
//        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
//        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
//        Page<TeachersPresence> list = listPresenceTeacher_repositorie.getByObservationAndIdSeanceDateBetween(true, startOfMonth, endOfMonth, pageable);
//        List<Paie> listPaie = readAllPaie();
//        List<TeachersPresence> newListPresence = new ArrayList<>();
//
//        for (TeachersPresence presence : list) {
//            boolean hasPaie = false;
//            for (Paie paie : listPaie) {
//                if (paie.getIdPresenceTeachers().getIdSeance().equals(presence.getIdSeance())) {
//                    hasPaie = true;
//                    break;
//                }
//            }
//            if (!hasPaie) {
//                newListPresence.add(presence);
//            }
//        }
//
//        // Paginate newListPresence
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), newListPresence.size());
//        List<TeachersPresence> subList = newListPresence.subList(start, end);
//
//        return new PageImpl<>(subList, pageable, newListPresence.size());
//    }

    //    ----------------------------------method get all teachers paie----------------------------------
    public List<PaieDTO> readAllPaie(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Paie> paieList = paie_repositorie.findByDateBetween(startOfMonth, endOfMonth);
        if (paieList.isEmpty()){
            return new ArrayList<>();
        }


        List<PaieDTO> dtoList = paieList.stream()
                .map(PaieDTO::fromPaieDTO)
                .toList();
        for (PaieDTO paie : dtoList) {
            int teacherHours = paie_repositorie.findTotalHoursByTeacherId(paie.getIdTeacher());
            paie.setNbreHeures(teacherHours);
        }
        return dtoList;
    }

    //    ---------------------
    public List<PaieDTO> getAllPaieByMonth(int month) {
        LocalDate startDate = LocalDate.of(2024, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<Paie> paieList = paie_repositorie.findByDateBetween(startDate, endDate);
        if (paieList.isEmpty()){
            return new ArrayList<>();
        }
        List<PaieDTO> dtoList = paieList.stream()
                .map(PaieDTO::fromPaieDTO)
                .toList();
        for (PaieDTO paie : dtoList) {
            int teacherHours = paie_repositorie.findTotalHoursByTeacherId(paie.getIdTeacher());
            paie.setNbreHeures(teacherHours);
        }
        return dtoList;
    }
//    -------------------------------------method pour appeler les heures paiyer de teacher
    public List<Paie> getAll_paie_byIdTeacher(long idTeacher){

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Paie> paies = paie_repositorie.getByDateBetweenAndJourneeIdTeacherIdEnseignant(
                startOfMonth, endOfMonth, idTeacher
        ) ;
        if (paies.isEmpty()){
            return new ArrayList<>();
        }
        return paies;
    }
//------------------------------------method pour les teachers paie par page de 10 personnes
    public Page<Paie> read_All_Paie_page(int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        Page<Paie> paieList = paie_repositorie.findByDateBetween(startOfMonth, endOfMonth, pageable);
        if (paieList.isEmpty()){
            return Page.empty(pageable);
        }
        return paieList;
    }
//    ---------------------------------mehod add paie-----------------------------------
//    public Object addPaie(Paie paie){
//
//        paie.setDate(LocalDate.now());
//        Paie paieExist = paie_repositorie.getByIdPresenceTeachersId(
//                 paie.getIdPresenceTeachers().getId());
//        if(paieExist !=null) {
//
//                throw new RuntimeException("cet enseignant est déjà payer a cette date");
//
//        }
//        paie_repositorie.save(paie);
//        return DTO_response_string.fromMessage("Paiement effectué avec succès",200);
//    }
//    -----------------------------------------methode update paiement--------------------
//    public Paie updatePaie(Paie paie){
//        Paie paieExist = paie_repositorie.findByDateAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(
//                paie.getDate(), paie.getIdPresenceTeachers().getIdSeance().getIdTeacher().getIdEnseignant());
//        if(paieExist == null){
//            throw  new RuntimeException("paiement no exist");
//        }
//        paieExist.setCoutHeure(paie.getCoutHeure());
//        paieExist.setDate(LocalDate.now());
//
////        int heure =   seance_service.nbreHeure(paie.getIdPresenceTeachers().getIdSeance().getIdTeacher());
////        paieExist.setNbreHeures(heure);
//        return paie_repositorie.save(paieExist);
//    }

    public void updateIfNotEmpty(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
    }
    //---------------------------------
    public Page<ProfilDTO> getAllProfile(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Profile> profilePage = profile_repositorie.findAll();

        if (profilePage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

         // Filtrer les enseignants uniques avant de les transformer en DTO
        Map<Long, ProfilDTO> enseignantMap = new HashMap<>();

        for (Profile profile : profilePage) {
            Long enseignantId = profile.getIdTeacher().getIdEnseignant();

            System.out.println("------------------id enseignant: " + profile);
            if (!enseignantMap.containsKey(enseignantId)) {
                ProfilDTO profilDTO = new ProfilDTO();
                profilDTO.setId(profile.getId());
                profilDTO.setTeachers(profile.getIdTeacher());

                // Ajouter les filières associées
                List<Filiere> filiereList = profile_repositorie
                        .getByIdTeacherIdEnseignant(enseignantId)
                        .stream()
                        .map(Profile::getIdFiliere)
                        .collect(Collectors.toList());

                profilDTO.setFilieres(filiereList);
                enseignantMap.put(enseignantId, profilDTO);
            }
        }

        // Transformer la map en liste et appliquer la pagination sur les enseignants uniques
        List<ProfilDTO> uniqueEnseignants = new ArrayList<>(enseignantMap.values());

        // Appliquer la pagination sur les enseignants uniques
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), uniqueEnseignants.size());
        List<ProfilDTO> paginatedList = uniqueEnseignants.subList(start, end);

        // Retourner un objet Page de ProfilDTO avec les enseignants uniques
        return new PageImpl<>(paginatedList, pageable, uniqueEnseignants.size());
    }


    //-----------------------------------------
    public Teachers getTeacersFiltered(int searchTream){
        return teacher_repositorie.getAllByTelephone(searchTream);
    }

    public List<Teachers> getListFiltered(String searchTream){
        List<Teachers> list = teacher_repositorie.findByNomContaining(searchTream);
        if (list.isEmpty()){
            return new ArrayList<>();
        }
       return  list.stream().limit(10).toList();
    }
}
