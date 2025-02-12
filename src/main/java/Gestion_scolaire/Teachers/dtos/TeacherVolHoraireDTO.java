package Gestion_scolaire.Teachers.dtos;

import lombok.Data;

@Data
public class TeacherVolHoraireDTO {
    private String typeCours;  // "CM" ou "TD"
    private int volumeHoraire;
}
