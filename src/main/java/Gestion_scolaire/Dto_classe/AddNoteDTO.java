package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Models.Notes;
import lombok.Data;

@Data
public class AddNoteDTO {
    private  long idModule;
    private  long idInscription;
    private  long idSemestre;
    private Admin idAdmin;
    private  double examNote;
    private  double classeNote;


    public  static AddNoteDTO toDto(Notes note) {
        AddNoteDTO dto = new AddNoteDTO();
        dto.setClasseNote(note.getClasseNote());
        dto.setExamNote(note.getExamNote());
        dto.setIdModule(note.getIdModule().getId());
        dto.setIdInscription(note.getIdInscription().getId());
        dto.setIdSemestre(note.getIdSemestre().getId());
        dto.setIdAdmin(note.getIdAdmin());
        return dto;
    }

    public static Notes toEntity(AddNoteDTO dto) {
        System.out.println("Appel de AddNoteDTO.toEntity");
        Notes note = new Notes();
        note.setClasseNote(dto.getClasseNote());
        note.setExamNote(dto.getExamNote());
        note.setIdAdmin(dto.getIdAdmin());
        return note;
    }
}
