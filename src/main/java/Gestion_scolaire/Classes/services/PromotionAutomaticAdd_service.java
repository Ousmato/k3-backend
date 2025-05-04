package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Classes.dtos.ModuleJsonDto;
import Gestion_scolaire.Classes.dtos.UeReadJsonDto;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Niveaux_Filieres.dtos.FiliereDTO;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.entity.Niveau;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import Gestion_scolaire.Niveaux_Filieres.repositories.SousFilieres_repositorie;
import Gestion_scolaire.Niveaux_Filieres.services.Filieres_service;
import Gestion_scolaire.Niveaux_Filieres.services.Niveau_service;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Ue_repositorie;
import Gestion_scolaire.Services.InfoScool_service;
import Gestion_scolaire.Services.Semestre_service;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.StudentsClasse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionAutomaticAdd_service {

   private final Niveau_service niveau_service;


   private final Filieres_service filieres_service;


    private final Classe_service classe_service;


    private final InfoScool_service infoScool_service;


    private final Shared_methods_service shared_methods_service;


    private final AdminRepositorie admin_repositorie;


    private final Semestre_service semestre_service;


    private final JsonDataService jsonDataService;


    private final Ue_repositorie ue_repositorie;


    private  final Modules_repositories modules_repositories;


    private final  SousFilieres_repositorie sousFilieres_repositorie;


//    @Transactional
//    public Object createSchoolYearAndClassesWithUes(AnneeScolaire newAnneeScolaire, long idAdmin) {
//        AdministrationUsers administrationUsers = admin_repositorie.getByIdAndActive(idAdmin, true);
//        if (administrationUsers == null) {
//            throw new NoteFundException("Admin non trouvable");
//        }
//        // 1. Récupérer ou ajouter les semestres
//        List<Semestres> semestres = semestre_service.getAll();
//        if (semestres.isEmpty()) {
//            semestre_service.add_semestre();
//            semestres = semestre_service.getAll();
//        }
//
//        // 2. Récupérer les niveaux et les filières
//        List<Niveau> niveaux = niveau_service.readAll();
//        List<Filiere> filieres = filieres_service.getFilieres();
//
//        // 3. Ajouter une nouvelle année scolaire
////        List<FiliereDTO> filieresJson = new ArrayList<>();
//        AnneeScolaire anneeScolaire = infoScool_service.add_anneeScolaire(newAnneeScolaire);
//        Facultes faculte = administrationUsers.getIdPoste().getFaculte();
//        Map<Facultes, List<FiliereDTO>> filieresParFaculte = jsonDataService.readAllFilieresGroupedByFaculte();
//        List<FiliereDTO> filieresJson = filieresParFaculte.get(faculte);
//
//
//        //System.out.println("--------------------je suis---------la-------------");
//        // 4. Parcourir les filières
//        for (Filiere filiere : filieres) {
//            // Abréviation de la filière
//            String filiereAbreviation = shared_methods_service.abrevigateName(filiere.getNomFiliere());
//
//            // 5. Parcourir les niveaux
//            for (Niveau niveau : niveaux) {
//                NiveauFilieres niveauFilieres = filieres_service.add(filiere, niveau, anneeScolaire);
//                StudentsClasse classe = classe_service.addProClasse(niveauFilieres.getId(), anneeScolaire.getId());
//                String nomNiveau = classe.getIdFiliere().getIdNiveau().getNom();
//                // Gestion des spécialités pour EEER et autres filières
//                if (filiereAbreviation.equalsIgnoreCase("3ER") ||
//                        (nomNiveau.equalsIgnoreCase("LICENCE 3") && !filiereAbreviation.equalsIgnoreCase("3ER"))) {
//
//                    // Parcourir les spécialités dans le JSON
//                    for (FiliereDTO filiereDTO : filieresJson) {
//                        String filiereAbrevFromJson = shared_methods_service.abrevigateName(filiereDTO.getNomFiliere());
//
//                        // Vérifier si la filière du JSON correspond à celle traitée
//                        if (filiereAbrevFromJson.equalsIgnoreCase(filiereAbreviation)) {
//                            // Ajouter les spécialités pour la classe actuelle
//                            if(filiereDTO.getSous() != null && !filiereDTO.getSous().isEmpty()) {
//                                filiereDTO.getSous().forEach(specialiteDTO -> {
//                                    SousFilieres sousFiliere = new SousFilieres();
//                                    sousFiliere.setIdClasse(classe);
//                                    sousFiliere.setNomSousFiliere(specialiteDTO.getName());
//                                    sousFilieres_repositorie.save(sousFiliere);
//                                });
//                            }else {
//                                continue;
//                            }
//
//                        }
//                    }
//                }
//
//
//                // Déterminer les semestres à associer au niveau
//                int firstSemestre = 0, secondSemestre = 0;
//                if (niveau.getNom().equalsIgnoreCase("LICENCE 1")) {
//                    firstSemestre = 1;
//                    secondSemestre = 2;
//                } else if (niveau.getNom().equalsIgnoreCase("LICENCE 2")) {
//                    firstSemestre = 3;
//                    secondSemestre = 4;
//                } else if (niveau.getNom().equalsIgnoreCase("LICENCE 3")) {
//                    firstSemestre = 5;
//                    secondSemestre = 6;
//                }
//
//                // Charger les UE et Modules des deux semestres
//                if (firstSemestre > 0) {
//                    List<UeReadJsonDto> ueJsonList = jsonDataService.readUeJson(filiereAbreviation, firstSemestre, secondSemestre);
//
//                    System.out.println("----------------ue json list-----------------" +ueJsonList);
//                    // Créer les UE et leurs modules
//                    for (UeReadJsonDto ueReadJsonDto : ueJsonList) {
//                        Semestres semestre = semestre_service.getSemestreByNom(ueReadJsonDto.getSemestre().toUpperCase());
//
//                        // Créer une nouvelle UE
//                        UE ue = new UE();
//                        ue.setIdClasse(classe);
//                        ue.setIdSemestre(semestre);
//                        ue.setIdAdministrationUsers(administrationUsers);
//                        String nomUE = ueReadJsonDto.getUe();
//                        ue.setNomUE(nomUE);
//                        ue.setCodeUE(ueReadJsonDto.getCode());
//                        UE savedUe = ue_repositorie.save(ue);
//
//                        // Associer les modules à l'UE
//                        for (ModuleJsonDto ueModule : ueReadJsonDto.getModules()) {
//                            Modules module = new Modules();
//
//                            if (ueModule.getCredits() > 6 && ueModule.getCredits() != 30) {
//                                throw new NoteFundException("Le coefficient doit être inférieur ou égal à 6 ou égal à 30.");
//                            }
//                            System.out.println("--------------------ue------------------"+ue.getNomUE());
//
//                            String objectif = ueModule.getObjectif();
//                            if (objectif.length() > 255) {
//                                objectif = objectif.substring(0, 255); // Tronque à 255 caractères
//                            }
//                            System.out.println("--------------------coeff------------------"+ueModule.getObjectif().length());
//                            module.setCoefficient(ueModule.getCredits());
//                            module.setIdUe(savedUe);
//                            module.setVolHCM(ueModule.getCm());
//                            module.setVolHTD(ueModule.getTd());
//                            module.setVolTPE(ueModule.getTpe());
//                            module.setVolTP(ueModule.getTp());
//                            module.setNomModule(ueModule.getEcue());
//                            module.setDescription(objectif);
//                            modules_repositories.save(module);
//                        }
//                    }
//                }
//            }
//        }
//
//        return DTO_response_string.addMessage();
//    }


@Transactional
public Object createSchoolYearAndClassesWithUes(AnneeScolaire newAnneeScolaire, long idAdmin) {
    AdministrationUsers admin = getActiveAdmin(idAdmin);
    initSemestresIfNeeded();
    Facultes faculte = admin.getIdPoste().getFaculte();
    List<Niveau> niveaux = niveau_service.readAll();
    List<Filiere> filieres = filieres_service.getFilieresByFaculte(faculte);
    AnneeScolaire anneeScolaire = infoScool_service.add_anneeScolaire(newAnneeScolaire);


    List<FiliereDTO> filieresJson = getFiliereJsonByFaculte(faculte);

    for (Filiere filiere : filieres) {
        processFiliereCycle(filiere, niveaux, filieresJson, anneeScolaire, admin);
    }

    return DTO_response_string.addMessage();
}

    private AdministrationUsers getActiveAdmin(long idAdmin) {
        AdministrationUsers admin = admin_repositorie.getByIdAndActive(idAdmin, true);
        if (admin == null) {
            throw new NoteFundException("Admin non trouvable");
        }
        return admin;
    }

    private void initSemestresIfNeeded() {
        if (semestre_service.getAll().isEmpty()) {
            semestre_service.add_semestre();
        }
    }

    private List<FiliereDTO> getFiliereJsonByFaculte(Facultes faculte) {
        Map<Facultes, List<FiliereDTO>> grouped = jsonDataService.readAllFilieresGroupedByFaculte();
        List<FiliereDTO> list = grouped.get(faculte);
        if (list == null || list.isEmpty()) {
            throw new NoteFundException("Aucune filière trouvée pour la faculté : " + faculte);
        }
        return list;
    }

    private void processFiliereCycle(Filiere filiere, List<Niveau> niveaux, List<FiliereDTO> filieresJson,
                                     AnneeScolaire anneeScolaire, AdministrationUsers admin) {
        String filiereAbreviation = shared_methods_service.abrevigateName(filiere.getNomFiliere());

        for (Niveau niveau : niveaux) {
            NiveauFilieres niveauFilieres = filieres_service.add(filiere, niveau, anneeScolaire);
            StudentsClasse classe = classe_service.addProClasse(niveauFilieres.getId(), anneeScolaire.getId());

            ajouterSousFilieresSiApplicable(filiereAbreviation, niveau, filieresJson, classe);

            int[] semestres = getSemestresForNiveau(niveau);
            if (semestres[0] > 0) {
                List<UeReadJsonDto> ueJsonList = jsonDataService.readUeJson(filiereAbreviation, semestres[0], semestres[1]);
                ajouterUesEtModules(ueJsonList, classe, admin);
            }
        }
    }

    private void ajouterSousFilieresSiApplicable(String filiereAbrev, Niveau niveau, List<FiliereDTO> filieresJson, StudentsClasse classe) {
        String nomNiveau = niveau.getNom();
        if (filiereAbrev.equalsIgnoreCase("3ER") ||
                (nomNiveau.equalsIgnoreCase("LICENCE 3") && !filiereAbrev.equalsIgnoreCase("3ER"))) {

            for (FiliereDTO dto : filieresJson) {
                if (shared_methods_service.abrevigateName(dto.getNomFiliere()).equalsIgnoreCase(filiereAbrev)
                        && dto.getSous() != null) {
                    dto.getSous().forEach(sous -> {
                        SousFilieres entity = new SousFilieres();
                        entity.setIdClasse(classe);
                        entity.setNomSousFiliere(sous.getName());
                        sousFilieres_repositorie.save(entity);
                    });
                }
            }
        }
    }

    private int[] getSemestresForNiveau(Niveau niveau) {
        return switch (niveau.getNom().toUpperCase()) {
            case "LICENCE 1" -> new int[]{1, 2};
            case "LICENCE 2" -> new int[]{3, 4};
            case "LICENCE 3" -> new int[]{5, 6};
            default -> new int[]{0, 0};
        };
    }

    private void ajouterUesEtModules(List<UeReadJsonDto> ueJsonList, StudentsClasse classe, AdministrationUsers admin) {
        for (UeReadJsonDto dto : ueJsonList) {
            Semestres semestre = semestre_service.getSemestreByNom(dto.getSemestre().toUpperCase());

            UE ue = new UE();
            ue.setIdClasse(classe);
            ue.setIdSemestre(semestre);
            ue.setIdAdministrationUsers(admin);
            ue.setNomUE(dto.getUe());
            ue.setCodeUE(dto.getCode());

            UE savedUe = ue_repositorie.save(ue);

            for (ModuleJsonDto moduleDto : dto.getModules()) {
                if (moduleDto.getCredits() > 6 && moduleDto.getCredits() != 30) {
                    throw new NoteFundException("Le coefficient doit être inférieur ou égal à 6 ou égal à 30.");
                }

                Modules module = new Modules();
                module.setIdUe(savedUe);
                module.setNomModule(moduleDto.getEcue());
                module.setCoefficient(moduleDto.getCredits());
                module.setVolHCM(moduleDto.getCm());
                module.setVolHTD(moduleDto.getTd());
                module.setVolTPE(moduleDto.getTpe());
                module.setVolTP(moduleDto.getTp());

                String objectif = moduleDto.getObjectif();
                if (objectif != null && objectif.length() > 255) {
                    objectif = objectif.substring(0, 255);
                }
                module.setDescription(objectif);
                modules_repositories.save(module);
            }
        }
    }


}
