package Gestion_scolaire.Services;

import Gestion_scolaire.Dto_classe.*;
import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.Models.*;
import Gestion_scolaire.Repositories.*;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Doc_service {

    @Autowired
    private Doc_repositorie doc_repositorie;

    @Autowired
    private Soutenance_repositorie soutenance_repositorie;

    @Autowired
    private Common_service common_service;

    @Autowired
    private Jury_repositorie jury_repositorie;

    @Autowired
    private StudentDoc_repositorie studentDoc_repositorie;

    @Autowired
    private Teacher_repositorie teacher_repositorie;

    //    ----------------------------------
    @Transactional
    public Object addDoc(DocDTO dto) {

          Documents docSaved = doc_repositorie.save(dto.getIdDocument());
          for (Studens student: dto.getIdEtudiant()){
              StudentDoc newStudentDoc = new StudentDoc();
              newStudentDoc.setIdDocument(docSaved);
              newStudentDoc.setIdEtudiant(student);
            studentDoc_repositorie.save(addStudentDoc(newStudentDoc, docSaved));
          }

        return DTO_response_string.fromMessage("Ajout éffectué avec succès", 200);
    }

//    -----------------------------------------get all doc
    public List<Documents> getAllDocs(){
        List<Documents> list = doc_repositorie.findAll();
        if(list.isEmpty()){
            return new ArrayList<>();
        }
        list.sort(Comparator.comparing(Documents::getDate)
                .thenComparing(Documents::getDocType));

        return list;
    }

//    -------------------------------------------------get all by annee and id classe
    public Page<DocDTO> getByIdAnneeAndIdClasse(int page, int pageSize, long idAnnee){

//        Sort sort = Sort.by(Sort.Order.asc("docType"));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<StudentDoc> docs = studentDoc_repositorie.getAllByIdEtudiantIdClasseIdAnneeScolaireId( idAnnee, pageable);
        System.out.println("----------------------------------------------------"+docs+ "-----------------------------------------");

        if(docs.isEmpty()){
            return Page.empty();
        }

        List<DocDTO> docDTOs = docs.stream()
                .map(DocDTO::toDocDTO)
                .toList();

        // Retour d'une nouvelle PageImpl avec les DTOs, la pagination et le nombre total d'éléments
        return new PageImpl<>(docDTOs, pageable, docs.getTotalElements());

    }

    //    --------------------------------------------get  all current doc
    public Page<DocDTO> defultCurrentDocs(int page, int size){

        //        Sort sort = Sort.by(Sort.Order.asc("date"),Sort.Order.asc("docType"));
        Pageable pageable = PageRequest.of(page, size);
        LocalDate date = LocalDate.now();
        LocalDate startDate =  date.minusYears(1);
        Page<StudentDoc> docs = studentDoc_repositorie.getByIdDocumentDateBetween(startDate, LocalDate.now(), pageable);
        if(docs.isEmpty()){
            return Page.empty();
        }
        List<DocDTO> docDTOs = docs.stream()
                .map(DocDTO::toDocDTO)
                .toList();

        //        List<Studens> listStudent = new ArrayList<>();
        for (DocDTO docDTO : docDTOs) {
            // Récupérer les étudiants associés à ce document spécifique
            List<StudentDoc> studensList = studentDoc_repositorie.findByIdDocumentId(docDTO.getIdDocument().getId());

            // Extraire les étudiants de la liste
            List<Studens> listStudent = studensList.stream()
                    .map(StudentDoc::getIdEtudiant) // On récupère chaque étudiant
                    .toList();

            // Associer la liste des étudiants au DocDTO
            docDTO.setIdEtudiant(listStudent);
        }
        // Retour d'une nouvelle PageImpl avec les DTOs, la pagination et le nombre total d'éléments
        return new PageImpl<>(docDTOs, pageable, docs.getTotalElements());

    }

    //    ---------------------------------------get all doc by classe id
    public List<DocDTO> getDocsByIdClass(long idClass){
        List<StudentDoc> listDocs = studentDoc_repositorie.findAllByIdEtudiantIdClasseId(idClass);
        if(listDocs.isEmpty()){
            return new ArrayList<>();
        }
        List<Studens> listStudent = new ArrayList<>();
        for (StudentDoc doc: listDocs){
            List<StudentDoc> studensList = studentDoc_repositorie.findByIdDocumentId(doc.getIdDocument().getId());
            for (StudentDoc student : studensList){
                listStudent.add(student.getIdEtudiant());
            }
        }

        List<DocDTO> docDTOs = listDocs.stream().map(DocDTO::toDocDTO).toList();

        for(DocDTO docDTO: docDTOs){
            docDTO.setIdEtudiant(listStudent);
        }

        return docDTOs;
    }

//    ---------------------------------add soutenance program
    @Transactional
    public Object addProgramSoutenance(ProgramSoutenanceDto dto){

        StudentDoc docExist = studentDoc_repositorie.findById(dto.getSoutenance().getIdDoc());
        if(docExist == null){
            throw new NoteFundException("Invalide le document est introuvable");
        }

        if(docExist.getIdDocument().isSoutenue()){
            throw new NoteFundException("L'étudiant  est déjà soutenu");
        }
        if(dto.getJurys().size() != 3){
            throw new NoteFundException("Invalide, Verifier la présence de tout les jurys.");
        }

        LocalTime hDebut = dto.getSoutenance().getHeureDebut();
        LocalTime hFin = dto.getSoutenance().getHeureFin();
        if (hDebut.isAfter(hFin)) {
            throw new RuntimeException("L'heure de début ne peut pas être après l'heure de fin.");
        }

        Duration duration = Duration.between(hDebut, hFin);
        if (duration.toMinutes() > 45) {
            throw new RuntimeException("La durée ne doit pas dépasser 45 minutes.");
        }

        List<Salles> occuperForDate = common_service.salle_occuper_toDay(docExist.getIdDocument().getDate());
        if(!occuperForDate.isEmpty()) {
            throw new NoteFundException("La salle : " + dto.getSoutenance().getIdSalle().getNom() + "est occupé");
        }
        Soutenance souenanceExist = soutenance_repositorie.getByHeureDebutAndHeureFinAndDate(
                dto.getSoutenance().getHeureDebut(), dto.getSoutenance().getHeureFin(), dto.getSoutenance().getDate()
        );
        if(souenanceExist != null){
            if (souenanceExist.getHeureFin().isAfter(dto.getSoutenance().getHeureDebut()) && souenanceExist.getHeureFin().isBefore(dto.getSoutenance().getHeureFin())) {
                throw new NoteFundException("L'heure de fin de la soutenance se trouve dans l'intervalle d'une autre soutenance existante pour cette date.");
            }

        }

        Soutenance stn =  soutenance_repositorie.findByIdDocId(docExist.getId());
        if(stn != null){
            stn.setHeureDebut(dto.getSoutenance().getHeureDebut());
            stn.setHeureFin(dto.getSoutenance().getHeureFin());
            stn.setDate(dto.getSoutenance().getDate());
            stn.setIdSalle(dto.getSoutenance().getIdSalle());
            stn.getIdDoc().getIdDocument().setProgrammer(true);
            soutenance_repositorie.save(stn);
            return DTO_response_string.fromMessage("Ajout éffectué avec succès", 200);

        }
        Soutenance saved = soutenance_repositorie.save(getSoutenance(dto.getSoutenance(), docExist));

        for (JuryDto jr: dto.getJurys()){

            Jury jury = new Jury();
            Teachers teacherExist = teacher_repositorie.findByIdEnseignant(jr.getIdTeacher());
            jury.setIdTeacher(teacherExist);
            jury.setIdSoutenance(saved);
            jury.setRole(jr.getRole());
            jury_repositorie.save(jury);

            docExist.getIdDocument().setProgrammer(true);
            doc_repositorie.save(docExist.getIdDocument());

        }
        return DTO_response_string.fromMessage("Ajout éffectué avec succès", 200);

    }
////    -----------------------------------------------
//    public Soutenance soutenanceWithDay(SoutenanceDTO dto, List<Soutenance> listByDate, StudentDoc docExist) {
//        Soutenance newSouenance = new Soutenance();
//
//        for (Soutenance stnce: listByDate){
//            newSouenance.setIdDoc(docExist);
//            newSouenance.setDate(stnce.getDate());
//            newSouenance.setIdSalle(stnce.getIdSalle());
//            newSouenance.setHeureDebut(dto.getHeureDebut());
//            newSouenance.setHeureFin(dto.getHeureFin());
//        }
//        return newSouenance;
//    }

    public Soutenance getSoutenance(SoutenanceDTO dto, StudentDoc docExist) {


        Soutenance soutenance = new Soutenance();

        soutenance.setIdDoc(docExist);
        soutenance.setDate(dto.getDate());
        soutenance.setHeureDebut(dto.getHeureDebut());
        soutenance.setHeureFin(dto.getHeureFin());
        soutenance.setIdSalle(dto.getIdSalle());
        return soutenance;
    }
//----------------------------------------
    public List<SoutenanceDTO> getAllSoutenancesActive() {
        List<SoutenanceDTO> dtos = new ArrayList<>();
        List<Soutenance> soutenanceList = soutenance_repositorie.getByDate(LocalDate.now());

        for (Soutenance soutenance : soutenanceList) {

            SoutenanceDTO newDto = SoutenanceDTO.toDto(soutenance);


            List<StudentDoc> studentDocs = studentDoc_repositorie.findByIdDocumentId(soutenance.getIdDoc().getId());

            List<Studens> listStudent = studentDocs.stream()
                    .map(StudentDoc::getIdEtudiant) // On récupère chaque étudiant
                    .toList();

            for (Studens student : listStudent) {
                newDto.setFiliere(student.getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
                newDto.setNiveaux(student.getIdClasse().getIdFiliere().getIdNiveau().getNom());
            }

           List<Jury> jurys = jury_repositorie.findByIdSoutenanceId(soutenance.getId());
            for(StudentDoc studentDoc: studentDocs){
                newDto.setIdTeacher(studentDoc.getIdDocument().getIdEncadrant());
            }

            List<JuryDto> juryList = jurys.stream()
                    .map(jury -> {
                        JuryDto dto = new JuryDto();
                        dto.setId(jury.getId());
                        dto.setRole(jury.getRole());
                        dto.setTeachers(jury.getIdTeacher());
                    return dto;
                    }).collect(Collectors.toList());

            newDto.setIdJury(juryList);
            newDto.setStudents(listStudent);

            dtos.add(newDto);

        }
        return dtos;
    }

//    -----------------------------------------------
    public StudentDoc addStudentDoc(StudentDoc studentDoc, Documents doc){
        StudentDoc docExist = studentDoc_repositorie.findByIdDocumentDocTypeAndIdEtudiantIdEtudiant(studentDoc.getIdDocument().getDocType(), studentDoc.getIdEtudiant().getIdEtudiant());
            if (docExist != null) {
                if(doc.getId() != 0){
                    doc_repositorie.delete(doc);
                }
                throw new NoteFundException("Impossible l'étudiant à déjà déposer son  "  + docExist.getIdDocument().getDocType().toString().toUpperCase() + " veillez changer le type de doccument " );
            }

        if(studentDoc.getIdDocument().getDocType() == DocType.memoire){
                StudentsClasse classe = studentDoc.getIdEtudiant().getIdClasse();
                if(!Objects.equals(classe.getIdFiliere().getIdNiveau().getNom(), "LICENCE 3") &&
                        !Objects.equals(classe.getIdFiliere().getIdNiveau().getNom(), "MASTER 1 ") &&
                        !Objects.equals(classe.getIdFiliere().getIdNiveau().getNom(), "MASTER 2 ")){
                    if(doc.getId() != 0){
                        doc_repositorie.delete(doc);
                    }
                    throw new NoteFundException("L'étudiant ne pas autoriser à déposer  un " +  DocType.memoire.toString().toUpperCase() );
                }
        }

        if(studentDoc.getIdDocument().getDocType() == DocType.rapport){
            StudentsClasse classe = studentDoc.getIdEtudiant().getIdClasse();
            if(!classe.getIdFiliere().getIdNiveau().getNom().equals("LICENCE 2")){
                throw new NoteFundException("Un étudiant avec ce niveau : "+ classe.getIdFiliere().getIdNiveau().getNom() + "n'est pas autoriser a déposer un Rapport");
            }
        }

        return studentDoc;
    }

    public int countMemoire(){
        int count = 0;
      return   count = doc_repositorie.countByDocType(DocType.memoire);
    }
    public int countRapport(){
        int count = 0;
        return   count = doc_repositorie.countByDocType(DocType.rapport);
    }


//    -------------------------
    public boolean annulerProgramSoutenance(long idDoc){
        Documents doc = doc_repositorie.findById(idDoc);
        if(doc != null){
            doc.setProgrammer(false);
            doc_repositorie.save(doc);
            return true;
        }
        return false;
    }

//   -------------------------------
    public Object addSoutenanceNote(long idDoc, double note){
        Documents doc = doc_repositorie.findById(idDoc);
        if(doc != null){
            Soutenance stn = soutenance_repositorie.getByIdDocIdDocumentId(doc.getId());
            if(stn.getDate().isAfter(LocalDate.now())){
                throw new NoteFundException("Impossible de noter une soutenance en cours");
            }
            doc.setNote(note);
            doc.setSoutenue(true);
            doc_repositorie.save(doc);

            return DTO_response_string.fromMessage("Note ajouter avec succès", 200);
        }
        return null;
    }
    
}
