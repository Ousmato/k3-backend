package Gestion_scolaire.Administrators.dtos;

import lombok.Data;

@Data
public class AdminDTO {
   private long idAdministra;
   private String nom;
   private String prenom;
   private String email;
   private String telephone;

}
