package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Administrators.repositories.Role_repositorie;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.PaieDTO;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.SharedService.Shared_service;
import Gestion_scolaire.Teachers.dtos.TeacherDTO;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.entity.Teacher_specialite;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.repositories.Paie_repositorie;
import Gestion_scolaire.Teachers.repositories.Specialite_repositorie;
import Gestion_scolaire.Teachers.repositories.TeacherSpecialite_repositorie;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Service
public class Teachers_service {
    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private Paie_repositorie paie_repositorie;

    @Autowired
    private Specialite_repositorie specialite_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private Shared_service shared_service;

    @Autowired
    private TeacherSpecialite_repositorie teacherSpecialite_repositorie;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    @Autowired
    private Commom_methods commom_methods;
    @Autowired
    private AdminRepositorie adminRepositorie;

    public Object add(Teachers teacher){
        Teachers teach = commom_methods.validateTeacher(teacher);
        System.out.println("---------------------------------je suis quand meme la---------------");
        Admin admin = adminRepositorie.findByIdAdministra(teach.getAdmin().getIdAdministra());
        if (admin == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        String roleName = admin.getIdRole().getNom();
        String roleAbrevigate = shared_service.abrevigateRoleName(roleName);
        if (!roleAbrevigate.equalsIgnoreCase("DER")){
            throw new NoteFundException("Autorisation refusée");
        }
        teach.setAdmin(admin);
        String pasEncode = passwordEncoder.encode(teach.getPassword());
        teach.setPassword(pasEncode);
        teacher_repositorie.save(teach);
        return DTO_response_string.addMessage();
    }

    //method pour desactiver un enseignant
    public Object desactive(long id){
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(id, true);
        if (teachersExist != null){
            teachersExist.setActive(!teachersExist.isActive());
        }
        return "desactiver avec succes";
    }

    //count number teacher
    public int countNumber(){
        return teacher_repositorie.countByActive(true);
    }

    //methode pour modifier
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
            if (t.getStatus() != null) {
                teachersExist.setStatus(t.getStatus());
            }
            if (t.getTelephone() != null) {
                teachersExist.setTelephone(t.getTelephone());
            }

            teacher_repositorie.save(teachersExist);
            return DTO_response_string.fromMessage("Mise a jours effectuer avec sucès");
        }else {
            throw new RemoteException("Enseignants n'existe pas");
        }

    }


    //method pour lire les enseignants actifs
    public Page<Teachers> readAll(int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("prenom"));

        Pageable pageable = PageRequest.of(page, size, sort);
        return teacher_repositorie.findAll(pageable);

    }

    public List<Teachers> readAll_teacher() {
        List<Teachers> list = teacher_repositorie.findAll();

        list.sort(Comparator.comparing(Teachers::getNom));
        return list;
    }


    //mehod pour appeler un enseignant
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
//                return DTO_response_string.fromMessage("Ajout effectuer avec succes");
//
//            }
////            throw new NoteFundException("Presence exist deja");
//
//        }
//        presence.setObservation(true);
//
//        // Sauvegarde de la présence dans tous les cas
//        listPresenceTeacher_repositorie.save(presence);
//        return DTO_response_string.fromMessage("Présence ajout effectuer avec succes");
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

    public Object importTeachers(List<Teachers> list, long idAdmin){
        Admin admin = adminRepositorie.findByIdAdministra(idAdmin);
        if (admin == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        if(list == null || list.isEmpty()){
            throw new NoteFundException("Choisir au moins un enseignant");
        }
        for (Teachers teacher : list){

            teacher.setAdmin(admin);
            add(teacher);
        }
        return DTO_response_string.addMessage();
    }

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
    public Page<TeacherDTO> getAllProfile(int page, int size) {

        // Créer un tri par le nom de l'enseignant (en supposant que 'idTeacher.nom' existe)
        Sort sorted = Sort.by(Sort.Order.asc("nom"));

        // Créer un objet Pageable avec pagination et tri
        Pageable pageable = PageRequest.of(page, size, sorted);

        // Récupérer la page de Teacher_specialite avec la pagination et le tri
        Page<Teachers> teachersPage = teacher_repositorie.findAll(pageable);

        // Liste pour collecter les TeacherDTO
        List<TeacherDTO> teacherDTOList = new ArrayList<>();

        // Parcourir les éléments de Teacher_specialite
        for (Teachers teacher : teachersPage) {
            // Convertir chaque Teacher_specialite en TeacherDTO
            TeacherDTO dto = TeacherDTO.toTeacherDTO(teacher);

            // Récupérer les spécialités associées à cet enseignant (optimisation possible)
            List<Specialites> specialites = specialite_repositorie.findAllByIdTeacher(teacher.getIdEnseignant());
            if(specialites.isEmpty()){
                dto.setSpecialitesList(new ArrayList<>());
            }
            // Ajouter la liste des spécialités au DTO
            dto.setSpecialitesList(specialites);

            // Ajouter le DTO à la liste
            teacherDTOList.add(dto);
        }
        // Retourner la page
        return new PageImpl<>(teacherDTOList, pageable, teachersPage.getTotalElements());
    }



    //-----------------------------------------
    public Teachers getTeacersFiltered(String searchTream){
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
