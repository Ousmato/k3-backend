package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.Administrators.repositories.AdminRepositorie;
import Gestion_scolaire.Classes.dtos.ModuleJsonDto;
import Gestion_scolaire.Classes.dtos.UeReadJsonDto;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.DTO_response_string;
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
import Gestion_scolaire.SharedService.Shared_service;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.StudentsClasse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionAutomaticAdd_service {
    @Autowired
   private Niveau_service niveau_service;

    @Autowired
   private Filieres_service filieres_service;

    @Autowired
    private Classe_service classe_service;

    @Autowired
    private InfoScool_service infoScool_service;

    @Autowired
    private Shared_service shared_service;

    @Autowired
    private AdminRepositorie admin_repositorie;

    @Autowired
    private Semestre_service semestre_service;

    @Autowired
    private JsonDataService jsonDataService;

    @Autowired
    private Ue_repositorie ue_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    @Autowired
    private SousFilieres_repositorie sousFilieres_repositorie;


    @Transactional
    public Object createSchoolYearAndClassesWithUes(AnneeScolaire newAnneeScolaire, long idAdmin) {
        Admin admin = admin_repositorie.getByIdAdministraAndActive(idAdmin, true);
        if (admin == null) {
            throw new NoteFundException("Admin non trouvable");
        }
        // 1. Récupérer ou ajouter les semestres
        List<Semestres> semestres = semestre_service.getAll();
        if (semestres.isEmpty()) {
            semestre_service.add_semestre();
            semestres = semestre_service.getAll();
        }

        // 2. Récupérer les niveaux et les filières
        List<Niveau> niveaux = niveau_service.readAll();
        List<Filiere> filieres = filieres_service.getFilieres();

        // 3. Ajouter une nouvelle année scolaire
        AnneeScolaire anneeScolaire = infoScool_service.add_anneeScolaire(newAnneeScolaire);
        List<FiliereDTO> filieresJson = jsonDataService.readJsonFilieres();

        System.out.println("--------------------je suis---------la-------------");
        // 4. Parcourir les filières
        for (Filiere filiere : filieres) {
            // Abréviation de la filière
            String filiereAbreviation = shared_service.abrevigateRoleName(filiere.getNomFiliere());


            // 5. Parcourir les niveaux
            for (Niveau niveau : niveaux) {
                NiveauFilieres niveauFilieres = filieres_service.add(filiere, niveau, anneeScolaire);
                StudentsClasse classe = classe_service.addProClasse(niveauFilieres.getId(), anneeScolaire.getId());
                String nomNiveau = classe.getIdFiliere().getIdNiveau().getNom();
                // Gestion des spécialités pour EEER et autres filières
                if (filiereAbreviation.equalsIgnoreCase("EEER") ||
                        (nomNiveau.equalsIgnoreCase("LICENCE 3") && !filiereAbreviation.equalsIgnoreCase("EEER"))) {

                    // Parcourir les spécialités dans le JSON
                    for (FiliereDTO filiereDTO : filieresJson) {
                        String filiereAbrevFromJson = shared_service.abrevigateRoleName(filiereDTO.getNomFiliere());

                        // Vérifier si la filière du JSON correspond à celle traitée
                        if (filiereAbrevFromJson.equalsIgnoreCase(filiereAbreviation)) {
                            // Ajouter les spécialités pour la classe actuelle
                            if(filiereDTO.getSous() != null && !filiereDTO.getSous().isEmpty()) {
                                filiereDTO.getSous().forEach(specialiteDTO -> {
                                    SousFilieres sousFiliere = new SousFilieres();
                                    sousFiliere.setIdClasse(classe);
                                    sousFiliere.setNomSousFiliere(specialiteDTO.getName());
                                    sousFilieres_repositorie.save(sousFiliere);
                                });
                            }else {
                                continue;
                            }

                        }
                    }
                }


                // Déterminer les semestres à associer au niveau
                int firstSemestre = 0, secondSemestre = 0;
                if (niveau.getNom().equalsIgnoreCase("LICENCE 1")) {
                    firstSemestre = 1;
                    secondSemestre = 2;
                } else if (niveau.getNom().equalsIgnoreCase("LICENCE 2")) {
                    firstSemestre = 3;
                    secondSemestre = 4;
                } else if (niveau.getNom().equalsIgnoreCase("LICENCE 3")) {
                    firstSemestre = 5;
                    secondSemestre = 6;
                }

                // Charger les UE et Modules des deux semestres
                if (firstSemestre > 0) {
                    List<UeReadJsonDto> ueJsonList = jsonDataService.readUeJson(filiereAbreviation, firstSemestre, secondSemestre);

                    System.out.println("----------------ue json list-----------------" +ueJsonList);
                    // Créer les UE et leurs modules
                    for (UeReadJsonDto ueReadJsonDto : ueJsonList) {
                        Semestres semestre = semestre_service.getSemestreByNom(ueReadJsonDto.getSemestre().toUpperCase());

                        // Créer une nouvelle UE
                        UE ue = new UE();
                        ue.setIdClasse(classe);
                        ue.setIdSemestre(semestre);
                        ue.setIdAdmin(admin);
                        String nomUE = ueReadJsonDto.getUe();
                        ue.setNomUE(nomUE);
                        ue.setCodeUE(ueReadJsonDto.getCode());
                        UE savedUe = ue_repositorie.save(ue);

                        // Associer les modules à l'UE
                        for (ModuleJsonDto ueModule : ueReadJsonDto.getModules()) {
                            Modules module = new Modules();

                            if (ueModule.getCredits() > 6 && ueModule.getCredits() != 30) {
                                throw new NoteFundException("Le coefficient doit être inférieur ou égal à 6 ou égal à 30.");
                            }
                            System.out.println("--------------------ue------------------"+ue.getNomUE());

                            String objectif = ueModule.getObjectif();
                            if (objectif.length() > 255) {
                                objectif = objectif.substring(0, 255); // Tronque à 255 caractères
                            }
                            System.out.println("--------------------coeff------------------"+ueModule.getObjectif().length());
                            module.setCoefficient(ueModule.getCredits());
                            module.setIdUe(savedUe);
                            module.setNomModule(ueModule.getEcue());
                            module.setDescription(objectif);
                            modules_repositories.save(module);
                        }
                    }
                }
            }
        }

        return DTO_response_string.addMessage();
    }
}
