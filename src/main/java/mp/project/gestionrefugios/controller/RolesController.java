package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Roles;
import mp.project.gestionrefugios.services.RolesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
public class RolesController {

  @Autowired
  private RolesService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Roles>> getRolesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Roles> roles = service.getRolesByStatus(status);
      if (roles.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(roles);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Roles>> getRoles() {
    try {
      List<Roles> roles = service.getRoles();
      if (roles.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(roles);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Roles> getRolById(@PathVariable Integer id) {
    try {
      Optional<Roles> rol = service.getRolById(id);
      return rol.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveRole(@RequestBody Roles rol) {
    try {
      if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del rol es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (rol.getEstadoRegistro() == null) {
        rol.setEstadoRegistro(true);
      }

      int result = service.saveRol(rol);
      return result > 0 ? ResponseEntity.ok("Rol guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el rol");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateRol(@RequestBody Roles rol) {
    try {
      // Validar que el ID esté presente para la actualización
      if (rol.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del rol es obligatorio para actualizar.");
      }

      if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del rol es obligatorio.");
      }

      // Verificar que el rol existe antes de actualizar
      Optional<Roles> existingRol = service.getRolById(rol.getId());
      if (!existingRol.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (rol.getEstadoRegistro() == null) {
        rol.setEstadoRegistro(existingRol.get().getEstadoRegistro());
      }

      int result = service.updateRol(rol);
      return result > 0 ? ResponseEntity.ok("Rol actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el rol");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteRol(@PathVariable Integer id) {
    try {
      // Verificar que el rol existe antes de eliminarlo
      Optional<Roles> existingRol = service.getRolById(id);
      if (!existingRol.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteRol(id);
      return ResponseEntity.ok("Rol eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}
