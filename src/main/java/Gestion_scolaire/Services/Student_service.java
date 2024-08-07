package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.MailSender.MessaSender;
import Gestion_scolaire.MailSender.PendingEmail;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentsClasse;
import Gestion_scolaire.Models.StudentsPresence;
import Gestion_scolaire.Repositories.Classe_repositorie;
import Gestion_scolaire.Repositories.StudentsPresene_repositorie;
import Gestion_scolaire.Repositories.Students_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Student_service {

    @Autowired
    private Students_repositorie students_repositorie;

    @Autowired
    private Classe_repositorie classe_repositorie;
    
    @Autowired
    MessaSender messaSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentsPresene_repositorie studentsPresene_repositorie;

    @Autowired
    private fileManagers fileManagers;

    String adminEmail = "ousmatotoure98@gmail.com";

    public Object add(Studens studens, MultipartFile file) throws IOException {
//        System.out.printf("%d-------------------------id de la classe---------%n", studens.getIdClasse().getId());
        String telephone = String.valueOf(studens.getTelephone());
        if(telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }
        LocalDate dateNaissance = LocalDate.now().minusYears(15);
        if(dateNaissance.isBefore(studens.getDateNaissance())) {
            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
        }
        if(studens.getMatricule().length() != 12){
            throw new NoteFundException("Le matricule n'est pas valide");
        }
        Studens studentExist = students_repositorie.findByMatriculeAndTelephone(studens.getMatricule(), studens.getTelephone());
        if(studentExist == null){

            StudentsClasse classeExist = classe_repositorie.findById(studens.getIdClasse().getId());
            if(classeExist != null){
                double class_scolarite = classeExist.getScolarite();
                double student_scolarite = studens.getScolarite();
                double scolarite = class_scolarite - student_scolarite;

                classeExist.setEffectifs(classeExist.getEffectifs()+1);
                classe_repositorie.save(classeExist);

                String urlPhoto = fileManagers.saveFile(file);
                studens.setUrlPhoto(urlPhoto);
                studens.setDate(LocalDate.now());
                studens.setIdClasse(classeExist);

                String plainPassword = studens.getPassword();
                studens.setPassword(passwordEncoder.encode(plainPassword));
                students_repositorie.save(studens);

                PendingEmail emailPend = new PendingEmail();
                emailPend.setToSend(studens.getEmail());
                emailPend.setFromAdmin(adminEmail);
                emailPend.setBody("Bonjour M. %s %s%s,".formatted(studens.getNom(), studens.getPrenom(), messaSender.message(studens, plainPassword)));
                emailPend.setSubject("Confirmation");

                messaSender.sendSimpleMail(emailPend);
            }
            return DTO_response_string.fromMessage("Inscription effectuée avec succé ", 200); 
        }else {
            throw new RuntimeException("l'etudiant avec ce numero matricule  et ce numero de telephone existe deja");
        }

    }
//    -----------------------------------------------------------------------------------
    public Object update(Studens studens, MultipartFile file) throws IOException{
//System.out.println(studens +" ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"+ file);
        String telephone = String.valueOf(studens.getTelephone());
        if(telephone.length() > 8) {
            throw new NoteFundException("Le numéro de téléphone ne doit pas dépasser 8 chiffres");
        }
        LocalDate dateNaissance = LocalDate.now().minusYears(15);
        if(dateNaissance.isBefore(studens.getDateNaissance())) {
            throw new NoteFundException("La date de naissance n'est pas valide. L'étudiant doit avoir au moins 15 ans.");
        }
        if(studens.getMatricule().length() != 12){
            throw new NoteFundException("Le matricule n'est pas valide");
        }

        Studens studentExist = students_repositorie.findByIdEtudiant(studens.getIdEtudiant());
        if (studentExist == null) {
            throw new NoteFundException("L'étudiant n'existe pas");
        }

        // Mise à jour de la photo si le fichier est fourni
        if (file != null && !file.isEmpty()) {
            String urlPhoto = fileManagers.updateFile(file, studentExist.getUrlPhoto());
            studentExist.setUrlPhoto(urlPhoto);
        }

            studentExist.setDate(LocalDate.now());
            studentExist.setMatricule(studens.getMatricule());
            studentExist.setScolarite(studens.getScolarite());
            studentExist.setSexe(studens.getSexe());
            studentExist.setEmail(studens.getEmail());
            studentExist.setDateNaissance(studens.getDateNaissance());
            studentExist.setLieuNaissance(studens.getLieuNaissance());
            studentExist.setNom(studens.getNom());
            studentExist.setPrenom(studens.getPrenom());
            studentExist.setPassword(passwordEncoder.encode(studens.getPassword()));

            students_repositorie.save(studentExist);
            return DTO_response_string.fromMessage("Modification effectuée avec succé",200);


    }
//    -----------------------------methode pour desactiver un etudiant-------------------------------------------
    public Object desable(long id){
        Studens studensExist = students_repositorie.findByIdEtudiant(id);
        if(studensExist != null){
            studensExist.setActive(!studensExist.isActive());
           students_repositorie.save(studensExist);
           return DTO_response_string.fromMessage("Changement d'etat effectué avec succé", 200);
        }
       throw new NoteFundException("Student does not exist");
    }
//    -----------------------------------------------------methode pour appeller tous les etudiants active----------------
    public Page<Studens> readAll(int page, int pageSize){
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Studens> studensList = students_repositorie.findAll(pageable);
        if (studensList.isEmpty()) {
            return Page.empty(pageable);
        } else {
            return studensList;
        }
    }
//    ----------------------------find all student
    public List<Studens> find_all(){
        return students_repositorie.findAll();
    }
//    --------------------------------------------------methode appeler un etudiant par id----------------------
    public Studens studenById(long id){
        Studens studensExist = students_repositorie.findByIdEtudiant(id);
        if (studensExist != null){
            return studensExist;
        }else {
            throw new NoteFundException("l'etudiant est introuvable");
        }
    }
//    ---------------------------method les etudiant de la classe-----------------------------------
    public Page<Studens> readAllByClassId(int page, int pageSize,  long idClass){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Studens> list = students_repositorie.findByIdClasseId(idClass, pageable);
        if (list.isEmpty()){
            return Page.empty(pageable);
        }
        return list;
    }

//    ----------------------add presence student ----------------------------
    public StudentsPresence addPresence(StudentsPresence presence){
        StudentsPresence studentsPresenceExist = studentsPresene_repositorie.findByIdSeanceAndIdStudentsAndStatus(
                presence.getIdSeance(), presence.getIdStudents(), presence.isStatus()
        );
        if (studentsPresenceExist != null){
            studentsPresenceExist.setStatus(!studentsPresenceExist.isStatus());
            return studentsPresene_repositorie.save(studentsPresenceExist);

        }
        return studentsPresene_repositorie.save(presence);
    }
//    ------------------------method get all presence ------------------------------
    public List<StudentsPresence> getListPresence(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<StudentsPresence> list = studentsPresene_repositorie.findByStatusAndDateBetween(true,startOfMonth,endOfMonth);
        if (list.isEmpty()){
            return new ArrayList<>();
        }
        return list;
    }
//-----------------------------update scolarite
    public Object update_scolarite(long idStudent, double scolarite){
        Studens studensExist = students_repositorie.findByIdEtudiant(idStudent);
        if (studensExist != null){
            double montant = studensExist.getScolarite() + scolarite; // Par exemple, simple addition
            studensExist.setScolarite(montant);
            students_repositorie.save(studensExist);
          return DTO_response_string.fromMessage("Modification effectuer avec succé", 200);
        }
        throw new NoteFundException("Student does not exist");
    }
}
