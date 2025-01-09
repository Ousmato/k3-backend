package Gestion_scolaire.Niveaux_Filieres.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere_specialite;
import Gestion_scolaire.Niveaux_Filieres.repositories.FiliereSpecialiste_repositorie;
import Gestion_scolaire.Niveaux_Filieres.repositories.Filiere_repositorie;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.repositories.Specialite_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FiliereSpecialite_service {

    @Autowired
    private FiliereSpecialiste_repositorie filiereSpecialiste_repositorie;

    @Autowired
    private Filiere_repositorie filiere_repositorie;

    @Autowired
    private Specialite_repositorie specialite_repositorie;

    public void addFiliereSpecialiste(long idFiliere, long idSpecialite) {
        Specialites spl = specialite_repositorie.findById(idSpecialite);
        if (spl == null) {
            throw new NoteFundException("La spécialité est introuvable");

        }
        Filiere fl = filiere_repositorie.findById(idFiliere);
        if (fl == null) {
            throw new NoteFundException("La Filière est introuvable");

        }
        Filiere_specialite fspl = filiereSpecialiste_repositorie.getByIdFiliereIdAndIdSpecialiteId(idFiliere, idSpecialite);
        if (fspl != null) {
            throw new NoteFundException("Cette filière avec cette spécialité existe déjà");
        }
        Filiere_specialite filSplt = new Filiere_specialite();
        filSplt.setIdSpecialite(spl);
        filSplt.setIdFiliere(fl);
        filiereSpecialiste_repositorie.save(filSplt);
//        return DTO_response_string.addMessage();
    }
}
