package Gestion_scolaire.students.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Classes.repositories.Classe_repositorie;
import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Repositories.AnneeScolaire_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.dtos.DTO_scolarite;
import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.InscriptionDTO;
import Gestion_scolaire.students.dtos.Student_DTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.enumClass.StudentDiplome;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Students_repositorie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class Inscription_service {

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MehodShared mehodShared;

    @Autowired
    private AnneeScolaire_repositorie anneeScolaire_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;


    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;


    @Transactional
    public Object add(InscriptionDTO inscrit, MultipartFile file) throws IOException {

        int currentYear = LocalDate.now().getYear();

        String plainPassword = "IUFP- "+currentYear+"@";
        inscrit.getIdEtudiant().setPassword(passwordEncoder.encode(plainPassword));

        Students student = mehodShared.validateSudent(inscrit.getIdEtudiant());
        Students studentExist = students_repositorie.findByTelephone( student.getTelephone());
        if (studentExist == null) {

            if(file != null && !file.isEmpty()) {
                String urlPhoto = fileManagers.saveFile(file);
                student.setUrlPhoto(urlPhoto);
            }else {
                student.setUrlPhoto("photo");
            }



            if (inscrit.getIdClasse() != null) {

                String niveauName = inscrit.getIdClasse().getIdFiliere().getIdNiveau().getNom();
                if(student.getDiplome().equals(StudentDiplome.DUT) && niveauName.equals("LICENCE 1") || niveauName.equals("LICENCE 2") ){
                    throw new NoteFundException("Atteintion un diplome DUT ne peut pas s'inscrire en " +niveauName);
                }

                Inscription newIncription = new Inscription();
                newIncription.setDate(LocalDate.now());
                if (newIncription.getDate().isBefore(inscrit.getIdClasse().getIdAnneeScolaire().getDebutAnnee()) || newIncription.getDate().isAfter(inscrit.getIdClasse().getIdAnneeScolaire().getFinAnnee())) {
                    throw new NoteFundException("Inscription non valide veillez choisir une année scolaire en cours");
                }

                inscrit.getIdClasse().setEffectifs(inscrit.getIdClasse().getEffectifs() + 1);
                classe_repositorie.save(inscrit.getIdClasse());

                Students newStudent = students_repositorie.save(student);
                newIncription.setIdClasse(inscrit.getIdClasse());
                newIncription.setIdEtudiant(newStudent);
                newIncription.setPayer(false);
                newIncription.setIdAdmin(inscrit.getIdAdmin());
                inscription_repositorie.save(newIncription);

//                PendingEmail emailPend = new PendingEmail();
//                emailPend.setToSend(inscrit.getEmail());
//                emailPend.setFromAdmin(adminEmail);
//                emailPend.setBody("Bonjour M. %s %s%s,".formatted(inscrit.getNom(), inscrit.getPrenom(), messaSender.message(inscrit, plainPassword)));
//                emailPend.setSubject("Confirmation");
//
//                messaSender.sendSimpleMail(emailPend);
            }
            return DTO_response_string.fromMessage("Inscription effectuée avec succés ");
        } else {
            throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");
        }

    }

    public DTO_scolarite getScolariteAndReliquatByIdIncrit(long idInscrit){
        Inscription inscription = inscription_repositorie.findById(idInscrit);
        if (inscription == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }
        return DTO_scolarite.getScolarite_student(inscription);
    }

    public List<GetInscriptionDto> getAllInscritByYear(long idAnnee){
        List<Inscription> inscriptionList = inscription_repositorie.findByIdClasseIdAnneeScolaireId(idAnnee);
        if (inscriptionList.isEmpty()){
            return new ArrayList<>();
        }
        return inscriptionList.stream()
        .map(list -> {
            GetInscriptionDto dto = GetInscriptionDto.toDto(list);
            dto.setIdEtudiant(Student_DTO.toDTO(list.getIdEtudiant())); // Correction ici
            return dto;
        })
        .sorted(Comparator.comparing(dto -> dto.getIdEtudiant().getNom())) // Tri par nom
        .toList();
    }

    public List<GetInscriptionDto> getInscritCurrentYear(){
        AnneeScolaire currentYear = anneeScolaire_repositorie.findCurrentYear(LocalDate.now());
        return getAllInscritByYear(currentYear.getId());
    }

    public Inscription getInscription(long id) {
        Inscription inscription = inscription_repositorie.findById(id);
        if (inscription == null) {
            throw new RuntimeException("l'etudiant n'existe pas");
        }
        return inscription;
    }

    public Inscription getById(Long id) {
        Optional<Inscription> inscription = inscription_repositorie.findById(id);
        if (inscription.isEmpty()) {
            throw new RuntimeException("L'étudiant n'existe pas");
        }
        return inscription.get();  // Retourne l'objet Inscription directement
    }



}
