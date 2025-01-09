package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Teachers.entity.Teachers;
import lombok.Data;

import java.util.List;

@Data
public class ProfilDTO {
    private long id;
    private Admin idAdmin;
    private Teachers teachers;
    private List<Filiere> filieres;
}
