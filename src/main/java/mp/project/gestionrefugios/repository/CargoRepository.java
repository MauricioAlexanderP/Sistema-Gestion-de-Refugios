package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

  @Query("select c from Cargo c where c.estadoRegistro = :status")
  public List<Cargo> getCargosByStatus(@Param("status") Boolean status);
}
