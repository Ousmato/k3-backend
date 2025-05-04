package Gestion_scolaire.Services;

import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Emplois.repositorie.Emplois_repositorie;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Semestre_service {
    private final Shared_repositories shared_repositories;

    public void add_semestre() throws NoteFundException {
        String[] nomsSemestres = {
                "S1", "S2",
                "S3", "S4",
                "S5", "S6"
        };
        for (String nomsSemestre : nomsSemestres) {
            Semestres semestre = new Semestres();
            semestre.setNomSemetre(nomsSemestre);
            shared_repositories.getSemestre_repositorie().save(semestre);

        }
    }

    public Semestres getSemestreByNom(String nom){
        return shared_repositories.getSemestre_repositorie().findByNomSemetre(nom);
    }


    //    --------------------------------------get all semestres---------------------------
    public List<Semestres> getAll(){
        List<Semestres> semestresList = shared_repositories.getSemestre_repositorie().findAll();
        if (semestresList.isEmpty()){
            return new ArrayList<>();
        }
        return semestresList;
    }

    //-----------get semestre by idClasse
    public Semestres semestre_classe_id(long id){
        Emplois em = shared_repositories.getEmplois_repositorie().findByIdClasseId(id);
        if (em == null){
            return null;
        }
        return em.getIdSemestre();
    }
//    --------------------------------
    public List<Semestres> getCurrenctSemestresByIdNivFil(long idClasse){
       List<Semestres> semestresList = shared_repositories.getSemestre_repositorie().getByIdClasse(idClasse);
       if (semestresList.isEmpty()){
           return new ArrayList<>();
       }
       return semestresList;
    }

    public Semestres getSemestre(long id) {
        Semestres semestre = shared_repositories.getSemestre_repositorie().findById(id);
        if (semestre == null) {
            throw new RuntimeException("le semestre n'existe pas");
        }
        return semestre;
    }
    //--------------------------------
 
}
