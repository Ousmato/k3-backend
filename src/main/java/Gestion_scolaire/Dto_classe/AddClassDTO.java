package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class AddClassDTO {
    private long idFiliere;
    private long idNiveau;
    private double scolarite;
    private long idAnnee;
}
