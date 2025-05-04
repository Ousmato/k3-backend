package Gestion_scolaire.students.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.FiliereInscritDTO;
import Gestion_scolaire.students.dtos.FiliereStudentDTO;
import Gestion_scolaire.students.dtos.StatistiqueDTO;
import Gestion_scolaire.students.dtos.StatusInscritDTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.enumClass.Type_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StudentStatistique_service {
    @Autowired
    private Shared_repositories shared_repositories;

    @Autowired
    private Shared_methods_service shared_methods_service;

    public StatistiqueDTO getCurrentYearStatistique(long idAdmin){
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        LocalDate date = LocalDate.now();
        AnneeScolaire currentYear = shared_repositories.getAnneeScolaire_repositorie().findCurrentYear(date);

        return getStatistiqueByIdAnnee(currentYear.getId(), idAdmin);
    }

    public StatistiqueDTO getStatistiqueByIdAnnee(long idAnne, long idAdmin){
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        StatistiqueDTO statistiqueDTO = new StatistiqueDTO();
        Double totalInscrits = shared_repositories.getInscription_repositorie().countByIdClasseIdAnneeScolaireId(idAnne);

        Double dette = shared_repositories.getInscription_repositorie().cuntByIdAnnee(idAnne,true, false);
        dette = Objects.requireNonNullElse(dette, 0.0);
        double pourcentageDette = (dette / totalInscrits) * 100;
        double pourcentageArrondiDette = Math.round(pourcentageDette * 100.0) / 100.0;

        Double payer = shared_repositories.getInscription_repositorie().cuntByIdAnnee(idAnne,true, true);

        payer = Objects.requireNonNullElse(payer, 0.0);
        double pourcentagePaye = (payer / totalInscrits) * 100;
        double pourcentageArrondiPaye = Math.round(pourcentagePaye * 100.0) / 100.0;

        Double notPay = shared_repositories.getInscription_repositorie().cuntByIdAnnee(idAnne,false, false);
        notPay = Objects.requireNonNullElse(notPay, 0.0);
        double pourcentageNotPay = (notPay / totalInscrits) * 100;
        double pourcentageArrondiNotPay = Math.round(pourcentageNotPay * 100.0) / 100.0;

        statistiqueDTO.setDettePurcent(pourcentageArrondiDette);
        statistiqueDTO.setInscrit(payer.intValue());
        statistiqueDTO.setNonInscrit(notPay.intValue());
        statistiqueDTO.setPayePurcent(pourcentageArrondiPaye);
        statistiqueDTO.setNotPayePurcent(pourcentageArrondiNotPay);

        statistiqueDTO.setStatusInscrits(listInscritByStatus(idAnne));
        statistiqueDTO.setFiliereInscrit(listInscritByFiliere(idAnne));
        return statistiqueDTO;
    }

    // all inscrit paye and not paye by filiere
    public List<FiliereInscritDTO> listInscritByFiliere(long idAnnee){
        List<Filiere> filieres = shared_repositories.getFiliere_repositorie().getByIdAnnee(idAnnee);
        List<FiliereInscritDTO> dtoList = new ArrayList<>();
        for (Filiere filiere : filieres){
            FiliereInscritDTO dto = new FiliereInscritDTO();
            Integer numberInscrit = shared_repositories.getInscription_repositorie().cuntByFiliere(filiere.getId(), true, idAnnee);
            dto.setNumbreInscrit(Objects.requireNonNullElse(numberInscrit, 0));
            Integer numberNotInscrit = shared_repositories.getInscription_repositorie().cuntByFiliere(filiere.getId(), false, idAnnee);
            dto.setNumberNotInscrit(Objects.requireNonNullElse(numberNotInscrit, 0));
            dto.setNumberNotInscrit(numberNotInscrit);
            dto.setFiliereName(filiere.getNomFiliere());
            dto.setIdFiliere(filiere.getId());
            dtoList.add(dto);


        }
        return dtoList;
    }

    // all inscrit paye and paye by status
    public List<StatusInscritDTO> listInscritByStatus(long idAnnee) {
        List<Type_status> status = Arrays.stream(Type_status.values()).toList();
        List<StatusInscritDTO> dtoList = new ArrayList<>();
        for (Type_status statu : status){
            StatusInscritDTO dto = new StatusInscritDTO();
            Integer dette = shared_repositories.getInscription_repositorie().cuntByStatus(statu, true, false, idAnnee);
            dto.setDette(Objects.requireNonNullElse(dette, 0));
            Integer nonPayer = shared_repositories.getInscription_repositorie().cuntByStatus(statu, false, false, idAnnee);
            dto.setDette(Objects.requireNonNullElse(nonPayer, 0));

            Integer payer = shared_repositories.getInscription_repositorie().cuntByStatus(statu, true, true, idAnnee);
            dto.setDette(Objects.requireNonNullElse(payer, 0));
            dto.setStatus(statu.toString().replace("_", " "));
            dto.setNumberNotIncrit(nonPayer);
            dto.setNumbreInscit(payer);
            dto.setDette(dette);
            dtoList.add(dto);


        }
        return dtoList;
    }

    public List<FiliereStudentDTO> getStudentByFilieresAndPaye(long idFiliere, long idAdmin, long idAnnee, boolean isPaye) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        List<FiliereStudentDTO> dtoList = new ArrayList<>();
        List<Inscription> inscriptions = shared_repositories.getInscription_repositorie().getInscriptionByFilieresAndPaye(idFiliere,idAnnee,isPaye);
        return mapedFiliereStudentDTOS(dtoList, inscriptions);

    }

    //get student inscrit by status and paye and total paye
    public List<FiliereStudentDTO> getStudentByStatusAndPaye(String status, long idAdmin, long idAnnee, long isPaye) {
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().getByIdAndActive(idAdmin, true);
        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }

        List<FiliereStudentDTO> dtoList = new ArrayList<>();
        Type_status typeStatus = shared_methods_service.convertStringToTypeStatus(status);
        List<Inscription> inscriptions;
        if (isPaye == 1){
            inscriptions = shared_repositories.getInscription_repositorie().getInscriptionByStatusAndPaye(typeStatus, true, true,idAnnee);
        }else if(isPaye == 2){
            inscriptions = shared_repositories.getInscription_repositorie().getInscriptionByStatusAndPaye(typeStatus, false, false, idAnnee);
        }else {
            inscriptions = shared_repositories.getInscription_repositorie().getInscriptionByStatusAndPaye(typeStatus, true, false, idAnnee);
        }
        return mapedFiliereStudentDTOS(dtoList, inscriptions);

    }

    // mapped inscription to dto
    private List<FiliereStudentDTO> mapedFiliereStudentDTOS(List<FiliereStudentDTO> dtoList, List<Inscription> inscriptions) {
        for (Inscription inscription : inscriptions){
            FiliereStudentDTO dto = new FiliereStudentDTO();
            dto.setIdInscrit(inscription.getId());
            dto.setNom(inscription.getIdEtudiant().getNom());
            dto.setPrenom(inscription.getIdEtudiant().getPrenom());
            dto.setSexe(inscription.getIdEtudiant().getSexe());
            dto.setStatus(inscription.getIdEtudiant().getStatus().toString().replace("_", " "));
            String filiereName = shared_methods_service.abrevigateName(inscription.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
            String niveauName = shared_methods_service.abregNiveauName(inscription.getIdClasse().getIdFiliere().getIdNiveau().getNom());
            dto.setClasse(niveauName +"-" + filiereName);
            dto.setLieuNaissance(inscription.getIdEtudiant().getLieuNaissance());
            dto.setDateNaissance(inscription.getIdEtudiant().getDateNaissance());
            dto.setSeuil(shared_methods_service.getSeuilScolarite(inscription.getIdEtudiant().getStatus()));

            Double payer = shared_repositories.getPaiement_repositorie().sumMontant(inscription.getId());

            dto.setPayer(Objects.requireNonNullElse(payer, 0.0));
            dto.setReliquat(dto.getSeuil() - dto.getPayer());
            dtoList.add(dto);
        }
        return dtoList;
    }
}
