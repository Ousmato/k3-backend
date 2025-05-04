package Gestion_scolaire.Administrators.dtos;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import lombok.Data;

import java.util.List;

@Data
public class AdminPostesDTO {
    private AdministrationUsers admin;
    private List<String> roleNames;
}
