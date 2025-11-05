package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.interfaces.IAdopcionesService;
import mp.project.gestionrefugios.model.Adopciones;
import mp.project.gestionrefugios.repository.AdopcionesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdopcionesService implements IAdopcionesService {

  @Autowired
  private AdopcionesRepository data;

  @Override
  public List<Adopciones> getAdopciones() {
    return data.findAll();
  }

  @Override
  public Optional<Adopciones> getAdopcionById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveAdopcion(Adopciones adopcion) {
    // Validaciones
    validateAdopcion(adopcion);

    data.save(adopcion);
    return adopcion.getId();
  }

  @Override
  public int updateAdopcion(Adopciones adopcion) {
    // Verificar que la adopción existe antes de actualizarla
    if (adopcion.getId() != null && data.existsById(adopcion.getId())) {
      // Validaciones
      validateAdopcion(adopcion);

      data.save(adopcion);
      return adopcion.getId();
    }
    return 0;
  }

  @Override
  public void deleteAdopcion(Integer id) {
    Optional<Adopciones> adopcionOptional = data.findById(id);
    if (adopcionOptional.isPresent()) {
      Adopciones adopcion = adopcionOptional.get();
      adopcion.setEstadoRegistro(false);
      data.save(adopcion);
    }
  }

  @Override
  public List<Adopciones> getAdopcionesByStatus(Boolean status) {
    return data.getAdopcionesByStatus(status);
  }

  @Override
  public List<Adopciones> getAdopcionesByAdoptante(Integer adoptanteId) {
    return data.getAdopcionesByAdoptante(adoptanteId);
  }

  @Override
  public List<Adopciones> getAdopcionesByAnimal(Integer animalId) {
    return data.getAdopcionesByAnimal(animalId);
  }

  @Override
  public List<Adopciones> getAdopcionesByEstadoAdopcion(Integer estadoId) {
    return data.getAdopcionesByEstadoAdopcion(estadoId);
  }

  @Override
  public List<Adopciones> getAdopcionesByFechaSolicitud(LocalDate fechaInicio, LocalDate fechaFin) {
    return data.getAdopcionesByFechaSolicitud(fechaInicio, fechaFin);
  }

  @Override
  public List<Adopciones> getAdopcionesByFechaAprobacion(LocalDate fechaInicio, LocalDate fechaFin) {
    return data.getAdopcionesByFechaAprobacion(fechaInicio, fechaFin);
  }

  // Mantener compatibilidad con método existente
  @Override
  public List<Adopciones> getAdopcionesByEstadoRegistro(boolean b) {
    return data.findByEstadoRegistro(b);
  }

  // Método privado para validaciones
  private void validateAdopcion(Adopciones adopcion) {
    // Validar que el animal esté presente
    if (adopcion.getAnimal() == null || adopcion.getAnimal().getId() == null) {
      throw new IllegalArgumentException("El animal es obligatorio para la adopción");
    }

    // Validar que el adoptante esté presente
    if (adopcion.getAdoptante() == null || adopcion.getAdoptante().getId() == null) {
      throw new IllegalArgumentException("El adoptante es obligatorio para la adopción");
    }

    // Validar que el estado esté presente
    if (adopcion.getEstado() == null || adopcion.getEstado().getId() == null) {
      throw new IllegalArgumentException("El estado de adopción es obligatorio");
    }

    // Validar fecha de solicitud
    if (adopcion.getFechaSolicitud() == null) {
      throw new IllegalArgumentException("La fecha de solicitud es obligatoria");
    }

    // Validar que la fecha de solicitud no sea futura
    if (adopcion.getFechaSolicitud().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de solicitud no puede ser futura");
    }

    // Validar fecha de aprobación si está presente
    if (adopcion.getFechaAprobacion() != null) {
      // La fecha de aprobación no puede ser anterior a la fecha de solicitud
      if (adopcion.getFechaAprobacion().isBefore(adopcion.getFechaSolicitud())) {
        throw new IllegalArgumentException("La fecha de aprobación no puede ser anterior a la fecha de solicitud");
      }

      // La fecha de aprobación no puede ser futura
      if (adopcion.getFechaAprobacion().isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("La fecha de aprobación no puede ser futura");
      }
    }

    // Validar longitud de observaciones
    if (adopcion.getObservaciones() != null && adopcion.getObservaciones().length() > 1000) {
      throw new IllegalArgumentException("Las observaciones no pueden exceder los 1000 caracteres");
    }
  }
}
