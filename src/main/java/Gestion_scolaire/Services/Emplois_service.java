package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
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

    @Autowired
    private Seance_repositorie seance_repositorie;

    public Emplois add(Emplois emplois){
        Emplois emplois_de_la_classe = emplois_repositorie.findByIdClasseId(emplois.getIdClasse().getId());
        Emplois empliExist = emplois_repositorie.findByIdClasseIdAndDateDebutAndDateFin(emplois.getIdClasse().getId(), emplois.getDateDebut(), emplois.getDateFin());

            LocalDate dateDebut = emplois.getDateDebut();
            LocalDate dateFin = emplois.getDateFin();
            LocalDate dateDebutSemestre = emplois.getIdSemestre().getDateDebut();
            LocalDate dateFinSemestre = emplois.getIdSemestre().getDatFin();

        // Vérification des dates
        if (dateDebut.isBefore(dateDebutSemestre) || dateFin.isAfter(dateFinSemestre)) {
            throw new RuntimeException("Les dates de l'emploi doivent être comprises entre les dates du semestre.");
        }

        if (emplois_de_la_classe != null) {
            if(dateDebut.isBefore(emplois_de_la_classe.getDateFin())){
                throw new RuntimeException("Il existe deja un emplois en cours veillez attendre cette date "+emplois_de_la_classe.getDateFin()+" ou modifier l'emplois du temps");

            }else {
                if (dateFin.isBefore(dateDebut)) {
                    throw new RuntimeException("Date de fin ne peut pas être avant la date de début");
                }
                return emplois_repositorie.save(emplois);
            }
        }else {
            if (dateFin.isBefore(dateDebut)) {
                throw new RuntimeException("Date de fin ne peut pas être avant la date de début");
            }
            return emplois_repositorie.save(emplois);
        }

    }
//    ------------------------------------------------------methode pour appeler les emplois----------
    public List<Emplois> readAll(){
        return emplois_repositorie.findAllEmploisActif(LocalDate.now());
    }
//    -----------------------------------------mehode pour modifier-------------------------
    public  Emplois update(Emplois emplois) {
        Emplois emploisExist = emplois_repositorie.findById(emplois.getId());
        if (emploisExist != null) {
            emploisExist.setDateDebut(emplois.getDateDebut());
            emploisExist.setDateFin(emplois.getDateFin());
            emplois.setIdSemestre(emplois.getIdSemestre());
            emplois.setIdClasse(emplois.getIdClasse());

            return emplois_repositorie.save(emploisExist);
        }
        throw new RuntimeException("emplois n'existe pas");
    }

//---------------------------method get current emplois by idClasse-------------------------
    public Emplois getByIdClasse(long idClasse){

        Emplois emplois = emplois_repositorie.getEmploisByDateFinAfterAndIdClasseId(LocalDate.now(),idClasse);
        if (emplois != null){
           return emplois;
        }
        throw new RuntimeException("Auccun emplois pour le moment");
    }
//    ---------------------------------------methode pour retourner un boolean if emplois exist pour une classe
    public Object hasEmplois(long idClasse) {
        // Vérifie si des emplois existent pour la classe spécifiée avec une date de fin après la date actuelle
        Emplois emplois = emplois_repositorie.findByIdClasseIdAndDateFinIsAfter(idClasse, LocalDate.now());

        if (emplois == null) {
            return false;
        }

        // Vérifie s'il y a des séances associées à cet emploi
        List<Seances> seances = seance_repositorie.findByIdEmploisId(emplois.getId());

        if (seances.isEmpty()) {
            return true;
        }

        // Sinon, retourne l'objet contenant l'emploi et les séances
        Map<String, Object> result = new HashMap<>();
        result.put("emplois", emplois);
        result.put("seances", seances);
        return result;
    }

    //    --------------------------method get all emplois of teacher-------------------------
    public List<Emplois> findAllEmploisByTeacher(long idteacher) {
        List<Seances> seancesList = seance_repositorie.findAll_ByIdTeacher( idteacher, LocalDate.now());
        List<Emplois> emploisList = new ArrayList<>();
        for (Seances seance : seancesList) {
            Emplois emplois = seance.getIdEmplois();
            if (!emploisList.contains(emplois)) {
                emploisList.add(emplois);
            }
        }
        return emploisList;
    }
//    --------------------------------method get by id emplois--------------------
    public Emplois getById(long id){
        Emplois emplois = emplois_repositorie.findById(id);
        if (emplois != null){
            return emplois;
        }
        throw new RuntimeException("Auccune correspondance");
    }
//    ----------------------methode pour verifier l'existence des seances sur l'emplois du temps
    public boolean hasSeances(long idEmplois){
        return seance_repositorie.existsByIdEmploisId(idEmplois);
    }
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

}
