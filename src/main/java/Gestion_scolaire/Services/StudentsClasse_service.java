package Gestion_scolaire.Services;

import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.Classe_repositorie;
import Gestion_scolaire.Repositories.Filiere_repositorie;
import Gestion_scolaire.Repositories.NiveauFiliere_repositorie;
import Gestion_scolaire.Repositories.Niveau_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsClasse_service {

    @Autowired
    private Filiere_repositorie filiere_repositorie;

    @Autowired
    private Classe_repositorie  classe_repositorie;

    @Autowired
    private NiveauFiliere_repositorie niveauFiliere_repositorie;


}
