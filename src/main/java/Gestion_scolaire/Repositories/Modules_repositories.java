package Gestion_scolaire.Repositories;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Modules_repositories extends JpaRepository<Modules, Long> {
    List<Modules> findByIdUeId(long id);

    Modules findByIdUeIdAndId(long idUe, long id);
    Modules findByIdUeAndNomModule(UE idUe, String nom);
    List<Modules> findAllById(long idClasse);

    Modules findById(long id);

    @Query("SELECT DISTINCT m FROM Modules m INNER JOIN Emplois e ON e.idModule.id = m.id WHERE e.idClasse.id =:idClasse AND e.idSemestre.id =:idSemestre AND e.dateFin <= :date")
    List<Modules> findModulesWithEmplois(@Param("idClasse") long idClasse, @Param("idSemestre") long idSemestre, @Param("date") LocalDate date);

}
