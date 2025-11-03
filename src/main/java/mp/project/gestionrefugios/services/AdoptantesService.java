package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Adoptantes;
import mp.project.gestionrefugios.interfaces.IAdoptantesService;
import mp.project.gestionrefugios.repository.AdoptantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AdoptantesService implements IAdoptantesService {

  @Autowired
  private AdoptantesRepository data;

  // Patrón para validar email
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  @Override
  public List<Adoptantes> getAdoptantes() {
    return data.findAll();
  }

  @Override
  public Optional<Adoptantes> getAdoptanteById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveAdoptante(Adoptantes adoptante) {
    // Validaciones
    validateAdoptante(adoptante);

    data.save(adoptante);
    return adoptante.getId();
  }

  @Override
  public int updateAdoptante(Adoptantes adoptante) {
    // Verificar que el adoptante existe antes de actualizarlo
    if (adoptante.getId() != null && data.existsById(adoptante.getId())) {
      // Validaciones
      validateAdoptante(adoptante);

      data.save(adoptante);
      return adoptante.getId();
    }
    return 0;
  }

  @Override
  public void deleteAdoptante(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Adoptantes> adoptanteOptional = data.findById(id);
    if (adoptanteOptional.isPresent()) {
      Adoptantes adoptante = adoptanteOptional.get();
      adoptante.setEstadoRegistro(false);
      data.save(adoptante);
    }
  }

  @Override
  public List<Adoptantes> getAdoptantesByStatus(Boolean status) {
    return data.getAdoptantesByStatus(status);
  }

  @Override
  public List<Adoptantes> searchAdoptantesByNombre(String nombre) {
    return data.searchAdoptantesByNombre(nombre);
  }

  @Override
  public Optional<Adoptantes> getAdoptanteByEmail(String email) {
    return data.getAdoptanteByEmail(email);
  }

  @Override
  public List<Adoptantes> searchAdoptantesByTelefono(String telefono) {
    return data.searchAdoptantesByTelefono(telefono);
  }

  // Método privado para validaciones
  private void validateAdoptante(Adoptantes adoptante) {
    // Validar email si está presente
    if (adoptante.getEmail() != null && !adoptante.getEmail().isEmpty()) {
      if (!EMAIL_PATTERN.matcher(adoptante.getEmail()).matches()) {
        throw new IllegalArgumentException("El formato del email no es válido");
      }

      // Validar que el email no esté duplicado (solo para nuevos registros o si el
      // email cambió)
      Optional<Adoptantes> existingByEmail = data.getAdoptanteByEmail(adoptante.getEmail());
      if (existingByEmail.isPresent()) {
        // Si es un nuevo registro o si el email pertenece a otro adoptante
        if (adoptante.getId() == null || !existingByEmail.get().getId().equals(adoptante.getId())) {
          throw new IllegalArgumentException("Ya existe un adoptante con este email");
        }
      }
    }

    // Validar teléfono si está presente
    if (adoptante.getTelefono() != null && !adoptante.getTelefono().isEmpty()) {
      // Validar que solo contenga números, espacios, guiones y paréntesis
      if (!adoptante.getTelefono().matches("^[0-9\\s\\-\\(\\)\\+]+$")) {
        throw new IllegalArgumentException(
            "El teléfono solo puede contener números, espacios, guiones, paréntesis y el símbolo +");
      }

      // Validar longitud mínima (al menos 7 dígitos)
      String digitsOnly = adoptante.getTelefono().replaceAll("[^0-9]", "");
      if (digitsOnly.length() < 7) {
        throw new IllegalArgumentException("El teléfono debe contener al menos 7 dígitos");
      }
    }

    // Validar longitud de dirección
    if (adoptante.getDireccion() != null && adoptante.getDireccion().length() > 150) {
      throw new IllegalArgumentException("La dirección no puede exceder los 150 caracteres");
    }
  }
}
