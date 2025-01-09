package Gestion_scolaire.Administrators.dtos;

import Gestion_scolaire.Administrators.entity.Admin;
import lombok.Data;

import java.util.List;

@Data
public class AdminPostesDTO {
    private Admin admin;
    private List<String> roleNames;
}
