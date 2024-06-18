package Gestion_scolaire.Services;

import Gestion_scolaire.Models.Documents;
import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Notifications;
import Gestion_scolaire.Repositories.Doc_repositorie;
import Gestion_scolaire.Repositories.Emplois_repositorie;
import Gestion_scolaire.Repositories.Notification_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Doc_service {

    @Autowired
    private Doc_repositorie doc_repositorie;

    @Autowired
    private Notification_repositorie notification_repositorie;

    @Autowired
    private fileManagers fileManagers;

    @Autowired
    private Emplois_repositorie emplois_repositorie;

    public Documents add(Documents documents, MultipartFile multipartFile, Notifications notifications) throws IOException {
        documents.setDate(LocalDate.now());
        Documents docExist = doc_repositorie.findByTitreAndDateAndIdSeanceId(
                documents.getTitre(),documents.getDate(),documents.getIdSeance().getId());
        if (docExist != null){
            throw new RuntimeException("Atteintion ce document vas etre dupliquer");
        }
        String docUrl = fileManagers.saveFile(multipartFile);
        documents.setDocUrl(docUrl);
       Documents docSaved = doc_repositorie.save(documents);
        notification_repositorie.save(notifications);
        return docSaved;
    }
//    -----------------------------------------------------update document----------------------
    public Documents update(Documents documents, MultipartFile file, Notifications notifications) throws IOException {
        Documents docExist = doc_repositorie.findById(documents.getId());
        if (docExist != null){
            docExist.setDocSize(documents.getDocSize());
            docExist.setDate(LocalDate.now());
            docExist.setDescription(documents.getDescription());

            String urlDoc = fileManagers.updateFile(file,docExist.getDocUrl());
            docExist.setDocUrl(urlDoc);
           Documents docUpdate = doc_repositorie.save(docExist);

           Notifications notifiExist = notification_repositorie.findByIdDoc(docExist);
           notifiExist.setTitre(notifications.getTitre());
           notifiExist.setIdDoc(documents);
           notifiExist.setDescription(notifications.getDescription());
           notifiExist.setDate(LocalDate.now());
           notification_repositorie.save(notifiExist);

           return docUpdate;
        }
        throw new RemoteException("document n'existe pas");
    }
//    --------------------------------------------------delete doc for teacher----------------------
    public  String deletedTeacher(long id){
        Documents docExist = doc_repositorie.findById(id);
        if (docExist != null){
            doc_repositorie.delete(docExist);
            return "sucess";
        }
        throw new RuntimeException("document n'existe pas");
    }

//    ------------------------------delete doc for students-------------------------------------
    public  String deletedStudent(long id){
        Documents docExist = doc_repositorie.findById(id);
        if (docExist != null){
            docExist.setDeleted(!docExist.isDeleted());
            return "sucess";
        }
        throw new RuntimeException("document n'existe pas");
    }

//    --------------------------------read all doc
    public List<Documents> readCurentDoc(){
        Emplois emploi = emplois_repositorie.findEmploisActif(LocalDate.now());
        if (emploi == null) {
            // Si la liste est vide, retourner une liste vide
            return new ArrayList<>();
        }
        return doc_repositorie.findByIdSeanceIdEmploisDateFin(emploi.getDateFin());
    }
//    ----------------------------------
}
