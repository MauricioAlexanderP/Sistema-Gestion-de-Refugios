package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Inventario;
import mp.project.gestionrefugios.interfaces.IInventarioService;
import mp.project.gestionrefugios.repository.InventarioRepository;
import mp.project.gestionrefugios.repository.TipoInventarioRepository;
import mp.project.gestionrefugios.repository.RefugiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService implements IInventarioService {

  @Autowired
  private InventarioRepository data;

  @Autowired
  private TipoInventarioRepository tipoInventarioRepository;

  @Autowired
  private RefugiosRepository refugiosRepository;

  @Override
  public List<Inventario> getInventarios() {
    return data.findAll();
  }

  @Override
  public Optional<Inventario> getInventarioById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveInventario(Inventario inventario) {
    // Validaciones
    validateInventario(inventario);

    data.save(inventario);
    return inventario.getId();
  }

  @Override
  public int updateInventario(Inventario inventario) {
    // Verificar que el inventario existe antes de actualizarlo
    if (inventario.getId() != null && data.existsById(inventario.getId())) {
      // Validaciones
      validateInventario(inventario);

      data.save(inventario);
      return inventario.getId();
    }
    return 0;
  }

  @Override
  public void deleteInventario(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Inventario> inventarioOptional = data.findById(id);
    if (inventarioOptional.isPresent()) {
      Inventario inventario = inventarioOptional.get();
      inventario.setEstadoRegistro(false);
      data.save(inventario);
    }
  }

  @Override
  public List<Inventario> getInventarioByStatus(Boolean status) {
    return data.getInventarioByStatus(status);
  }

  @Override
  public List<Inventario> searchInventarioByNombre(String nombre) {
    return data.searchInventarioByNombre(nombre);
  }

  @Override
  public Optional<Inventario> getInventarioByNombreExacto(String nombre) {
    return data.getInventarioByNombreExacto(nombre);
  }

  @Override
  public List<Inventario> getInventarioByTipo(Integer tipoId) {
    return data.getInventarioByTipo(tipoId);
  }

  @Override
  public List<Inventario> getInventarioByRefugio(Integer refugioId) {
    return data.getInventarioByRefugio(refugioId);
  }

  @Override
  public List<Inventario> getInventarioByTipoAndRefugio(Integer tipoId, Integer refugioId) {
    return data.getInventarioByTipoAndRefugio(tipoId, refugioId);
  }

  @Override
  public List<Inventario> getInventarioBajoStockMinimo() {
    return data.getInventarioBajoStockMinimo();
  }

  @Override
  public List<Inventario> getInventarioBajoStockMinimoByRefugio(Integer refugioId) {
    return data.getInventarioBajoStockMinimoByRefugio(refugioId);
  }

  @Override
  public List<Inventario> getInventarioConStockMenorA(Integer cantidad) {
    return data.getInventarioConStockMenorA(cantidad);
  }

  @Override
  public List<Inventario> getInventarioEnStockCritico(Integer refugioId) {
    return data.getInventarioEnStockCritico(refugioId);
  }

  // Método privado para validaciones
  private void validateInventario(Inventario inventario) {
    // Validar que el nombre no esté duplicado en el mismo refugio y tipo
    if (inventario.getNombre() != null && !inventario.getNombre().isEmpty()) {
      Optional<Inventario> existingByNombre = data.getInventarioByNombreExacto(inventario.getNombre());
      if (existingByNombre.isPresent()) {
        // Si es un nuevo registro o si el nombre pertenece a otro inventario
        if (inventario.getId() == null || !existingByNombre.get().getId().equals(inventario.getId())) {
          // Verificar si están en el mismo refugio y tipo
          if (existingByNombre.get().getRefugio().getId().equals(inventario.getRefugio().getId()) &&
              existingByNombre.get().getTipo().getId().equals(inventario.getTipo().getId())) {
            throw new IllegalArgumentException(
                "Ya existe un elemento de inventario con este nombre en el mismo refugio y tipo");
          }
        }
      }
    }

    // Validar que el tipo de inventario existe
    if (inventario.getTipo() != null && inventario.getTipo().getId() != null) {
      if (!tipoInventarioRepository.existsById(inventario.getTipo().getId())) {
        throw new IllegalArgumentException("El tipo de inventario especificado no existe");
      }
    }

    // Validar que el refugio existe
    if (inventario.getRefugio() != null && inventario.getRefugio().getId() != null) {
      if (!refugiosRepository.existsById(inventario.getRefugio().getId())) {
        throw new IllegalArgumentException("El refugio especificado no existe");
      }
    }

    // Validar cantidad
    if (inventario.getCantidad() != null && inventario.getCantidad() < 0) {
      throw new IllegalArgumentException("La cantidad no puede ser negativa");
    }

    // Validar stock mínimo
    if (inventario.getStockMinimo() != null && inventario.getStockMinimo() < 0) {
      throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
    }

    // Validar longitud del nombre
    if (inventario.getNombre() != null && inventario.getNombre().length() > 100) {
      throw new IllegalArgumentException("El nombre no puede exceder los 100 caracteres");
    }

    // Validar longitud de la unidad
    if (inventario.getUnidad() != null && inventario.getUnidad().length() > 20) {
      throw new IllegalArgumentException("La unidad no puede exceder los 20 caracteres");
    }

    // Validar fecha de ingreso no futura
    if (inventario.getFechaIngreso() != null && inventario.getFechaIngreso().isAfter(java.time.LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
    }
  }
}