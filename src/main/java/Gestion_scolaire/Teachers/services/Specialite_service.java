package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Niveaux_Filieres.dtos.FiliereSpecialiteDto;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import Gestion_scolaire.Niveaux_Filieres.repositories.Filiere_repositorie;
import Gestion_scolaire.Niveaux_Filieres.services.FiliereSpecialite_service;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.entity.Teacher_specialite;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.Teachers.repositories.Specialite_repositorie;
import Gestion_scolaire.Teachers.repositories.TeacherSpecialite_repositorie;
import Gestion_scolaire.Teachers.repositories.Teacher_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Specialite_service {


    private final Specialite_repositorie specialite_repositorie;

    private final Teacher_repositorie teacher_repositorie;

    @Autowired
    private Validator validator;


    private final FiliereSpecialite_service filiereSpecialite_service;

    private final TeacherSpecialite_repositorie teacherSpecialite_repositorie;

    private final Filiere_repositorie filiere_repositorie;

    @Transactional
    public Object addSpecialite(Specialites specialite, List<Filiere> filieres) {
        if(filieres.isEmpty()){
            throw new NoteFundException("Choisir au moins une filière");
        }
        Set<ConstraintViolation<Specialites>> violation = validator.validate(specialite);
        if(!violation.isEmpty()){
            throw new ConstraintViolationException(violation);
        }
        Specialites spl = specialite_repositorie.findByNom(specialite.getNom());
        if(spl != null){
            throw new NoteFundException("La spécialité avec ce nom existe déjà");
        }

//        validateRole(specialite);
      Specialites specialiteSaved =  specialite_repositorie.save(specialite);
        for (Filiere filiere : filieres) {
            filiereSpecialite_service.addFiliereSpecialiste(filiere.getId(), specialiteSaved.getId());
        }
        return DTO_response_string.addMessage();

    }

    public Object updateSpecialite(Specialites specialite) {
        Set<ConstraintViolation<Specialites>> violation = validator.validate(specialite);
        if(!violation.isEmpty()){
            throw new ConstraintViolationException(violation);
        }
        Specialites spl = specialite_repositorie.findById(specialite.getId());
        if(spl == null){
            throw new NoteFundException("La spécialité est introuvable");
        }
//       AdministrationUsers administrationUsers = validateRole(spl);

        spl.setNom(specialite.getNom());
//        spl.setIdAdministrationUsers(administrationUsers);
        specialite_repositorie.save(spl);
        return DTO_response_string.updateMessage();

    }

    @Transactional
    public Object addSpecialiteForTeacher(long idTeacher, List<Specialites> specialites) {
        Teachers teacher = teacher_repositorie.findByIdAndActive(idTeacher, true);
        if(teacher == null){
            throw new NoteFundException("L'enseignant est introuvable");
        }
        System.out.println("------------------"+specialites);
        for (Specialites specialite : specialites) {
            Teacher_specialite tspecialites = teacherSpecialite_repositorie.getByIdSpecialiteIdAndIdTeacherId(specialite.getId(),idTeacher);
            if(tspecialites != null){
                continue;
            }
            addTeacherSpecialite(teacher, specialite);
        }
        return DTO_response_string.addMessage();
    }

    public void addTeacherSpecialite(Teachers teacher, Specialites specialite) {

        Teachers teach = teacher_repositorie.findById(teacher.getId());
        if(teach == null){
            throw new NoteFundException("L'enseignant est introuvable");
        }
        Specialites spl = specialite_repositorie.findById(specialite.getId());
        if(spl == null){
            throw new NoteFundException("La spécialité est introuvable");
        }
        Teacher_specialite tsp = new Teacher_specialite();
        tsp.setIdSpecialite(specialite);
        tsp.setIdTeacher(teacher);

        Set<ConstraintViolation<Teacher_specialite>> violation = validator.validate(tsp);
        if(!violation.isEmpty()){
            throw new ConstraintViolationException(violation);
        }
        teacherSpecialite_repositorie.save(tsp);
    }

    public List<Specialites> getAllSpecialites(){
        return specialite_repositorie.findAll();
    }


    public List<FiliereSpecialiteDto> getAllFilieresSpecialite(){
        List<Specialites> specialitesList = specialite_repositorie.findAll();
        List<FiliereSpecialiteDto> filiereSpecialiteDtoList = new ArrayList<>();
        for (Specialites specialite : specialitesList){
            List<Filiere> filiereList = filiere_repositorie.getListByIdSpecialite(specialite.getId());
            FiliereSpecialiteDto dto = new FiliereSpecialiteDto();
            dto.setFilieres(filiereList);
            dto.setSpecialite(specialite);
            filiereSpecialiteDtoList.add(dto);
        }
        filiereSpecialiteDtoList.sort(Comparator.comparing(filiereSpecialiteDto -> filiereSpecialiteDto.getSpecialite().getNom()));

        return filiereSpecialiteDtoList;
    }

    public List<Specialites> getAllSpecialiteNotAssociatedInByIdTeacher(long idTeacher) {
        List<Specialites> specialitesList = specialite_repositorie.findAll();
        if(specialitesList.isEmpty()){
            return new ArrayList<>();
        }
        List<Specialites> specialitesAssociated = specialite_repositorie.findAllByIdTeacher(idTeacher);
         specialitesList.removeAll(specialitesAssociated);

         return specialitesList;
    }
//    private AdministrationUsers validateRole(Specialites specialite) {
//        AdministrationUsers administrationUsers = admin_repositorie.getByIdAdministraAndActive(specialite.getIdAdministrationUsers().getIdAdministra(), true);
//        if(administrationUsers == null){
//            throw new NoteFundException("L'admin est introuvable");
//
//        }
//        String roleName = administrationUsers.getIdRole().getNom().replace("_", " ").toUpperCase();
//        String roleAbrivate = shared_methods_service.abrevigateName(roleName);
//        if(!roleAbrivate.equalsIgnoreCase("DER")){
//            throw new NoteFundException("Autorisation refusée");
//        }
//        return administrationUsers;
//    }


}
