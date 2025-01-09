package Gestion_scolaire.Dto_classe;

import lombok.Data;

import java.util.List;

@Data
public class NoteDTO {

    private long idUe;
    private String nomUE;
    private List<NoteModuleDTO> modules;
    private double noteUE;
    private int coefficientUe;
    private int session;
    private double noteUeCoefficient;
    private double moyenne;
}
