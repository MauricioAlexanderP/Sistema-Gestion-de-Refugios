package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Usuarios;
import mp.project.gestionrefugios.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UsuariosRepository usuariosRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Usuarios> usuarioOptional = usuariosRepository.findByUsuarioAndEstadoRegistroTrue(username);

    if (usuarioOptional.isEmpty()) {
      throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }

    Usuarios usuario = usuarioOptional.get();

    String roleName = usuario.getRol() != null ? usuario.getRol().getNombre() : "USER";

    return User.builder()
        .username(usuario.getUsuario())
        .password(usuario.getContrasena())
        .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase())))
        .accountExpired(false)
        .accountLocked(!usuario.getEstadoRegistro())
        .credentialsExpired(false)
        .disabled(!usuario.getEstadoRegistro())
        .build();
  }

  public Usuarios findByUsername(String username) {
    Optional<Usuarios> usuarioOptional = usuariosRepository.findByUsuarioAndEstadoRegistroTrue(username);
    return usuarioOptional.orElse(null);
  }
}