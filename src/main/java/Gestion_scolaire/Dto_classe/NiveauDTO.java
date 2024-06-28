package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Niveau;
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
