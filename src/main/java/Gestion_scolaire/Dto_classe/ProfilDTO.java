package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Filiere;
import Gestion_scolaire.Models.Teachers;
import lombok.Data;

import java.util.List;

@Data
public class ProfilDTO {
    private long id;
    private Admin idAdmin;
    private Teachers teachers;
    private List<Filiere> filieres;
}
