package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

  @Query("select i from Inventario i where i.estadoRegistro = :status")
  List<Inventario> getInventarioByStatus(@Param("status") Boolean status);

  @Query("select i from Inventario i where lower(i.nombre) like lower(concat('%', :nombre, '%')) and i.estadoRegistro = true")
  List<Inventario> searchInventarioByNombre(@Param("nombre") String nombre);

  @Query("select i from Inventario i where lower(i.nombre) = lower(:nombre) and i.estadoRegistro = true")
  Optional<Inventario> getInventarioByNombreExacto(@Param("nombre") String nombre);

  @Query("select i from Inventario i where i.tipo.id = :tipoId and i.estadoRegistro = true")
  List<Inventario> getInventarioByTipo(@Param("tipoId") Integer tipoId);

  @Query("select i from Inventario i where i.refugio.id = :refugioId and i.estadoRegistro = true")
  List<Inventario> getInventarioByRefugio(@Param("refugioId") Integer refugioId);

  @Query("select i from Inventario i where i.tipo.id = :tipoId and i.refugio.id = :refugioId and i.estadoRegistro = true")
  List<Inventario> getInventarioByTipoAndRefugio(@Param("tipoId") Integer tipoId,
      @Param("refugioId") Integer refugioId);

  // Consultas específicas para stock mínimo
  @Query("select i from Inventario i where i.cantidad <= i.stockMinimo and i.estadoRegistro = true")
  List<Inventario> getInventarioBajoStockMinimo();

  @Query("select i from Inventario i where i.cantidad <= i.stockMinimo and i.refugio.id = :refugioId and i.estadoRegistro = true")
  List<Inventario> getInventarioBajoStockMinimoByRefugio(@Param("refugioId") Integer refugioId);

  @Query("select i from Inventario i where i.cantidad < :cantidad and i.estadoRegistro = true")
  List<Inventario> getInventarioConStockMenorA(@Param("cantidad") Integer cantidad);

  @Query("select i from Inventario i where i.cantidad = 0 and i.refugio.id = :refugioId and i.estadoRegistro = true")
  List<Inventario> getInventarioEnStockCritico(@Param("refugioId") Integer refugioId);
}
