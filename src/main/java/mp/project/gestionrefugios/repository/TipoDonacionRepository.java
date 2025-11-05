package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.TipoDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDonacionRepository extends JpaRepository<TipoDonacion, Integer> {

  @Query("select t from TipoDonacion t where t.estadoRegistro = :status")
  List<TipoDonacion> getTipoDonacionByStatus(@Param("status") Boolean status);

  @Query("select t from TipoDonacion t where lower(t.nombre) like lower(concat('%', :nombre, '%')) and t.estadoRegistro = true")
  List<TipoDonacion> searchTipoDonacionByNombre(@Param("nombre") String nombre);

  @Query("select t from TipoDonacion t where lower(t.nombre) = lower(:nombre) and t.estadoRegistro = true")
  Optional<TipoDonacion> getTipoDonacionByNombreExacto(@Param("nombre") String nombre);

  @Query("select t from TipoDonacion t where lower(t.descripcion) like lower(concat('%', :descripcion, '%')) and t.estadoRegistro = true")
  List<TipoDonacion> searchTipoDonacionByDescripcion(@Param("descripcion") String descripcion);
}
