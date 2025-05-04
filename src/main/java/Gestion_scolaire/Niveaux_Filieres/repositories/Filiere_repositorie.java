package Gestion_scolaire.Niveaux_Filieres.repositories;

import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Filiere_repositorie extends JpaRepository<Filiere, Long> {
    Filiere findByNomFiliere(String nom);
    Filiere findById(long idFiliere);

    @Query("SELECT distinct f From Filiere f INNER JOIN Filiere_specialite fs ON fs.idFiliere.id = f.id where fs.idSpecialite.id =:idSpecialite")
    List<Filiere> getListByIdSpecialite(@Param("idSpecialite") long idSpecialite);

    @Query("select f from Filiere f inner JOIN StudentsClasse cl on cl.idFiliere.idFiliere.id = f.id where cl.idAnneeScolaire.id =:idAnnee")
    List<Filiere> getByIdAnnee(@Param("idAnnee") long idAnnee);

    List<Filiere> findByFaculte(Facultes faculte);

}
