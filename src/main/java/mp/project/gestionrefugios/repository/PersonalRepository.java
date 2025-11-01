package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Integer> {

  @Query("select p from Personal p left join fetch p.cargo left join fetch p.refugio where p.estadoRegistro = :status")
  public List<Personal> getPersonalByStatus(@Param("status") Boolean status);

  @Query("select p from Personal p left join fetch p.cargo left join fetch p.refugio where p.refugio.id = :refugioId and p.estadoRegistro = true")
  public List<Personal> getPersonalByRefugioId(@Param("refugioId") Integer refugioId);

  @Query("select p from Personal p left join fetch p.cargo left join fetch p.refugio where p.cargo.id = :cargoId and p.estadoRegistro = true")
  public List<Personal> getPersonalByCargoId(@Param("cargoId") Integer cargoId);

  @Query("select p from Personal p left join fetch p.cargo left join fetch p.refugio")
  public List<Personal> findAllWithRelations();

  @Query("select p from Personal p left join fetch p.cargo left join fetch p.refugio where p.id = :id")
  public Optional<Personal> findByIdWithRelations(@Param("id") Integer id);

  @Query("select p from Personal p where p.email = :email and p.estadoRegistro = true")
  public List<Personal> findByEmailAndEstadoRegistroTrue(@Param("email") String email);

  @Query("select p from Personal p where p.telefono = :telefono and p.estadoRegistro = true")
  public List<Personal> findByTelefonoAndEstadoRegistroTrue(@Param("telefono") String telefono);

  @Query("select p from Personal p where p.email = :email and p.id != :id and p.estadoRegistro = true")
  public List<Personal> findByEmailAndIdNotAndEstadoRegistroTrue(@Param("email") String email, @Param("id") Integer id);

  @Query("select p from Personal p where p.telefono = :telefono and p.id != :id and p.estadoRegistro = true")
  public List<Personal> findByTelefonoAndIdNotAndEstadoRegistroTrue(@Param("telefono") String telefono,
      @Param("id") Integer id);
}
