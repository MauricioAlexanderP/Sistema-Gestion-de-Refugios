package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Razas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RazasRepository extends JpaRepository<Razas, Integer> {

  @Query("select r from Razas r where r.estadoRegistro = :status")
  public List<Razas> getRazasByStatus(@Param("status") Boolean status);

  @Query("select r from Razas r where r.especie.id = :especieId and r.estadoRegistro = true")
  public List<Razas> getRazasByEspecieId(@Param("especieId") Integer especieId);
}
