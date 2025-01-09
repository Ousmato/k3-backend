package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import lombok.Data;

@Data
public class NiveauDTO {
    private  String nom;

    public  static NiveauDTO toNiveauDTO(Niveau niveau){
        NiveauDTO dto = new NiveauDTO();
        dto.setNom(niveau.getNom());
        return dto;
    }
}
