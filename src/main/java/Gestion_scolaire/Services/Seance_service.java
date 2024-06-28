package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Notification_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class Seance_service {
    @Autowired
    private Seance_repositorie seance_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private Notification_repositorie notification_repositorie;






//    ----------------------------------------------------appels tout les seances par id---------------
    public List<Seances> readByIdEmplois(long idEmplois){

        Emplois emploisExist = emplois_repositorie.findById(idEmplois);
//        System.out.println(emploisExist+ "emplois exist");
        List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploisExist.getId());

//        System.out.println(seancesList+ "seance exist");
        if (!seancesList.isEmpty()) {
            if (emploisExist.getDateFin().isAfter(LocalDate.now())) {
                seancesList.sort(Comparator.comparing(Seances::getHeureDebut));
                return seancesList;
            }
            return new ArrayList<>();
        }
        return seancesList;
    }
//--------------------------------------------method call all teacher in seances actif-------------
//public List<TeacherSeancesObject> allTeacher() {
//    // Récupérer tous les emplois actifs pour la date actuelle
//    List<Emplois> emploisList = emplois_repositorie.findAllEmploisActif(LocalDate.now());
//
//    // Utiliser un map pour éviter les doublons et regrouper les séances par enseignant
//    Map<Long, TeacherSeancesObject> teacherMap = new HashMap<>();
//
//    // Itérer sur chaque emploi actif
//    for (Emplois emploi : emploisList) {
//        // Récupérer toutes les séances pour cet emploi
//        List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploi.getId());
//
//        // Itérer sur chaque séance pour cet emploi
//        for (Seances seance : seancesList) {
//            Teachers teacher = seance.getIdTeacher();
//            Long teacherId = teacher.getIdEnseignant();
//
//            // Si l'enseignant est déjà dans le map, ajouter la séance à sa liste
//            // Sinon, créer une nouvelle entrée pour cet enseignant
//            TeacherSeancesObject teacherSeancesObject = teacherMap.getOrDefault(teacherId, new TeacherSeancesObject());
//            teacherSeancesObject.setTeacher(teacher);
//            teacherSeancesObject.setClasse(seance.getIdEmplois().getIdClasse());
//            teacherSeancesObject.addSeance(seance);
//
//            // Ajouter ou mettre à jour l'entrée dans le map
//            teacherMap.put(teacherId, teacherSeancesObject);
//        }
//    }
//
//    // Convertir le map en une liste
//    return new ArrayList<>(teacherMap.values());
//}



    //    -----------------------------------------methode pour calculer le nbre d'heure d'un enseignant par seance-------
    public  int nbreHeure(Teachers idTeacher){
       return seance_repositorie.findTotalHoursByTeacher(idTeacher.getIdEnseignant());
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
    public Seances createSeance(Seances seances) {
        // Vérifie la date par rapport à l'emploi du temps
        Emplois emploisExist = emplois_repositorie.findById(seances.getIdEmplois().getId());
        LocalDate dateSeance = seances.getDate();

        if (dateSeance.isBefore(emploisExist.getDateDebut()) || dateSeance.isAfter(emploisExist.getDateFin())) {
            throw new RuntimeException("La date de la séance doit être comprise entre la date de début et de fin de l'emploi du temps");
        }

        // Vérifie l'existence d'une séance similaire

        Seances seances1 = seance_repositorie.getByDateAndIdModuleId(
                seances.getDate(), seances.getIdModule().getId());
        if(seances1 != null){
           if(seances.getHeureDebut().isBefore(seances1.getHeureFin())){
               throw new RuntimeException("Attention, une séance similaire existe déjà l'heure de debut dois etre superieur au precedent" );

           };
        }
        // Enregistre la séance
        return  seance_repositorie.save(seances);

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
//-----------------------------------------------------------------get teacherDTO by id
    public TeacherSeancesDTO getDetail(long idTeacher) {
        List<TeacherSeancesDTO> detail = all_teacher();
        for (TeacherSeancesDTO tsDto : detail) {
            if (tsDto.getTeacher().getIdEnseignant() == idTeacher) {
                return tsDto;
            }
        }
        return null; // Retourner null si aucun enseignant ne correspond à l'id fourni
    }
}



