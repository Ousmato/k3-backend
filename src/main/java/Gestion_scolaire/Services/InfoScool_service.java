package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Repositories.AnneeScolaire_repositorie;
import Gestion_scolaire.Repositories.InfoSchool_repositorie;
import Gestion_scolaire.Repositories.Students_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Slf4j
@Service
public class InfoScool_service {

    @Autowired
    private InfoSchool_repositorie infoSchool_repositorie;

    @Autowired
    private fileManagers fileManagers;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private AnneeScolaire_repositorie anneeScolaire_repositorie;

    @PostConstruct
    public void init() {
        String email = "ousmato98@gmail.com";
        int telephone = 73855168;


        InfoSchool infoExist = infoSchool_repositorie.findByEmailAndTelephone(email,telephone);
        if (infoExist == null) {
            InfoSchool inf = new InfoSchool();
            inf.setEmail(email);
            System.out.println("sauis la-----------------");
            inf.setTelephone(telephone);
//            inf.setAnneeScolaire(annee);
            inf.setUrlPhoto("logo-uie-removebg-preview.png");
            inf.setLocalite("BADALABOUGOU -Coline du savoir");
            inf.setNomSchool("UNIVERSITE INTERNATIONNALE D'EXCELENCE");

            infoSchool_repositorie.save(inf);
        }

        log.info("InfoSchool record already exists with email: {} and telephone: {}", email, telephone);
    }
//    ---------------------------------------get school
    public InfoSchool getInfo(){
        List<InfoSchool> list = infoSchool_repositorie.findAll();
      return   list.getFirst();
    }

//    -----------------------------method update
    public Object update(InfoSchool school, MultipartFile file) throws IOException {
        InfoSchool infExist = infoSchool_repositorie.findById(school.getId());

        // Vérifie si l'entité existe
        if (infExist != null) {
            if (file != null && !file.isEmpty()) {
                // Si un nouveau fichier est téléchargé, mettre à jour l'URL de la photo
                String urlPhoto = fileManagers.updateFile(file, infExist.getUrlPhoto());
                infExist.setUrlPhoto(urlPhoto);
            }

            infExist.setNomSchool(school.getNomSchool());
            infExist.setLocalite(school.getLocalite());
            infExist.setAnneeScolaire(school.getAnneeScolaire());
            infExist.setEmail(school.getEmail());
            infExist.setTelephone(school.getTelephone());

            // Enregistrer l'entité mise à jour dans le repository
             infoSchool_repositorie.save(infExist);
             return DTO_response_string.fromMessage("Mise à effectuée avec succès",200);
        }

        // Si l'entité n'existe pas, lever une exception
        throw new NoteFundException("School with id " + school.getId() + " does not exist");
    }
//-----------------------------------------
    public Object add_anneeScolaire(AnneeScolaire anneeScolaire) {
        AnneeScolaire anneeExist = anneeScolaire_repositorie.findByFinAnneeBetween(anneeScolaire.getDebutAnnee(), anneeScolaire.getFinAnnee());
        if(anneeExist != null){
            throw new NoteFundException("Cette année scolaire existe déjà");

        }
        if(anneeScolaire.getFinAnnee().isBefore(anneeScolaire.getDebutAnnee()) || anneeScolaire.getDebutAnnee().isAfter(anneeScolaire.getFinAnnee())){
            throw new NoteFundException("Les dates ne sont pas conforme");
        }
        List<AnneeScolaire> list = anneeScolaire_repositorie.findAll();
        for (AnneeScolaire annee : list) {
            if (anneeScolaire.getDebutAnnee().isBefore(annee.getFinAnnee()) &&
                    anneeScolaire.getFinAnnee().isAfter(annee.getDebutAnnee())) {
                throw new NoteFundException("Les dates se chevauchent avec une autre année scolaire existante");
            }
        }

        anneeScolaire_repositorie.save(anneeScolaire);
        return DTO_response_string.fromMessage("Ajout effectuée avec succès",200);
    }

//    --------------------------get all annee scolaire
    public List<AnneeScolaire> readAll_anne(){
        List<AnneeScolaire> list = anneeScolaire_repositorie.findAll();

        list.sort(Comparator.comparing(AnneeScolaire::getFinAnnee));
        return list;
    }
//    ---------------------------------update annee scolaire
    public Object update_AnneeScolaire(AnneeScolaire anneeScolaire) {
        AnneeScolaire annee = anneeScolaire_repositorie.findById(anneeScolaire.getId());
        if (annee != null) {
            List<Studens> list = students_repositorie.getByIdAnneeScolaireId(annee.getId());
            if(!list.isEmpty()){
                throw new NoteFundException("Impossible de modifier l'année scolaire, des opérations sont associé ");
            }
            if(anneeScolaire.getFinAnnee().isBefore(anneeScolaire.getDebutAnnee()) || anneeScolaire.getDebutAnnee().isAfter(anneeScolaire.getFinAnnee())){
                throw new NoteFundException("Les dates ne sont pas conforme");
            }

            List<AnneeScolaire> listExist = anneeScolaire_repositorie.findAll();
            for (AnneeScolaire annee_find : listExist) {
                if (anneeScolaire.getDebutAnnee().isBefore(annee_find.getFinAnnee()) &&
                        anneeScolaire.getFinAnnee().isAfter(annee_find.getDebutAnnee())) {
                    throw new NoteFundException("Les dates se chevauchent avec une autre année scolaire existante");
                }
            }

            annee.setFinAnnee(anneeScolaire.getFinAnnee());
            annee.setDebutAnnee(anneeScolaire.getDebutAnnee());
            anneeScolaire_repositorie.save(annee);
            return DTO_response_string.fromMessage("Mise à jours effectué avec succès",200);
        }
        throw new NoteFundException("Année scolaire n'existe pas");
    }

//    --------------------------------------delete annee scolaire
    public Object delete_annee(long idAnnee){
        AnneeScolaire annee = anneeScolaire_repositorie.findById(idAnnee);
        if (annee != null) {
            List<Studens> list = students_repositorie.getByIdAnneeScolaireId(annee.getId());
            if (!list.isEmpty()) {
                throw new NoteFundException("Impossible de supprimer l'année scolaire, des opérations sont associé ");
            }
            anneeScolaire_repositorie.delete(annee);
            return DTO_response_string.fromMessage("Suppression effectué avec succès",200);
        }
        throw new NoteFundException("L'année scolaire n'existe pas");
    }
}
