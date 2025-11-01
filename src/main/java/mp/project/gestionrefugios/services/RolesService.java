package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IRolesService;
import mp.project.gestionrefugios.repository.RolesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService implements IRolesService {

  @Autowired
  private RolesRepository data;

  @Override
  public List<Roles> getRoles() {
    return data.findAll();
  }

  @Override
  public Optional<Roles> getRolById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveRol(Roles rol) {
    data.save(rol);
    return rol.getId();
  }

  @Override
  public int updateRol(Roles rol) {
    // Verificar que el rol existe antes de actualizarlo
    if (rol.getId() != null && data.existsById(rol.getId())) {
      data.save(rol);
      return rol.getId();
    }
    return 0; 
  }

  @Override
  public void deleteRol(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Roles> rolOptional = data.findById(id);
    if (rolOptional.isPresent()) {
      Roles rol = rolOptional.get();
      rol.setEstadoRegistro(false);
      data.save(rol);
    }
  }

  @Override
  public List<Roles> getRolesByStatus(Boolean status) {
    return data.getRolesByStatus(status);
  }
}
