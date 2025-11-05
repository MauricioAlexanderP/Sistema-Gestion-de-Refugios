package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Donadores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonadoresRepository extends JpaRepository<Donadores, Integer> {

  @Query("select d from Donadores d where d.estadoRegistro = :status")
  List<Donadores> getDonadoresByStatus(@Param("status") Boolean status);

  @Query("select d from Donadores d where lower(d.nombre) like lower(concat('%', :nombre, '%')) and d.estadoRegistro = true")
  List<Donadores> searchDonadoresByNombre(@Param("nombre") String nombre);

  @Query("select d from Donadores d where lower(d.email) = lower(:email) and d.estadoRegistro = true")
  Optional<Donadores> getDonadorByEmail(@Param("email") String email);

  @Query("select d from Donadores d where d.telefono like concat('%', :telefono, '%') and d.estadoRegistro = true")
  List<Donadores> searchDonadoresByTelefono(@Param("telefono") String telefono);

  @Query("select d from Donadores d where lower(d.direccion) like lower(concat('%', :direccion, '%')) and d.estadoRegistro = true")
  List<Donadores> searchDonadoresByDireccion(@Param("direccion") String direccion);
}
