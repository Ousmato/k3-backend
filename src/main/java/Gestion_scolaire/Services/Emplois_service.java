package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Seance_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Emplois_service {

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private Seance_repositorie seance_repositorie;

    public Emplois add(Emplois emplois){
        Emplois emplois_de_la_classe = emplois_repositorie.findByIdClasseModule(emplois.getIdClasseModule());
        Emplois empliExist = emplois_repositorie.findByIdClasseModuleAndDateDebutAndDateFin(emplois.getIdClasseModule(), emplois.getDateDebut(), emplois.getDateFin());

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
            emplois.setIdClasseModule(emplois.getIdClasseModule());

            return emplois_repositorie.save(emploisExist);
        }
        throw new RuntimeException("emplois n'existe pas");
    }

//---------------------------method get current emplois by idClasse-------------------------
    public Emplois getByIdClasse(long idClasse){
        Emplois emplois = emplois_repositorie.findEmploisActifByIdClass(LocalDate.now(),idClasse);
        if (emplois != null){
            return emplois;
        }
        throw new RuntimeException("Auccun emplois pour le moment");
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
}
