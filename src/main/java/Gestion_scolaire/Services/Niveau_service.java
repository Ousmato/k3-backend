package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Niveau;
import Gestion_scolaire.Repositories.Niveau_repositorie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Niveau_service {

    @Autowired
    private Niveau_repositorie niveau_repositorie;

    @PostConstruct
    public void init() {
        String[] nomsNiveaux = {"LICENCE 1", "LICENCE 2", "LICENCE 3", "MASTER 1", "MASTER 2"};

        for (String nom : nomsNiveaux) {
            Niveau niveau = new Niveau();
            niveau.setNom(nom);
            niveau.setActive(true);

            Niveau niveauxExist = niveau_repositorie.findByNom(nom);

            if (niveauxExist == null) {
                niveau_repositorie.save(niveau);
            }
        }
    }
//    ===============================================methode get All niveau==============================
    public List<Niveau> readAll(){
        return niveau_repositorie.findAll();
    }
}
