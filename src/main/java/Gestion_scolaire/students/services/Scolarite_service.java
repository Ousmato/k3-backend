package Gestion_scolaire.students.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.DTO_scolarite;
import Gestion_scolaire.students.entity.Paiement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scolarite_service {

    private final Shared_repositories shared_repositories;


    private final Shared_methods_service shared_methods_service;

    public List<Paiement> getListPaiementByIdInscrit(long idInscrit) {
        List<Paiement> paiementList = shared_repositories.getPaiement_repositorie().findByIdInscriptionId(idInscrit);
        if (paiementList.isEmpty()) {
            return  new ArrayList<>();
        }
        return paiementList;
    }

    public Object updatePaiement(long idPaiement, DTO_scolarite dto, long idAdmin) {
        Paiement paiement = shared_repositories.getPaiement_repositorie().findById(idPaiement);
        if (paiement == null) {
            throw new NoteFundException("Ce paiement est introuvable");
        }
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        if (administrationUsers == null) {
            throw new NoteFundException("Cet admin est introuvable");
        }
        Double seuil = shared_methods_service.getSeuilScolarite(dto.getType());
        boolean isValid = shared_methods_service.validateScolariteByStatus(dto.getPayer(), dto.getType(), seuil);
        if (!isValid) {
            throw new NoteFundException("Montant invalide, le seuil est fixé à " + seuil +" FCFA");
        }
        paiement.setUpdateDate(LocalDate.now());
        paiement.setMontant(dto.getPayer());
        paiement.setIdAdministrationUsers(administrationUsers);
        shared_repositories.getPaiement_repositorie().save(paiement);
        return DTO_response_string.updateMessage();

    }
}
