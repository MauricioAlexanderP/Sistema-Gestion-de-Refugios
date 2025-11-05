package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Adopciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface AdopcionesRepository extends JpaRepository<Adopciones, Integer> {

  @Query("select a from Adopciones a where a.estadoRegistro = :status")
  List<Adopciones> getAdopcionesByStatus(@Param("status") Boolean status);

  @Query("select a from Adopciones a where a.adoptante.id = :adoptanteId and a.estadoRegistro = true")
  List<Adopciones> getAdopcionesByAdoptante(@Param("adoptanteId") Integer adoptanteId);

  @Query("select a from Adopciones a where a.animal.id = :animalId and a.estadoRegistro = true")
  List<Adopciones> getAdopcionesByAnimal(@Param("animalId") Integer animalId);

  @Query("select a from Adopciones a where a.estado.id = :estadoId and a.estadoRegistro = true")
  List<Adopciones> getAdopcionesByEstadoAdopcion(@Param("estadoId") Integer estadoId);

  @Query("select a from Adopciones a where a.fechaSolicitud between :fechaInicio and :fechaFin and a.estadoRegistro = true")
  List<Adopciones> getAdopcionesByFechaSolicitud(@Param("fechaInicio") LocalDate fechaInicio,
      @Param("fechaFin") LocalDate fechaFin);

  @Query("select a from Adopciones a where a.fechaAprobacion between :fechaInicio and :fechaFin and a.estadoRegistro = true")
  List<Adopciones> getAdopcionesByFechaAprobacion(@Param("fechaInicio") LocalDate fechaInicio,
      @Param("fechaFin") LocalDate fechaFin);

  // Mantener compatibilidad con método existente
  @Query("select a from Adopciones a where a.estadoRegistro = :b")
  List<Adopciones> findByEstadoRegistro(@Param("b") boolean b);
}
