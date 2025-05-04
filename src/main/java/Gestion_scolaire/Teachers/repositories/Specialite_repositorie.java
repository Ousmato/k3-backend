package Gestion_scolaire.Teachers.repositories;

import Gestion_scolaire.Teachers.entity.Specialites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Specialite_repositorie extends JpaRepository<Specialites, Long> {
    Specialites findByNom(String name);

    Specialites findById(long id);

    @Query("SELECT DISTINCT s from Specialites s inner join Teacher_specialite ts on ts.idSpecialite.id = s.id where ts.idTeacher.id =:idTeacher")
    List<Specialites> findAllByIdTeacher(@Param("idTeacher") long idTeacher);

    @Query("select distinct  s From Specialites s inner JOIN Filiere_specialite fs on fs.idSpecialite.id =s.id where fs.idFiliere.id =:idFiliere")
    List<Specialites> getByFilierId(@Param("idFiliere") long idFiliere);
}
