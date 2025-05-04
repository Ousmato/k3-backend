package Gestion_scolaire.Teachers.repositories;

import Gestion_scolaire.Teachers.entity.Teacher_specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherSpecialite_repositorie extends JpaRepository<Teacher_specialite, Long> {


    Teacher_specialite getByIdSpecialiteIdAndIdTeacherId(long idSpecialite, long idTeacher);

//    @Query("SELECT ")
//    Page<Teacher_specialite> getByIdTeachers(long idTeacher, Pageable pageable);
}
