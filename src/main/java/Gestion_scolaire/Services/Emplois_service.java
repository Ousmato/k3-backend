package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.EmploisDTO;
import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Journee;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Journee_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class Emplois_service {

    @Autowired
    private Emplois_repositorie emplois_repositorie;
    @Autowired
    private Validator validator;

    @Autowired
    private Journee_repositorie journee_repositorie;
    
    public Object add(Emplois emplois) {
        Set<ConstraintViolation<Emplois>> violation = validator.validate(emplois);
        if (!violation.isEmpty()) {
            throw new ConstraintViolationException(violation);
        }
        System.out.println("---------------" + emplois);

        List<Emplois> emplois_de_la_classe = emplois_repositorie.findEmploisActifByIdClass(LocalDate.now(), emplois.getIdClasse().getId());

//        // Vérification des dates par rapport au semestre
        LocalDate dateDebut = emplois.getDateDebut();
        LocalDate dateFin = emplois.getDateFin();
        LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
        LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();

        System.out.println("dateDebutSemestre: " + dateDebutSemestre);
        System.out.println("dateFinSemestre: " + dateFinSemestre);

        System.out.println("dateDebut: " + dateDebut);
        System.out.println("dateFin: " + dateFin);
//        if(dateDebut.isBefore(LocalDate.now())){
//            throw new NoteFundException("invalide la date du début ne peut pas etre inferieur a aujourd'hui");
//        }
        if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
            throw new NoteFundException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
        }

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
        emplois_repositorie.save(emplois);
        return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
    }

    //    -----------------------------------------mehode pour modifier-------------------------
    public  Object update(Emplois emplois) {
        Emplois emploisExist = emplois_repositorie.findById(emplois.getId());
        if (emploisExist != null) {

            List<Journee> journees = journee_repositorie.findByIdEmploisId(emploisExist.getId());
            if(!journees.isEmpty()){
                if(emploisExist.getDateDebut().isBefore(LocalDate.now())){
                    throw new NoteFundException("Impossible de modifier l'emploi du temps, des séances sont déjà programmées pour cet emploi.");

                }
            }
            LocalDate dateDebut = emplois.getDateDebut();
            LocalDate dateFin = emplois.getDateFin();
            LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
            LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();

            if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
                throw new NoteFundException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
            }


            // Vérification finale sur les dates de début et de fin
            if (dateFin.isBefore(dateDebut)) {
                throw new NoteFundException("La date de fin ne peut pas être avant la date de début.");
            }

            emploisExist.setDateDebut(emplois.getDateDebut());
            emploisExist.setDateFin(emplois.getDateFin());
            emploisExist.setIdSemestre(emplois.getIdSemestre());
            emploisExist.setIdModule(emplois.getIdModule());

            emplois_repositorie.save(emploisExist);
            return DTO_response_string.fromMessage("Modification effectué avec succès", 200);
        }
        throw new NoteFundException("emplois n'existe pas");
    }

    //get all emplois by id class and id semestre and id module
    public List<Emplois> listEmploisByIdClassAndIdSemestreAndModule(long idClass, long idSemestre, long idModule) {
        List<Emplois> oldEmplois = emplois_repositorie.findAllOldEmploisOfClassBySemestre(idClass, idSemestre,LocalDate.now().minusWeeks(1));
        if (oldEmplois.isEmpty()) {
            return new ArrayList<>();
        }
        return oldEmplois;
    }

    //    --------------------------method get all emplois of teacher-------------------------
//    public List<Emplois> findAllEmploisByTeacher(long idteacher) {
//        List<Seances> seancesList = seance_repositorie.findAll_ByIdTeacher( idteacher, LocalDate.now());
//        List<Emplois> emploisList = new ArrayList<>();
//        for (Seances seance : seancesList) {
//            Emplois emplois = seance.getIdEmplois();
//            if (!emploisList.contains(emplois)) {
//                emploisList.add(emplois);
//            }
//        }
//        return emploisList;
//    }
//    --------------------------------method get by id emplois--------------------
    public Emplois getById(long id){
        Emplois emplois = emplois_repositorie.findById(id);
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
        Emplois emploiExist =emplois_repositorie.findById(idEmplois);
        if(emploiExist !=null){
               emploiExist.setValid(!emploiExist.isValid());
                emplois_repositorie.save(emploiExist);
                return  true;

        }
        return  false;
    }
//    -------------------methode de verification if emplois is valid or no
    public boolean isValid(long idEmplois) {
        Emplois emploiExist = emplois_repositorie.findById(idEmplois);

        if (emploiExist != null) {
            return emploiExist.isValid();
        }
        return false;
    }
//--------------------------------get all emplois
    public List<Emplois> listEmploisActifs(){
        List<Emplois>  list = emplois_repositorie.findAllEmploisActif(LocalDate.now());
        if (list.isEmpty()){
            return  new ArrayList<>();
        }
        return list;
    }
//    ------------------------------get emplois active with seances
//    public List<EmploisDTO> listEmploisActifs_with_seances(){
//        List<Emplois>  list = emplois_repositorie.findAllEmploisActif(LocalDate.now());
//
//        if (list.isEmpty()){
//            return  new ArrayList<>();
//        }
//        List<EmploisDTO> dtoList = new ArrayList<>();
//
//        for (Emplois emploi : list){
//            List<Seances> seancesList = seance_repositorie.findByIdEmploisId(emploi.getId());
//
//
//            EmploisDTO emploisDTO = EmploisDTO.toEmploisDTO(emploi);
//            emploisDTO.setIdSemestre(emploi.getIdSemestre());
//            emploisDTO.setSeances(seancesList);
//            dtoList.add(emploisDTO);
//
//        }
//        return dtoList;
//    }
}
