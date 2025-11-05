package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Adopciones;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAdopcionesService {
  public List<Adopciones> getAdopciones();

  public Optional<Adopciones> getAdopcionById(Integer id);

  public int saveAdopcion(Adopciones adopcion);

  public int updateAdopcion(Adopciones adopcion);

  public void deleteAdopcion(Integer id);

  public List<Adopciones> getAdopcionesByStatus(Boolean status);

  public List<Adopciones> getAdopcionesByAdoptante(Integer adoptanteId);

  public List<Adopciones> getAdopcionesByAnimal(Integer animalId);

  public List<Adopciones> getAdopcionesByEstadoAdopcion(Integer estadoId);

  public List<Adopciones> getAdopcionesByFechaSolicitud(LocalDate fechaInicio, LocalDate fechaFin);

  public List<Adopciones> getAdopcionesByFechaAprobacion(LocalDate fechaInicio, LocalDate fechaFin);

  // Mantener compatibilidad con método existente
  public List<Adopciones> getAdopcionesByEstadoRegistro(boolean b);
}
