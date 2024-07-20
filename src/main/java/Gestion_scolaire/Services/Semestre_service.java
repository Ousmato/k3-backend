package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Semestre_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Semestre_service {
    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @PostConstruct
    public void init(){
        String[] nomSemestre = {"SEMESTRE 1", "SEMESTRE 2", "SEMESTRE 3", "SEMESTRE 4", "SEMESTRE 5", "SEMESTRE 6"};
        LocalDate curentDate = LocalDate.now();
        List<Semestres> listSemestre = semestre_repositorie.findAll();
        if (listSemestre.isEmpty()) {
        for (int i = 0; i< nomSemestre.length; i++){

            Semestres sem = new Semestres();

                sem.setNomSemetre(nomSemestre[i]);
                LocalDate dateDebut = curentDate.plusMonths(i * 6);
                LocalDate dateFin = dateDebut.plusMonths(6);

                sem.setDateDebut(dateDebut);
                sem.setDatFin(dateFin);
                semestre_repositorie.save(sem);
            }
        }

    }
//    --------------------------------------get all semestres---------------------------
    public List<Semestres> getAll(){
        List<Semestres> semestresList = semestre_repositorie.findAll();
        if (semestresList.isEmpty()){
            return new ArrayList<>();
        }
        return semestresList;
    }
//    -----------------------------------------get current semestre-----------------------
    public Semestres currentSemestre(){
        return semestre_repositorie.getCurrentSemestre(LocalDate.now());
    }
//    ---------------------------------------method update semestre
    public Object update(Semestres semestre){
        Semestres smExist = semestre_repositorie.findById(semestre.getId());
        Semestres currentSemestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Emplois> emploisExist = emplois_repositorie.getByIdSemestreId(semestre.getId());
        if (!emploisExist.isEmpty()){
            throw new NoteFundException(" Impossible de modifier, Il existe deja des emplois pour ce semestre");
        }

        if (semestre.getDatFin().isBefore(semestre.getDateDebut())){
            throw new NoteFundException("La date debut ne pas etre inferieur a la date de fin");
        }

        if (semestre.getDatFin().isBefore(currentSemestre.getDateDebut())){
            throw  new NoteFundException("La date du %sne doit pas etre inferieur a la date de fin du semestre acctuel".formatted(semestre.getNomSemetre()));
        }

        smExist.setDateDebut(semestre.getDateDebut());
        smExist.setDatFin(semestre.getDatFin());
       semestre_repositorie.save(smExist);

        return DTO_response_string.fromMessage("Modification effectuer avec succé", 200);
    }
}
