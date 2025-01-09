package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import lombok.Data;

import java.util.List;

@Data
public class AddModuleDTO {
    private UE idUe;
    private List<Modules> modules;
}
