package Gestion_scolaire.Services;

import Gestion_scolaire.Repositories.Inscription_repositorie;
import Gestion_scolaire.Repositories.Moyenne_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Moyenne_service {

    @Autowired
    private Moyenne_repositorie moyenne_repositorie;

    @Autowired
    private Inscription_repositorie inscription_repositorie;


}
