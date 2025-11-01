package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Refugios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefugiosRepository extends JpaRepository<Refugios, Integer> {

  @Query("select r from Refugios r where r.estadoRegistro = :status")
  public List<Refugios> getRefugiosByStatus(@Param("status") Boolean status);
}
