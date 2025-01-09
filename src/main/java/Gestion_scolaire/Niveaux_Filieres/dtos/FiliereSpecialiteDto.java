package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Teachers.entity.Specialites;
import lombok.Data;

import java.util.List;

@Data
public class FiliereSpecialiteDto {
    private Specialites specialite;
    private List<Filiere> filieres;
}
