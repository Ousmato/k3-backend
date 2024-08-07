package Gestion_scolaire.Models;

import Gestion_scolaire.EnumClasse.Admin_role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Admin extends UsersAbstract{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long idAdministra;

    @NotNull
    private Admin_role role;

}
