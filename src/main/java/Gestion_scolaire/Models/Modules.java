package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Modules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private String nomModule;


    @NotNull(message = "Le coefficient ne doit pas être nul.")
    @Max(value = 6, message = "Le coefficient ne doit pas être supérieur à 6.")
    private  int coefficient;

    @ManyToOne
    private UE idUe;
}
