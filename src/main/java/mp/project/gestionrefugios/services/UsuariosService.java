package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IUsuariosService;
import mp.project.gestionrefugios.repository.UsuariosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService implements IUsuariosService {

  @Autowired
  private UsuariosRepository data;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public List<Usuarios> getUsuarios() {
    return data.findAllWithRelations();
  }

  @Override
  public Optional<Usuarios> getUsuarioById(Integer id) {
    return data.findByIdWithRelations(id);
  }

  @Override
  public int saveUsuario(Usuarios usuario) {
    // Cifrar la contraseña antes de guardar
    if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
      usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
    }
    data.save(usuario);
    return usuario.getId();
  }

  @Override
  public int updateUsuario(Usuarios usuario) {
    // Verificar que el usuario existe antes de actualizarlo
    if (usuario.getId() != null && data.existsById(usuario.getId())) {
      // Si se proporciona una nueva contraseña, cifrarla
      if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
      } else {
        // Si no se proporciona contraseña, mantener la actual
        Optional<Usuarios> existingUsuario = data.findById(usuario.getId());
        if (existingUsuario.isPresent()) {
          usuario.setContrasena(existingUsuario.get().getContrasena());
        }
      }
      data.save(usuario);
      return usuario.getId();
    }
    return 0;
  }

  @Override
  public void deleteUsuario(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Usuarios> usuarioOptional = data.findById(id);
    if (usuarioOptional.isPresent()) {
      Usuarios usuario = usuarioOptional.get();
      usuario.setEstadoRegistro(false);
      data.save(usuario);
    }
  }

  @Override
  public List<Usuarios> getUsuariosByStatus(Boolean status) {
    return data.getUsuariosByStatus(status);
  }

  @Override
  public Optional<Usuarios> authenticate(String usuario, String contrasena) {
    System.out.println("=== DEBUG AUTHENTICATE ===");
    System.out.println("Usuario buscado: " + usuario);
    System.out.println("Contraseña recibida: " + contrasena);

    Optional<Usuarios> usuarioOptional = data.findByUsuarioAndEstadoRegistroTrue(usuario);
    if (usuarioOptional.isPresent()) {
      Usuarios usuarioEntity = usuarioOptional.get();
      System.out.println("Usuario encontrado en BD: " + usuarioEntity.getUsuario());
      System.out.println("Hash en BD: " + usuarioEntity.getContrasena());

      boolean matches = passwordEncoder.matches(contrasena, usuarioEntity.getContrasena());
      System.out.println("¿Contraseñas coinciden?: " + matches);

      if (matches) {
        return usuarioOptional;
      }
    } else {
      System.out.println("Usuario NO encontrado en BD");
    }
    System.out.println("=========================");
    return Optional.empty();
  }

  @Override
  public Optional<Usuarios> findByUsuario(String usuario) {
    return data.findByUsuarioAndEstadoRegistroTrue(usuario);
  }

  @Override
  public Optional<Usuarios> findByEmail(String email) {
    return data.findByEmailAndEstadoRegistroTrue(email);
  }

  @Override
  public boolean isUsuarioUnique(String usuario) {
    if (usuario == null || usuario.isEmpty()) {
      return false; // Usuario vacío no es válido
    }
    return data.findByUsuarioAndEstadoRegistroTrueList(usuario).isEmpty();
  }

  @Override
  public boolean isEmailUnique(String email) {
    if (email == null || email.isEmpty()) {
      return false; // Email vacío no es válido
    }
    return data.findByEmailAndEstadoRegistroTrueList(email).isEmpty();
  }

  @Override
  public boolean isUsuarioUniqueForUpdate(String usuario, Integer id) {
    if (usuario == null || usuario.isEmpty()) {
      return false; // Usuario vacío no es válido
    }
    return data.findByUsuarioAndIdNotAndEstadoRegistroTrue(usuario, id).isEmpty();
  }

  @Override
  public boolean isEmailUniqueForUpdate(String email, Integer id) {
    if (email == null || email.isEmpty()) {
      return false; // Email vacío no es válido
    }
    return data.findByEmailAndIdNotAndEstadoRegistroTrue(email, id).isEmpty();
  }
}