package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {

  @Query("select u from Usuarios u left join fetch u.rol left join fetch u.personal where u.estadoRegistro = :status")
  public List<Usuarios> getUsuariosByStatus(@Param("status") Boolean status);

  @Query("select u from Usuarios u left join fetch u.rol left join fetch u.personal")
  public List<Usuarios> findAllWithRelations();

  @Query("select u from Usuarios u left join fetch u.rol left join fetch u.personal where u.id = :id")
  public Optional<Usuarios> findByIdWithRelations(@Param("id") Integer id);

  @Query("select u from Usuarios u left join fetch u.rol left join fetch u.personal where u.usuario = :usuario and u.estadoRegistro = true")
  public Optional<Usuarios> findByUsuarioAndEstadoRegistroTrue(@Param("usuario") String usuario);

  @Query("select u from Usuarios u left join fetch u.rol left join fetch u.personal where u.email = :email and u.estadoRegistro = true")
  public Optional<Usuarios> findByEmailAndEstadoRegistroTrue(@Param("email") String email);

  @Query("select u from Usuarios u where u.usuario = :usuario and u.estadoRegistro = true")
  public List<Usuarios> findByUsuarioAndEstadoRegistroTrueList(@Param("usuario") String usuario);

  @Query("select u from Usuarios u where u.email = :email and u.estadoRegistro = true")
  public List<Usuarios> findByEmailAndEstadoRegistroTrueList(@Param("email") String email);

  @Query("select u from Usuarios u where u.usuario = :usuario and u.id != :id and u.estadoRegistro = true")
  public List<Usuarios> findByUsuarioAndIdNotAndEstadoRegistroTrue(@Param("usuario") String usuario,
      @Param("id") Integer id);

  @Query("select u from Usuarios u where u.email = :email and u.id != :id and u.estadoRegistro = true")
  public List<Usuarios> findByEmailAndIdNotAndEstadoRegistroTrue(@Param("email") String email, @Param("id") Integer id);
}
