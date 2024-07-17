package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

    @NotNull
    private String nomUE;




}
