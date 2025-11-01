package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.dto.LoginRequest;
import mp.project.gestionrefugios.dto.LoginResponse;
import mp.project.gestionrefugios.model.Usuarios;
import mp.project.gestionrefugios.services.UsuariosService;
import mp.project.gestionrefugios.services.RolesService;
import mp.project.gestionrefugios.services.PersonalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuariosController {

  @Autowired
  private UsuariosService service;

  @Autowired
  private RolesService rolesService;

  @Autowired
  private PersonalService personalService;

  // Endpoint de autenticación
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    try {
      if (loginRequest.getUsuario() == null || loginRequest.getUsuario().isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new LoginResponse(false, "El nombre de usuario es obligatorio."));
      }

      if (loginRequest.getContrasena() == null || loginRequest.getContrasena().isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new LoginResponse(false, "La contraseña es obligatoria."));
      }

      Optional<Usuarios> usuarioOptional = service.authenticate(loginRequest.getUsuario(),
          loginRequest.getContrasena());

      if (usuarioOptional.isPresent()) {
        LoginResponse.UsuarioDto usuarioDto = new LoginResponse.UsuarioDto(usuarioOptional.get());
        return ResponseEntity.ok(new LoginResponse(true, "Autenticación exitosa.", usuarioDto));
      } else {
        return ResponseEntity.status(401)
            .body(new LoginResponse(false, "Usuario o contraseña incorrectos."));
      }

    } catch (Exception e) {
      return ResponseEntity.status(500)
          .body(new LoginResponse(false, "Error interno del servidor: " + e.getMessage()));
    }
  }

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<UsuarioResponse>> getUsuariosByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Usuarios> usuarios = service.getUsuariosByStatus(status);
      if (usuarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      // Convertir a DTO para no exponer contraseñas
      List<UsuarioResponse> usuariosResponse = usuarios.stream()
          .map(UsuarioResponse::new)
          .toList();
      return ResponseEntity.ok(usuariosResponse);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<UsuarioResponse>> getUsuarios() {
    try {
      List<Usuarios> usuarios = service.getUsuarios();
      if (usuarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      // Convertir a DTO para no exponer contraseñas
      List<UsuarioResponse> usuariosResponse = usuarios.stream()
          .map(UsuarioResponse::new)
          .toList();
      return ResponseEntity.ok(usuariosResponse);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Integer id) {
    try {
      Optional<Usuarios> usuario = service.getUsuarioById(id);
      return usuario.map(u -> ResponseEntity.ok(new UsuarioResponse(u)))
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/findByUsuario/{usuario}")
  public ResponseEntity<UsuarioResponse> findByUsuario(@PathVariable String usuario) {
    try {
      Optional<Usuarios> usuarioOptional = service.findByUsuario(usuario);
      return usuarioOptional.map(u -> ResponseEntity.ok(new UsuarioResponse(u)))
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveUsuario(@RequestBody Usuarios usuario) {
    try {
      // Validación de campos obligatorios
      if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del usuario es obligatorio.");
      }

      if (usuario.getUsuario() == null || usuario.getUsuario().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de usuario es obligatorio.");
      }

      if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
        return ResponseEntity.badRequest().body("La contraseña es obligatoria.");
      }

      if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
        return ResponseEntity.badRequest().body("El email es obligatorio.");
      }

      if (usuario.getRol() == null || usuario.getRol().getId() == null) {
        return ResponseEntity.badRequest().body("El rol es obligatorio.");
      }

      // Verificar que el rol existe
      if (!rolesService.getRolById(usuario.getRol().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El rol especificado no existe.");
      }

      // Verificar que el personal existe (si se especifica)
      if (usuario.getPersonal() != null && usuario.getPersonal().getId() != null) {
        if (!personalService.getPersonalById(usuario.getPersonal().getId()).isPresent()) {
          return ResponseEntity.badRequest().body("El personal especificado no existe.");
        }
      }

      // Validar unicidad de usuario
      if (!service.isUsuarioUnique(usuario.getUsuario())) {
        return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
      }

      // Validar unicidad de email
      if (!service.isEmailUnique(usuario.getEmail())) {
        return ResponseEntity.badRequest().body("El email ya está en uso.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (usuario.getEstadoRegistro() == null) {
        usuario.setEstadoRegistro(true);
      }

      int result = service.saveUsuario(usuario);
      return result > 0 ? ResponseEntity.ok("Usuario guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el usuario");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateUsuario(@RequestBody Usuarios usuario) {
    try {
      System.out.println("Recibido JSON para actualizar: " + usuario);

      // Validar que el ID esté presente para la actualización
      if (usuario.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del usuario es obligatorio para actualizar.");
      }

      // Validación de campos obligatorios
      if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del usuario es obligatorio.");
      }

      if (usuario.getUsuario() == null || usuario.getUsuario().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de usuario es obligatorio.");
      }

      if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
        return ResponseEntity.badRequest().body("El email es obligatorio.");
      }

      if (usuario.getRol() == null || usuario.getRol().getId() == null) {
        return ResponseEntity.badRequest().body("El rol es obligatorio.");
      }

      // Verificar que el usuario existe antes de actualizar
      Optional<Usuarios> existingUsuario = service.getUsuarioById(usuario.getId());
      if (!existingUsuario.isPresent()) {
        return ResponseEntity.status(404).body("El usuario con ID " + usuario.getId() + " no existe.");
      }

      // Verificar que el rol existe
      if (!rolesService.getRolById(usuario.getRol().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El rol con ID " + usuario.getRol().getId() + " no existe.");
      }

      // Verificar que el personal existe (si se especifica)
      if (usuario.getPersonal() != null && usuario.getPersonal().getId() != null) {
        if (!personalService.getPersonalById(usuario.getPersonal().getId()).isPresent()) {
          return ResponseEntity.badRequest()
              .body("El personal con ID " + usuario.getPersonal().getId() + " no existe.");
        }
      }

      // Validar unicidad de usuario para actualización
      if (!service.isUsuarioUniqueForUpdate(usuario.getUsuario(), usuario.getId())) {
        return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso por otro usuario activo.");
      }

      // Validar unicidad de email para actualización
      if (!service.isEmailUniqueForUpdate(usuario.getEmail(), usuario.getId())) {
        return ResponseEntity.badRequest().body("El email ya está en uso por otro usuario activo.");
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (usuario.getEstadoRegistro() == null) {
        usuario.setEstadoRegistro(existingUsuario.get().getEstadoRegistro());
      }

      int result = service.updateUsuario(usuario);
      return result > 0 ? ResponseEntity.ok("Usuario actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el usuario");

    } catch (Exception e) {
      System.err.println("Error al actualizar usuario: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUsuario(@PathVariable Integer id) {
    try {
      // Verificar que el usuario existe antes de eliminarlo
      Optional<Usuarios> existingUsuario = service.getUsuarioById(id);
      if (!existingUsuario.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteUsuario(id);
      return ResponseEntity.ok("Usuario eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  // Clase interna para respuesta sin contraseña
  public static class UsuarioResponse {
    private Integer id;
    private String nombre;
    private String usuario;
    private String email;
    private String rol;
    private String personal;
    private Boolean estadoRegistro;

    public UsuarioResponse(Usuarios usuario) {
      this.id = usuario.getId();
      this.nombre = usuario.getNombre();
      this.usuario = usuario.getUsuario();
      this.email = usuario.getEmail();
      this.rol = usuario.getRol() != null ? usuario.getRol().getNombre() : null;
      this.personal = usuario.getPersonal() != null ? usuario.getPersonal().getNombre() : null;
      this.estadoRegistro = usuario.getEstadoRegistro();
    }

    // Getters
    public Integer getId() {
      return id;
    }

    public String getNombre() {
      return nombre;
    }

    public String getUsuario() {
      return usuario;
    }

    public String getEmail() {
      return email;
    }

    public String getRol() {
      return rol;
    }

    public String getPersonal() {
      return personal;
    }

    public Boolean getEstadoRegistro() {
      return estadoRegistro;
    }
  }

}