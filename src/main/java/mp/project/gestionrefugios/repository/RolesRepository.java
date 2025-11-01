package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

  @Query("select r from Roles r where r.estadoRegistro = :status")
  public List<Roles> getRolesByStatus(@Param("status") Boolean status);
}
