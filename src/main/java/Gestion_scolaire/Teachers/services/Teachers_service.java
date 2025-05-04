package Gestion_scolaire.Teachers.services;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Shareds.Shared_methods_service;
import Gestion_scolaire.Shareds.Shared_repositories;
import Gestion_scolaire.Teachers.dtos.TeacherDTO;
import Gestion_scolaire.Teachers.entity.Specialites;
import Gestion_scolaire.Teachers.entity.Teachers;
import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

@Service
public class Teachers_service {
//
    @Autowired
    private Shared_methods_service shared_methods_service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    String adminEmail = "ousmatotoure98@gmail.com";

    @Autowired
    private Gestion_scolaire.Services.fileManagers fileManagers;

    @Autowired
    private Commom_methods commom_methods;
    @Autowired
    private Shared_repositories shared_repositories;

    public Object add(Teachers teacher, long idAdmin){
        Teachers teach = commom_methods.validateTeacher(teacher);
        System.out.println("---------------------------------je suis quand meme la---------------");
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findById(idAdmin);

        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        String roleName = administrationUsers.getIdPoste().getNom();
        String roleAbrevigate = shared_methods_service.abrevigateName(roleName);
        if (!roleAbrevigate.equalsIgnoreCase("DER")){
            throw new NoteFundException("Autorisation refusée");
        }
//        teach.set(administrationUsers);
//        String pasEncode = passwordEncoder.encode(teach.getPassword());
//        teach.setPassword(pasEncode);
        shared_repositories.getTeacher_repositorie().save(teach);
        return DTO_response_string.addMessage();
    }

    //method pour desactiver un enseignant
    public Object desactive(long id){
        Teachers teachersExist = shared_repositories.getTeacher_repositorie().findById(id);
        if (teachersExist != null){
            teachersExist.setActive(!teachersExist.isActive());
            shared_repositories.getTeacher_repositorie().save(teachersExist);
            return DTO_response_string.updateMessage();
        }
        throw new NoteFundException("Teacher introuvable");
    }

    //count number teacher
    public int countNumber(){
        return shared_repositories.getTeacher_repositorie().countByActive(true);
    }

    //methode pour modifier
    public Object update(Teachers t) throws IOException {
        Teachers teachersExist = shared_repositories.getTeacher_repositorie().findByIdAndActive(t.getId(),true);
        if(teachersExist != null){
            //           ------------------------- cas ou l'image ne pas changer--------------
            shared_methods_service.updateIfNotEmpty(t.getNom(), teachersExist::setNom);
            shared_methods_service.updateIfNotEmpty(t.getPrenom(), teachersExist::setPrenom);
            shared_methods_service.updateIfNotEmpty(t.getEmail(), teachersExist::setEmail);
            shared_methods_service.updateIfNotEmpty(t.getSexe(), teachersExist::setSexe);
//            shared_methods_service.updateIfNotEmpty(t.getPassword(), teachersExist::setPassword);
            shared_methods_service.updateIfNotEmpty(t.getDateNaissance(), teachersExist::setDateNaissance);
//            shared_methods_service.updateIfNotEmpty(t.getGrade(), teachersExist::setGrade);

            // Mise à jour conditionnelle pour les champs potentiellement nulls
            if (t.getDiplome() != null) {
                teachersExist.setDiplome(t.getDiplome());
            }
            if (t.getStatus() != null) {
                teachersExist.setStatus(t.getStatus());
            }
            if (t.getTelephone() != null) {
                teachersExist.setTelephone(t.getTelephone());
            }

            shared_repositories.getTeacher_repositorie().save(teachersExist);
            return DTO_response_string.fromMessage("Mise a jours effectuer avec sucès");
        }else {
            throw new RemoteException("Enseignants n'existe pas");
        }

    }


    //method pour lire les enseignants actifs
    public Page<Teachers> readAll(int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("prenom"));

        Pageable pageable = PageRequest.of(page, size, sort);
        return shared_repositories.getTeacher_repositorie().findAll(pageable);

    }

    public List<Teachers> readAll_teacher() {
        List<Teachers> list = shared_repositories.getTeacher_repositorie().findAll();

        list.sort(Comparator.comparing(Teachers::getNom));
        return list;
    }


    //mehod pour appeler un enseignant
    public Teachers teachById(long id){
        return shared_repositories.getTeacher_repositorie().findById(id);
    }

    //    ----------------------------------- method pour appeler laliste de presences------------------------------

    public Object importTeachers(List<Teachers> list, long idAdmin){
        AdministrationUsers administrationUsers = shared_repositories.getAdminRepositorie().findById(idAdmin);
        if (administrationUsers == null){
            throw new NoteFundException("L'admin est introuvable");
        }
        if(list == null || list.isEmpty()){
            throw new NoteFundException("Choisir au moins un enseignant");
        }
        for (Teachers teacher : list){

//            teacher.setAdministrationUsers(administrationUsers);
            add(teacher, idAdmin);
        }
        return DTO_response_string.addMessage();
    }




    //---------------------------------
    public Page<TeacherDTO> getAllProfile(int page, int size) {

        // Créer un tri par le nom de l'enseignant (en supposant que 'idTeacher.nom' existe)
        Sort sorted = Sort.by(Sort.Order.asc("nom"));

        // Créer un objet Pageable avec pagination et tri
        Pageable pageable = PageRequest.of(page, size, sorted);

        // Récupérer la page de Teacher_specialite avec la pagination et le tri
        Page<Teachers> teachersPage = shared_repositories.getTeacher_repositorie().findAll(pageable);

        // Liste pour collecter les TeacherDTO
        List<TeacherDTO> teacherDTOList = new ArrayList<>();

        // Parcourir les éléments de Teacher_specialite
        for (Teachers teacher : teachersPage) {
            // Convertir chaque Teacher_specialite en TeacherDTO
            TeacherDTO dto = TeacherDTO.toTeacherDTO(teacher);

            // Récupérer les spécialités associées à cet enseignant (optimisation possible)
            List<Specialites> specialites = shared_repositories.getSpecialite_repositorie().findAllByIdTeacher(teacher.getId());
            if(specialites.isEmpty()){
                dto.setSpecialitesList(new ArrayList<>());
            }
            // Ajouter la liste des spécialités au DTO
            dto.setSpecialitesList(specialites);

            // Ajouter le DTO à la liste
            teacherDTOList.add(dto);
        }
        // Retourner la page
        return new PageImpl<>(teacherDTOList, pageable, teachersPage.getTotalElements());
    }



    //-----------------------------------------
    public Teachers getTeacersFiltered(String searchTream){
        return shared_repositories.getTeacher_repositorie().getAllByTelephone(searchTream);
    }

    public List<Teachers> getListFiltered(String searchTream){
        List<Teachers> list = shared_repositories.getTeacher_repositorie().findByNomContaining(searchTream);
        if (list.isEmpty()){
            return new ArrayList<>();
        }
       return  list.stream().limit(10).toList();
    }
}
