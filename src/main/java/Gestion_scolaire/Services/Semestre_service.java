package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Repositories.Semestre_repositorie;
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
}
