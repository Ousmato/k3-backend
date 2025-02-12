package Gestion_scolaire.students.dtos;

import Gestion_scolaire.students.enumClass.Type_status;
import lombok.Data;

@Data
public class StatusInscritDTO {
    private String status;
    private int numbreInscit;
    private int numberNotIncrit;
    private int dette;
}
