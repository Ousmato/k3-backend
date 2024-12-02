package Gestion_scolaire.Dto_classe;

import lombok.Data;

import java.util.List;

@Data
public class GetNoteDTO {
    private long idNote;
    private UeDTO ues;
    private double moyenUe;
    private int session;
    private int coefUe;
    private double moyenGeneral;
}
