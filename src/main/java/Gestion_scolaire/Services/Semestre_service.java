package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Semestre_service {
    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    @Autowired
    private Validator validator;

    @Autowired
    private Classe_repositorie classe_repositorie;

    @Autowired
    private Ue_repositorie ue_repositorie;


    public void add_semestre() throws NoteFundException {
        String[] nomsSemestres = {
                "S1", "S2",
                "S3", "S4",
                "S5", "S6"
        };
        for (String nomsSemestre : nomsSemestres) {
            Semestres semestre = new Semestres();
            semestre.setNomSemetre(nomsSemestre);
            semestre_repositorie.save(semestre);

        }
    }

    public Semestres getSemestreByNom(String nom){
        return semestre_repositorie.findByNomSemetre(nom);
    }


    //    --------------------------------------get all semestres---------------------------
    public List<Semestres> getAll(){
        List<Semestres> semestresList = semestre_repositorie.findAll();
        if (semestresList.isEmpty()){
            return new ArrayList<>();
        }
        return semestresList;
    }

    //-----------get semestre by idClasse
    public Semestres semestre_classe_id(long id){
        Emplois em = emplois_repositorie.findByIdClasseId(id);
        if (em == null){
            return null;
        }
        return em.getIdSemestre();
    }
//    --------------------------------
    public List<Semestres> getCurrenctSemestresByIdNivFil(long idClasse){
       List<Semestres> semestresList = semestre_repositorie.getByIdClasse(idClasse);
       if (semestresList.isEmpty()){
           return new ArrayList<>();
       }
       return semestresList;
    }

    public Semestres getSemestre(long id) {
        Semestres semestre = semestre_repositorie.findById(id);
        if (semestre == null) {
            throw new RuntimeException("le semestre n'existe pas");
        }
        return semestre;
    }
    //--------------------------------
 
}
