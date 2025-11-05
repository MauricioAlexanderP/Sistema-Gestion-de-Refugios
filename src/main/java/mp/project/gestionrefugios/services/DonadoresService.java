package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Donadores;
import mp.project.gestionrefugios.interfaces.IDonadoresService;
import mp.project.gestionrefugios.repository.DonadoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class DonadoresService implements IDonadoresService {

  @Autowired
  private DonadoresRepository data;

  // Patrón para validar email
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  @Override
  public List<Donadores> getDonadores() {
    return data.findAll();
  }

  @Override
  public Optional<Donadores> getDonadorById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveDonador(Donadores donador) {
    // Validaciones
    validateDonador(donador);

    data.save(donador);
    return donador.getId();
  }

  @Override
  public int updateDonador(Donadores donador) {
    // Verificar que el donador existe antes de actualizarlo
    if (donador.getId() != null && data.existsById(donador.getId())) {
      // Validaciones
      validateDonador(donador);

      data.save(donador);
      return donador.getId();
    }
    return 0;
  }

  @Override
  public void deleteDonador(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Donadores> donadorOptional = data.findById(id);
    if (donadorOptional.isPresent()) {
      Donadores donador = donadorOptional.get();
      donador.setEstadoRegistro(false);
      data.save(donador);
    }
  }

  @Override
  public List<Donadores> getDonadoresByStatus(Boolean status) {
    return data.getDonadoresByStatus(status);
  }

  @Override
  public List<Donadores> searchDonadoresByNombre(String nombre) {
    return data.searchDonadoresByNombre(nombre);
  }

  @Override
  public Optional<Donadores> getDonadorByEmail(String email) {
    return data.getDonadorByEmail(email);
  }

  @Override
  public List<Donadores> searchDonadoresByTelefono(String telefono) {
    return data.searchDonadoresByTelefono(telefono);
  }

  @Override
  public List<Donadores> searchDonadoresByDireccion(String direccion) {
    return data.searchDonadoresByDireccion(direccion);
  }

  // Método privado para validaciones
  private void validateDonador(Donadores donador) {
    // Validar email si está presente
    if (donador.getEmail() != null && !donador.getEmail().isEmpty()) {
      if (!EMAIL_PATTERN.matcher(donador.getEmail()).matches()) {
        throw new IllegalArgumentException("El formato del email no es válido");
      }

      // Validar que el email no esté duplicado (solo para nuevos registros o si el
      // email cambió)
      Optional<Donadores> existingByEmail = data.getDonadorByEmail(donador.getEmail());
      if (existingByEmail.isPresent()) {
        // Si es un nuevo registro o si el email pertenece a otro donador
        if (donador.getId() == null || !existingByEmail.get().getId().equals(donador.getId())) {
          throw new IllegalArgumentException("Ya existe un donador con este email");
        }
      }
    }

    // Validar teléfono si está presente
    if (donador.getTelefono() != null && !donador.getTelefono().isEmpty()) {
      // Remover espacios y caracteres especiales para validar solo números
      String telefonoLimpio = donador.getTelefono().replaceAll("[^0-9]", "");
      if (telefonoLimpio.length() < 7) {
        throw new IllegalArgumentException("El teléfono debe contener al menos 7 dígitos");
      }
    }

    // Validar longitud de dirección
    if (donador.getDireccion() != null && donador.getDireccion().length() > 150) {
      throw new IllegalArgumentException("La dirección no puede exceder los 150 caracteres");
    }
  }
}