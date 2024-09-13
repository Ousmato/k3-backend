package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import lombok.Data;

import java.util.List;

@Data
public class AddModuleDTO {
    private UE idUe;
    private List<Modules> modules;
}
