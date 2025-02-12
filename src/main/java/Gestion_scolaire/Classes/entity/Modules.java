package Gestion_scolaire.Classes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Modules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    private String nomModule;

    private String description;


    @NotNull(message = "Le coefficient ne doit pas être nul.")
//    @Max(value = 8, message = "Le coefficient ne doit pas être supérieur à 8.")
    private  int coefficient;

    @Max(value = 60, message = "Le volume horaire des TD ne dois pas dépassé 60 heures")
    private int volHTD = 0;

    @Max(value = 120, message = "Le volume horaire CM ne dois pas dépassé 120 heures")
    private int volHCM = 0;

    private int volTPE = 0;
    private int volTP = 0;

    @ManyToOne
    private UE idUe;

    private boolean active = true;
}
