package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.InfoSchool;
import Gestion_scolaire.Repositories.InfoSchool_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class InfoScool_service {

    @Autowired
    private InfoSchool_repositorie infoSchool_repositorie;

    @Autowired
    private fileManagers fileManagers;

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
            inf.setDebutAnnee(LocalDate.now());
            inf.setUrlPhoto("logo-uie-removebg-preview.png");
            inf.setLocalite("BADALABOUGOU -Coline du savoir");
            inf.setNomSchool("UNIVERSITE INTERNATIONNALE D'EXCELENCE");
            inf.setFinAnnee(LocalDate.now().plusYears(1));

            infoSchool_repositorie.save(inf);
        }
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
        infExist.setDebutAnnee(school.getDebutAnnee());
        infExist.setFinAnnee(school.getFinAnnee());
        infExist.setEmail(school.getEmail());
        infExist.setTelephone(school.getTelephone());

        // Enregistrer l'entité mise à jour dans le repository
         infoSchool_repositorie.save(infExist);
         return DTO_response_string.fromMessage("Mise à effectuée avec succès",200);
    }

    // Si l'entité n'existe pas, lever une exception
    throw new NoteFundException("School with id " + school.getId() + " does not exist");
}

}
