package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class AddNoteDTO {
    private long idNote;
    private long idModule;
    private double noteExam;
    private double noteClasse;
    private AddUeDTO addUeDto;
}
