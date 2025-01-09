package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Classes.dtos.UeDTO;
import lombok.Data;

@Data
public class GetNoteDTO {
    private long idNote;
    private UeDTO ues;
    private double moyenUe;
    private int session;
    private int coefUe;
    private double moyenGeneral;
}
