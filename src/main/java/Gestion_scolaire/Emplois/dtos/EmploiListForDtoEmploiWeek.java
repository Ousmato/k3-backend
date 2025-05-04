package Gestion_scolaire.Emplois.dtos;

import Gestion_scolaire.Emplois.entity.Emplois;
import Gestion_scolaire.Teachers.services.Commom_methods;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class EmploiListForDtoEmploiWeek {
    private  long idEmploi;
    private  long idClasse;
    private  long idSemestre;
    private String nomSemestre;
    private String nomNiveau;
    private String nomFiliere;
    private String nomTeacher;
    private String nomModule;


    public static EmploiListForDtoEmploiWeek toDto(Emplois emplois){
        EmploiListForDtoEmploiWeek dto = new EmploiListForDtoEmploiWeek();
        dto.setIdEmploi(emplois.getId());
        dto.setIdClasse(emplois.getIdClasse().getId());
        dto.setIdSemestre(emplois.getIdSemestre().getId());
        dto.setNomModule(emplois.getIdModule().getNomModule());
        dto.setNomSemestre(emplois.getIdSemestre().getNomSemetre());
        dto.setNomNiveau(emplois.getIdClasse().getIdFiliere().getIdNiveau().getNom());
        dto.setNomFiliere(emplois.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
        return dto;
    }
    
}
