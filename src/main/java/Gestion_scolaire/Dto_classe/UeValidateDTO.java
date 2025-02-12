package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class UeValidateDTO {
    private String nomSemestre;
    private String observation;
    private double moyenSemestre;
    private double percentUeSemestre;
}
