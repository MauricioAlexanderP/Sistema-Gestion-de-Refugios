package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Especies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspeciesRepository extends JpaRepository<Especies, Integer> {

  @Query("select e from Especies e where e.estadoRegistro = :status")
  public List<Especies> getEspeciesByStatus(@Param("status") Boolean status);
}
