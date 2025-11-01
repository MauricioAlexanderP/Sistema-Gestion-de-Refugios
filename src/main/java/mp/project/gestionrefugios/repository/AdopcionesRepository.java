package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Adopciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdopcionesRepository extends JpaRepository<Adopciones, Integer> {

  @Query("select a from Adopciones a where a.estadoRegistro = :b")
  List<Adopciones> findByEstadoRegistro(@Param("b") boolean b);
}
