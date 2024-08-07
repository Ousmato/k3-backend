package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.TeachersPresence;
import Gestion_scolaire.Repositories.ListPresenceTeacher_repositorie;
import Gestion_scolaire.Repositories.Paie_repositorie;
import Gestion_scolaire.Repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Teachers_service {
    @Autowired
    private Teacher_repositorie teacher_repositorie;

    @Autowired
    private ListPresenceTeacher_repositorie listPresenceTeacher_repositorie;

    @Autowired
    private Paie_repositorie paie_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessaSender messaSender;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Autowired
    private fileManagers fileManagers;

    public Object add(Teachers teachers, MultipartFile file) throws IOException {
        Teachers teachersExist = teacher_repositorie.findByEmail(teachers.getEmail());

        if (teachersExist != null) {
            throw new RuntimeException("L'adresse email existe déjà");
        }

        if (file != null && !file.isEmpty()) {
            String urlPhoto = fileManagers.saveFile(file);
            teachers.setUrlPhoto(urlPhoto);
        } else {
            teachers.setUrlPhoto("no_image.jpg"); // Exemple de valeur par défaut pour l'image
        }
        String plainPassword = teachers.getPassword();
        teachers.setPassword(passwordEncoder.encode(teachers.getPassword()));

        PendingEmail emailPend = new PendingEmail();
        emailPend.setToSend(teachers.getEmail());
        emailPend.setFromAdmin(adminEmail);
        emailPend.setBody("Bonjour M. %s %s%s,".formatted(teachers.getNom(), teachers.getPrenom(), messaSender.messageTeacher(teachers, plainPassword)));
        emailPend.setSubject("Confirmation");

        messaSender.sendSimpleMail(emailPend);
        teacher_repositorie.save(teachers);
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
//    --------------------------------------get presence by id seance
    public TeachersPresence getByIdSeanceId(long id){
        TeachersPresence presenceExist = listPresenceTeacher_repositorie.findByIdSeanceId(id);
        if(presenceExist == null){
            throw new NoteFundException("La seance avec cet id n'existe pas");
        }
        return presenceExist;
    }
//    -------------------------------------methode pour modifier-----------------------------------------
    public Object update(Teachers t, MultipartFile file) throws IOException {
       System.out.println("------------------------------------------------------- +" + t);
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(t.getIdEnseignant(),true);
        if(teachersExist != null){

            if (file != null && !file.isEmpty()) {
                String urlPhoto = fileManagers.updateFile(file, teachersExist.getUrlPhoto());
                teachersExist.setUrlPhoto(urlPhoto);
            }
//           ------------------------- cas ou l'image ne pas changer--------------
                teachersExist.setNom(t.getNom());
                teachersExist.setPrenom(t.getPrenom());
                teachersExist.setEmail(t.getEmail());
                teachersExist.setTelephone(t.getTelephone());
                teachersExist.setPassword(t.getPassword());

            teacher_repositorie.save(teachersExist);
            return DTO_response_string.fromMessage("Mise a jours effectuer avec sucès", 200);
        }else {
            throw new RemoteException("Enseignants n'existe pas");
        }

    }


//    --------------------------------method pour lire les enseignants acti------------------------------
    public Page<Teachers> readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return teacher_repositorie.findAll(pageable);
    }

    public List<Teachers> readAll_teacher() {
        return teacher_repositorie.findAll();
    }

//    -----------------------------------------------------mehod pour appeler un enseignant-------------
    public Teachers teachById(long id){
        return teacher_repositorie.findByIdEnseignant(id);
    }

//    -----------------------------------method to add teacher in presence list------------------
    public Object addPresence(TeachersPresence presence) {
        presence.setDate(LocalDate.now());
        // Recherche de la présence existante
        TeachersPresence teachersPresenceExist = listPresenceTeacher_repositorie.findByIdSeanceIdAndIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(
                presence.getIdSeance().getId(),
                presence.getIdSeance().getIdTeacher().getIdEnseignant(),
                LocalDate.now(),
                presence.getIdSeance().getHeureFin()
        );

        // Si aucune présence n'existe encore, ajouter la présence avec observation à true
        if (teachersPresenceExist != null) {
            if(!teachersPresenceExist.isObservation()){
                teachersPresenceExist.setObservation(true);
               listPresenceTeacher_repositorie.save(teachersPresenceExist);

                return DTO_response_string.fromMessage("Ajout effectuer avec succes", 200);

            }
//            throw new NoteFundException("Presence exist deja");

        }
        presence.setObservation(true);

        // Sauvegarde de la présence dans tous les cas
        listPresenceTeacher_repositorie.save(presence);
        return DTO_response_string.fromMessage("Présence ajout effectuer avec succes", 200);
    }



    //    --------------------------------------------method change-observation un teacher
    public Object change_observation(TeachersPresence presence){
        TeachersPresence presenceExist = listPresenceTeacher_repositorie.findByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateAndIdSeanceHeureFin(
                presence.getIdSeance().getIdTeacher().getIdEnseignant(), presence.getIdSeance().getDate() ,presence.getIdSeance().getHeureFin()
        );
        if(presenceExist != null){
            presenceExist.setObservation(!presenceExist.isObservation());
            listPresenceTeacher_repositorie.save(presenceExist);
            return DTO_response_string.fromMessage("Mise à jour effectuer avec succè",200);
        }
        throw new NoteFundException("Echec, Enseignants n'existe pas");
    }

//    ----------------------------------------method get status of teacher
    public List<TeachersPresence> getStatus(long idTeacher){

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        System.out.println(endOfMonth);
        List<TeachersPresence> list = listPresenceTeacher_repositorie.getByIdSeanceIdTeacherIdEnseignantAndIdSeanceDateBetween(idTeacher, startOfMonth, endOfMonth);
        System.out.println("----------------------"+ list + " "+ idTeacher);
        if(!list.isEmpty()){

            return list;

        }
       return new ArrayList<>();
    }

    //    ----------------------------------- method pour appeler laliste de presences------------------------------
    public Page<TeachersPresence> getListPresence(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        Page<TeachersPresence> list = listPresenceTeacher_repositorie.getByObservationAndIdSeanceDateBetween(true, startOfMonth, endOfMonth, pageable);
        List<Paie> listPaie = readAllPaie();
        List<TeachersPresence> newListPresence = new ArrayList<>();

        for (TeachersPresence presence : list) {
            boolean hasPaie = false;
            for (Paie paie : listPaie) {
                if (paie.getIdPresenceTeachers().getIdSeance().equals(presence.getIdSeance())) {
                    hasPaie = true;
                    break;
                }
            }
            if (!hasPaie) {
                newListPresence.add(presence);
            }
        }

        // Paginate newListPresence
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), newListPresence.size());
        List<TeachersPresence> subList = newListPresence.subList(start, end);

        return new PageImpl<>(subList, pageable, newListPresence.size());
    }

    //    ----------------------------------method get all teachers paie----------------------------------
    public List<Paie> readAllPaie(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Paie> paieList = paie_repositorie.findByDateBetween(startOfMonth, endOfMonth);
        if (paieList.isEmpty()){
            return new ArrayList<>();
        }
        return paieList;
    }
//    -------------------------------------method pour appeler les heures paiyer de teacher
    public List<Paie> getAll_paie_byIdTeacher(long idTeacher){

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Paie> paies = paie_repositorie.getByDateBetweenAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(
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
    public Object addPaie(Paie paie){

        paie.setDate(LocalDate.now());
        Paie paieExist = paie_repositorie.findByDateAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(
                paie.getDate(), paie.getIdPresenceTeachers().getIdSeance().getIdTeacher().getIdEnseignant());
        if(paieExist !=null) {
            long idSeanceExist = paieExist.getIdPresenceTeachers().getIdSeance().getId();
            long idSeanceNew = paie.getIdPresenceTeachers().getIdSeance().getId();

            if (idSeanceExist == idSeanceNew) {
                throw new RuntimeException("cet enseignant est deja paiyer a cette date");
            }
        }
        paie_repositorie.save(paie);
        return DTO_response_string.fromMessage("Paiement effectué avec succè",200);
    }
//    -----------------------------------------methode update paiement--------------------
    public Paie updatePaie(Paie paie){
        Paie paieExist = paie_repositorie.findByDateAndIdPresenceTeachersIdSeanceIdTeacherIdEnseignant(
                paie.getDate(), paie.getIdPresenceTeachers().getIdSeance().getIdTeacher().getIdEnseignant());
        if(paieExist == null){
            throw  new RuntimeException("paiement no exist");
        }
        paieExist.setCoutHeure(paie.getCoutHeure());
        paieExist.setDate(LocalDate.now());

//        int heure =   seance_service.nbreHeure(paie.getIdPresenceTeachers().getIdSeance().getIdTeacher());
//        paieExist.setNbreHeures(heure);
        return paie_repositorie.save(paieExist);
    }
}
