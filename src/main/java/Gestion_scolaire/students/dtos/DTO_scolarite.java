package Gestion_scolaire.students.dtos;

import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.enumClass.Type_status;
import lombok.Data;

@Data
public class DTO_scolarite {
    private double scolarite;
    private long id;
    private double payer;
    private double reliquat;

    public static DTO_scolarite update_scolarite_student(Inscription studens) {
        DTO_scolarite dto = new DTO_scolarite();
//        dto.setScolarite(studens.getScolarite());
        dto.setId(studens.getId());

        return dto;
    }

    public static DTO_scolarite getScolarite_student(Inscription inscrit) {
        DTO_scolarite dto = new DTO_scolarite();
        if(inscrit.getIdEtudiant().getStatus().equals(Type_status.REGULIER)){
            dto.setScolarite(6000);
        } else if(inscrit.getIdEtudiant().getStatus().equals(Type_status.FORMATION_CONTINUE)){
            dto.setScolarite(300000);
        }else if(inscrit.getIdEtudiant().getStatus().equals(Type_status.PROFESSIONNEL_COLLECTIVITE) || inscrit.getIdEtudiant().getStatus().equals(Type_status.PROFESSIONNEL_ETAT)){
            dto.setScolarite(150000);
        }else if(inscrit.getIdEtudiant().getStatus().equals(Type_status.PROFESSIONNEL_PRIVEE)){
            dto.setScolarite(200000);
        }

        dto.setId(inscrit.getId());
//        dto.setPayer(inscrit.getScolarite());

//        double reliquat = dto.getScolarite() - inscrit.getScolarite();
//        dto.setReliquat(reliquat);
        return dto;

    }
}
