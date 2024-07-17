package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Paie;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Models.TeachersPresence;
import Gestion_scolaire.Repositories.ListPresenceTeacher_repositorie;
import Gestion_scolaire.Repositories.Paie_repositorie;
import Gestion_scolaire.Repositories.Teacher_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Seance_service seance_service;


    @Autowired
    private fileManagers fileManagers;

    public Teachers add(Teachers teachers, MultipartFile file) throws IOException {
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

        return teacher_repositorie.save(teachers);
    }

    //    ---------------------------------------method pour desactiver un enseignant-------------------------
    public Object desactive(long id){
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(id, true);
        if (teachersExist != null){
            teachersExist.setActive(!teachersExist.isActive());
        }
        return "desactiver avec succes";
    }
//    -------------------------------------methode pour modifier-----------------------------------------
    public Teachers update(Teachers t, MultipartFile multipartFile) throws IOException {
        Teachers teachersExist = teacher_repositorie.findByIdEnseignantAndActive(t.getIdEnseignant(),true);
        if(teachersExist != null){
            Teachers teachers;
//           ------------------------- cas ou l'image ne pas changer--------------
                teachersExist.setNom(t.getNom());
                teachersExist.setPrenom(t.getPrenom());
                teachersExist.setEmail(t.getEmail());
                teachersExist.setTelephone(t.getTelephone());
                teachersExist.setPassword(t.getPassword());

            if(!multipartFile.isEmpty()) {
                String urlPhoto =  fileManagers.updateFile(multipartFile, teachersExist.getUrlPhoto());
                teachersExist.setUrlPhoto(urlPhoto);

            }
            return   teacher_repositorie.save(teachersExist);
        }else {
            throw new RemoteException("Enseignants n'existe pas");
        }

    }


//    --------------------------------method pour lire les enseignants acti------------------------------
    public List<Teachers> readAll(){
      List<Teachers> teachersList = teacher_repositorie.findByActive(true);
      if (teachersList.isEmpty()){
          return new ArrayList<>();
      }
      return teachersList;


    }

//    -----------------------------------------------------mehod pour appeler un enseignant-------------
    public Teachers teachById(long id){
        return teacher_repositorie.findByIdEnseignantAndActive(id, true);
    }

//    -----------------------------------method to add teacher in presence list------------------
    public TeachersPresence addPresence(TeachersPresence presence) {
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
               return listPresenceTeacher_repositorie.save(teachersPresenceExist);
            }

        }
        presence.setObservation(true);

        // Sauvegarde de la présence dans tous les cas
        return listPresenceTeacher_repositorie.save(presence);
    }



    //    --------------------------------------------method absenter un teacher
    public boolean abscenter(TeachersPresence presence){
        TeachersPresence presenceExist = listPresenceTeacher_repositorie.findByIdSeanceIdTeacherIdEnseignantAndObservationAndIdSeanceDateAndIdSeanceHeureFin(
                presence.getIdSeance().getIdTeacher().getIdEnseignant(), true, LocalDate.now(),presence.getIdSeance().getHeureFin()
        );
        if(presenceExist != null){
            presenceExist.setObservation(false);
            listPresenceTeacher_repositorie.save(presenceExist);
            return true;
        }
        return false;
    }

//    ----------------------------------------method get status of teacher
    public List<TeachersPresence> getStatus(long idTeacher){
        List<TeachersPresence> list = listPresenceTeacher_repositorie.findByIdSeanceIdTeacherIdEnseignantAndIdSeanceDate(idTeacher, LocalDate.now());
        if(!list.isEmpty()){
            return list;
        }
       return new ArrayList<>();
    }

    //    ----------------------------------- method pour appeler laliste de presences------------------------------
    public List<TeachersPresence> getListPresence(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<TeachersPresence> list = listPresenceTeacher_repositorie.findByObservationAndIdSeanceDateBetween(true,startOfMonth,endOfMonth);
        List<Paie> listPaie = readAllPaie();
        List<TeachersPresence> newListPresence = new ArrayList<>();
        for(TeachersPresence presence: list){
            boolean hasPaie = false;
            for (Paie paie: listPaie){
                if(paie.getIdPresenceTeachers().getIdSeance().equals(presence.getIdSeance())){
//                    LocalTime hourValid = LocalTime.now().withHour(23);
                   hasPaie = true;
                   break;
                }
            }
            if (!hasPaie) {
                newListPresence.add(presence);
            }
        }
        if (newListPresence.isEmpty()){
            return new ArrayList<>();
        }
        return newListPresence;
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
//    ---------------------------------mehod add paie-----------------------------------
    public Paie addPaie(Paie paie){

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
        return paie_repositorie.save(paie);
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
