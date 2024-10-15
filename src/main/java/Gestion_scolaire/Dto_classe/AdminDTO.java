package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class AdminDTO {
   private long idAdministra;
   private String nom;
   private String prenom;
   private String email;
   private int telephone;
}
