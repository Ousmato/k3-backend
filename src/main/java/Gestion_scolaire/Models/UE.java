package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

    @NotBlank(message = "Le champ ne doit pas être nul ou vide.\n")
    @Size(min = 3, max = 20, message = "Le champ nom doit contenir entre 3 et 20 caractères.\n")
    private String nomUE;


    @NotNull
    @ManyToOne
    private Admin idAdmin;

}
