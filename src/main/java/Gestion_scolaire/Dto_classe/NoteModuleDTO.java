package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class NoteModuleDTO {
    private  long idModule;
    private  long idUe;
    private String nomModule;
    private double noteModule;
    private int coefficient;
}