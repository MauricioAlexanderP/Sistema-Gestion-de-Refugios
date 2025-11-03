package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Adoptantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptantesRepository extends JpaRepository<Adoptantes, Integer> {

  @Query("select a from Adoptantes a where a.estadoRegistro = :status")
  public List<Adoptantes> getAdoptantesByStatus(@Param("status") Boolean status);

  @Query("select a from Adoptantes a where lower(a.nombre) like lower(concat('%', :nombre, '%')) and a.estadoRegistro = true")
  public List<Adoptantes> searchAdoptantesByNombre(@Param("nombre") String nombre);

  @Query("select a from Adoptantes a where lower(a.email) = lower(:email) and a.estadoRegistro = true")
  public Optional<Adoptantes> getAdoptanteByEmail(@Param("email") String email);

  @Query("select a from Adoptantes a where a.telefono like concat('%', :telefono, '%') and a.estadoRegistro = true")
  public List<Adoptantes> searchAdoptantesByTelefono(@Param("telefono") String telefono);
}
