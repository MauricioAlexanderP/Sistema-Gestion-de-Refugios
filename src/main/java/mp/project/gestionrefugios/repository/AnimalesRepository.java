package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Animales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalesRepository extends JpaRepository<Animales, Integer> {

  @Query("select a from Animales a where a.estadoRegistro = :status")
  public List<Animales> getAnimalesByStatus(@Param("status") Boolean status);

  @Query("select a from Animales a where lower(a.nombre) like lower(concat('%', :nombre, '%')) and a.estadoRegistro = true")
  public List<Animales> searchAnimalesByNombre(@Param("nombre") String nombre);

  @Query("select a from Animales a where a.especie.id = :especieId and a.estadoRegistro = true")
  public List<Animales> getAnimalesByEspecie(@Param("especieId") Integer especieId);

  @Query("select a from Animales a where a.estado.id = :estadoId and a.estadoRegistro = true")
  public List<Animales> getAnimalesByEstado(@Param("estadoId") Integer estadoId);
}
