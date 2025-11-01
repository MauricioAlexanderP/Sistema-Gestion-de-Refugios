package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Usuarios;

import java.util.List;
import java.util.Optional;

public interface IUsuariosService {

  public List<Usuarios> getUsuarios();

  public Optional<Usuarios> getUsuarioById(Integer id);

  public int saveUsuario(Usuarios usuario);

  public int updateUsuario(Usuarios usuario);

  public void deleteUsuario(Integer id);

  public List<Usuarios> getUsuariosByStatus(Boolean status);

  // Métodos específicos de autenticación
  public Optional<Usuarios> authenticate(String usuario, String contrasena);

  public Optional<Usuarios> findByUsuario(String usuario);

  public Optional<Usuarios> findByEmail(String email);

  public boolean isUsuarioUnique(String usuario);

  public boolean isEmailUnique(String email);

  public boolean isUsuarioUniqueForUpdate(String usuario, Integer id);

  public boolean isEmailUniqueForUpdate(String email, Integer id);
}