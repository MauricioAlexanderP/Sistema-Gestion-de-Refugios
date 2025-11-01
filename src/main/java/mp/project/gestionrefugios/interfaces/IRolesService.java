package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Roles;

import java.util.List;
import java.util.Optional;

public interface IRolesService {

  public List<Roles> getRoles();
  public Optional<Roles> getRolById(Integer id);
  public int saveRol(Roles rol);
  public int updateRol(Roles rol);
  public void deleteRol(Integer id);
  public List<Roles> getRolesByStatus(Boolean status);
}
