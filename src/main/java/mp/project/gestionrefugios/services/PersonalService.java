package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Personal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IPersonalService;
import mp.project.gestionrefugios.repository.PersonalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalService implements IPersonalService {

  @Autowired
  private PersonalRepository data;

  @Override
  public List<Personal> getPersonal() {
    return data.findAllWithRelations();
  }

  @Override
  public Optional<Personal> getPersonalById(Integer id) {
    return data.findByIdWithRelations(id);
  }

  @Override
  public int savePersonal(Personal personal) {
    data.save(personal);
    return personal.getId();
  }

  @Override
  public int updatePersonal(Personal personal) {
    // Verificar que el personal existe antes de actualizarlo
    if (personal.getId() != null && data.existsById(personal.getId())) {
      data.save(personal);
      return personal.getId();
    }
    return 0;
  }

  @Override
  public void deletePersonal(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Personal> personalOptional = data.findById(id);
    if (personalOptional.isPresent()) {
      Personal personal = personalOptional.get();
      personal.setEstadoRegistro(false);
      data.save(personal);
    }
  }

  @Override
  public List<Personal> getPersonalByStatus(Boolean status) {
    return data.getPersonalByStatus(status);
  }

  @Override
  public List<Personal> getPersonalByRefugioId(Integer refugioId) {
    return data.getPersonalByRefugioId(refugioId);
  }

  @Override
  public List<Personal> getPersonalByCargoId(Integer cargoId) {
    return data.getPersonalByCargoId(cargoId);
  }

  // Métodos de validación para unicidad
  public boolean isEmailUnique(String email) {
    if (email == null || email.isEmpty()) {
      return true; // Email vacío es válido (opcional)
    }
    return data.findByEmailAndEstadoRegistroTrue(email).isEmpty();
  }

  public boolean isTelefonoUnique(String telefono) {
    if (telefono == null || telefono.isEmpty()) {
      return true; // Teléfono vacío es válido (opcional)
    }
    return data.findByTelefonoAndEstadoRegistroTrue(telefono).isEmpty();
  }

  public boolean isEmailUniqueForUpdate(String email, Integer id) {
    if (email == null || email.isEmpty()) {
      return true; // Email vacío es válido (opcional)
    }
    return data.findByEmailAndIdNotAndEstadoRegistroTrue(email, id).isEmpty();
  }

  public boolean isTelefonoUniqueForUpdate(String telefono, Integer id) {
    if (telefono == null || telefono.isEmpty()) {
      return true; // Teléfono vacío es válido (opcional)
    }
    return data.findByTelefonoAndIdNotAndEstadoRegistroTrue(telefono, id).isEmpty();
  }
}