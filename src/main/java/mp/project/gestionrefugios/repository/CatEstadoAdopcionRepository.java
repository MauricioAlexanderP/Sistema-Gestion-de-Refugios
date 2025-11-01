package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.CatEstadoAdopcion;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CatEstadoAdopcionRepository extends JpaRepository<CatEstadoAdopcion, Integer> {

  @Query("select e from CatEstadoAdopcion e where e.estadoRegistro = :status")
  public List<CatEstadoAdopcion> getEstadoAdopcionByStatus(Boolean status);
}
