package Gestion_scolaire.Teachers.repositories;

import Gestion_scolaire.Teachers.entity.Teacher_specialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSpecialite_repositorie extends JpaRepository<Teacher_specialite, Long> {

    List<Teacher_specialite> findByIdTeacherIdEnseignant(long idTeacher);

    Teacher_specialite getByIdSpecialiteIdAndIdTeacherIdEnseignant(long idSpecialite, long idTeacher);

//    @Query("SELECT ")
//    Page<Teacher_specialite> getByIdTeachers(long idTeacher, Pageable pageable);
}
