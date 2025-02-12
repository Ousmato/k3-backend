package Gestion_scolaire.Administrators.repositories;

import Gestion_scolaire.Administrators.entity.Roles;
import Gestion_scolaire.EnumClasse.TypeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Role_repositorie extends JpaRepository<Roles, Long> {

    Roles findByNomAndTypeFiliere(String name, TypeFiliere typeFiliere);

    Roles findById(long id);

    Roles findByNom(String nom);

    @Query("SELECT r from Roles r inner  JOIN Admin a on a.idRole.id = r.id where r.id =:idRole and a.active =:active")
    Roles findByIdRoleAndActive(@Param("idRole") long idRole, @Param("active") boolean active);

    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.idRole.id = :idRole")
    boolean isRoleAssignedToAdmin(@Param("idRole") long idRole);


}
