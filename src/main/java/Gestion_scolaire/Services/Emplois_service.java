package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Dto_classe.EmploisDTO;
import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Emplois_service {

    @Autowired
    private Emplois_repositorie emplois_repositorie;


    public Object add(Emplois emplois) {
        List<Emplois> emplois_de_la_classe = emplois_repositorie.findEmploisActifByIdClass(LocalDate.now(), emplois.getIdClasse().getId());

//        // Vérification des dates par rapport au semestre
        LocalDate dateDebut = emplois.getDateDebut();
        LocalDate dateFin = emplois.getDateFin();
        LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
        LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();

//        System.out.println("dateDebutSemestre: " + dateDebutSemestre);
//        System.out.println("dateFinSemestre: " + dateFinSemestre);
//
//        System.out.println("dateDebut: " + dateDebut);
//        System.out.println("dateFin: " + dateFin);
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

            LocalDate dateDebut = emploisExist.getDateDebut();
            LocalDate dateFin = emploisExist.getDateFin();
            LocalDate dateDebutSemestre = emploisExist.getIdSemestre().getDateDebut();
            LocalDate dateFinSemestre = emploisExist.getIdSemestre().getDatFin();


            if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
                throw new NoteFundException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
            }


            // Vérification finale sur les dates de début et de fin
            if (dateFin.isBefore(dateDebut)) {
                throw new NoteFundException("La date de fin ne peut pas être avant la date de début.");
            }

            emploisExist.setDateDebut(emplois.getDateDebut());
            emploisExist.setDateFin(emplois.getDateFin());
            emplois.setIdSemestre(emplois.getIdSemestre());
            emplois.setIdClasse(emplois.getIdClasse());

            emplois_repositorie.save(emploisExist);
            return DTO_response_string.fromMessage("Modification effectué avec succès", 200);
        }
        throw new NoteFundException("emplois n'existe pas");
    }

//    ---------------------------------------methode pour retourner un boolean if emplois exist pour une classe
//    public Object hasEmplois(long idClasse) {
//        // Vérifie si des emplois existent pour la classe spécifiée avec une date de fin après la date actuelle
//        Emplois emplois = emplois_repositorie.findByIdClasseIdAndDateFinIsAfter(idClasse, LocalDate.now());
//
//        if (emplois == null) {
//            return false;
//        }
//
//        // Vérifie s'il y a des séances associées à cet emploi
//        List<Seances> seances = seance_repositorie.findByIdEmploisId(emplois.getId());
//
//        if (seances.isEmpty()) {
//            return true;
//        }
//        // Sinon, retourne l'objet contenant l'emploi et les séances
//        Map<String, Object> result = new HashMap<>();
//        result.put("emplois", emplois);
//        result.put("seances", seances);
//        return result;
//    }

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
