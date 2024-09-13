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
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class Semestre_service {
    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    public Object add_semestre(Semestres semestre) throws NoteFundException {
        // Vérifie si un semestre avec le même nom et la même date de fin existe déjà
        Semestres s = semestre_repositorie.findByNomSemetreAndIdAnneeScolaireId(semestre.getNomSemetre(), semestre.getIdAnneeScolaire().getId());
        if (s != null) {
            throw new NoteFundException("Impossible, le semestre existe déjà pour cette année ");
        }
        Period period = Period.between(semestre.getDateDebut(), semestre.getDatFin());
        if (period.toTotalMonths() < 4 || period.toTotalMonths() > 6 ) {
            throw new NoteFundException("La période pour une semestre ne dois pas etre supérieur a 6 mois ou inférieur a 4mois ");

        }
        LocalDate anneeDebutDate = semestre.getIdAnneeScolaire().getDebutAnnee();
        LocalDate anneeFinDate = semestre.getIdAnneeScolaire().getFinAnnee();
        if ((semestre.getDatFin().isAfter(anneeDebutDate.minusDays(1)) && semestre.getDateDebut().isBefore(anneeFinDate.plusDays(1)))) {
            // Sauvegarde le nouveau semestre
            semestre_repositorie.save(semestre);

            return DTO_response_string.fromMessage("Ajout effectué avec succès", 200);
        }
        throw new NoteFundException("Le semestre dois etre dans l'intervalle de la promotion " + anneeFinDate.getYear());

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
//        Semestres currentSemestre = semestre_repositorie.getCurrentSemestre(LocalDate.now());
        List<Emplois> emploisExist = emplois_repositorie.getByIdSemestreId(semestre.getId());
        if (!emploisExist.isEmpty()){
            throw new NoteFundException("Impossible de modifier, Il existe deja des emplois pour ce semestre");
        }

        if (semestre.getDatFin().isBefore(semestre.getDateDebut())){
            throw new NoteFundException("La date debut ne pas etre inferieur a la date de fin");
        }

        Period period = Period.between(semestre.getDateDebut(), semestre.getDatFin());
        if (period.toTotalMonths() < 4 || period.toTotalMonths() > 6 ) {
            throw new NoteFundException("La période pour une semestre ne dois pas etre supérieur a 6 mois ou inférieur a 4mois ");

        }

        LocalDate anneeDebutDate = semestre.getIdAnneeScolaire().getDebutAnnee();
        LocalDate anneeFinDate = semestre.getIdAnneeScolaire().getFinAnnee();
        if ((semestre.getDatFin().isAfter(anneeDebutDate.minusDays(1)) && semestre.getDateDebut().isBefore(anneeFinDate.plusDays(1)))) {

            smExist.setNomSemetre(semestre.getNomSemetre());
            smExist.setDateDebut(semestre.getDateDebut());
            smExist.setDatFin(semestre.getDatFin());
            semestre_repositorie.save(smExist);

            return DTO_response_string.fromMessage("Mise à jour effectuée avec succès", 200);
        }
        throw new NoteFundException("Le semestre dois etre dans l'intervalle de la promotion " + anneeFinDate.getYear());
        
    }

//    --------------------------------------------get semestre by idClasse
    public Semestres semestre_classe_id(int id){
        Emplois em = emplois_repositorie.findByIdClasseId(id);
        if (em == null){
            return null;
        }
        return em.getIdSemestre();
    }
}
