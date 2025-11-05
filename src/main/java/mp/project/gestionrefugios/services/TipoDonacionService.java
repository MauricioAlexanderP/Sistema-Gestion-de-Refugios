package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.TipoDonacion;
import mp.project.gestionrefugios.interfaces.ITipoDonacionService;
import mp.project.gestionrefugios.repository.TipoDonacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDonacionService implements ITipoDonacionService {

  @Autowired
  private TipoDonacionRepository data;

  @Override
  public List<TipoDonacion> getTipoDonaciones() {
    return data.findAll();
  }

  @Override
  public Optional<TipoDonacion> getTipoDonacionById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveTipoDonacion(TipoDonacion tipoDonacion) {
    // Validaciones
    validateTipoDonacion(tipoDonacion);

    data.save(tipoDonacion);
    return tipoDonacion.getId();
  }

  @Override
  public int updateTipoDonacion(TipoDonacion tipoDonacion) {
    // Verificar que el tipo de donación existe antes de actualizarlo
    if (tipoDonacion.getId() != null && data.existsById(tipoDonacion.getId())) {
      // Validaciones
      validateTipoDonacion(tipoDonacion);

      data.save(tipoDonacion);
      return tipoDonacion.getId();
    }
    return 0;
  }

  @Override
  public void deleteTipoDonacion(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<TipoDonacion> tipoDonacionOptional = data.findById(id);
    if (tipoDonacionOptional.isPresent()) {
      TipoDonacion tipoDonacion = tipoDonacionOptional.get();
      tipoDonacion.setEstadoRegistro(false);
      data.save(tipoDonacion);
    }
  }

  @Override
  public List<TipoDonacion> getTipoDonacionByStatus(Boolean status) {
    return data.getTipoDonacionByStatus(status);
  }

  @Override
  public List<TipoDonacion> searchTipoDonacionByNombre(String nombre) {
    return data.searchTipoDonacionByNombre(nombre);
  }

  @Override
  public Optional<TipoDonacion> getTipoDonacionByNombreExacto(String nombre) {
    return data.getTipoDonacionByNombreExacto(nombre);
  }

  @Override
  public List<TipoDonacion> searchTipoDonacionByDescripcion(String descripcion) {
    return data.searchTipoDonacionByDescripcion(descripcion);
  }

  // Método privado para validaciones
  private void validateTipoDonacion(TipoDonacion tipoDonacion) {
    // Validar que el nombre no esté duplicado (solo para nuevos registros o si el
    // nombre cambió)
    if (tipoDonacion.getNombre() != null && !tipoDonacion.getNombre().isEmpty()) {
      Optional<TipoDonacion> existingByNombre = data.getTipoDonacionByNombreExacto(tipoDonacion.getNombre());
      if (existingByNombre.isPresent()) {
        // Si es un nuevo registro o si el nombre pertenece a otro tipo de donación
        if (tipoDonacion.getId() == null || !existingByNombre.get().getId().equals(tipoDonacion.getId())) {
          throw new IllegalArgumentException("Ya existe un tipo de donación con este nombre");
        }
      }
    }

    // Validar longitud de descripción
    if (tipoDonacion.getDescripcion() != null && tipoDonacion.getDescripcion().length() > 1000) {
      throw new IllegalArgumentException("La descripción no puede exceder los 1000 caracteres");
    }
  }
}