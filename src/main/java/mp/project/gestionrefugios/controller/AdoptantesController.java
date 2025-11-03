package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Adoptantes;
import mp.project.gestionrefugios.services.AdoptantesService;

@RestController
@RequestMapping("/adoptantes")
@CrossOrigin(origins = "*")
public class AdoptantesController {

  @Autowired
  private AdoptantesService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Adoptantes>> getAdoptantesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Adoptantes> adoptantes = service.getAdoptantesByStatus(status);
      if (adoptantes.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adoptantes);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Adoptantes>> getAdoptantes() {
    try {
      List<Adoptantes> adoptantes = service.getAdoptantes();
      if (adoptantes.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adoptantes);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Adoptantes> getAdoptanteById(@PathVariable Integer id) {
    try {
      Optional<Adoptantes> adoptante = service.getAdoptanteById(id);
      return adoptante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<Adoptantes>> searchAdoptantesByNombre(@PathVariable String nombre) {
    try {
      List<Adoptantes> adoptantes = service.searchAdoptantesByNombre(nombre);
      if (adoptantes.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adoptantes);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/email/{email}")
  public ResponseEntity<Adoptantes> getAdoptanteByEmail(@PathVariable String email) {
    try {
      Optional<Adoptantes> adoptante = service.getAdoptanteByEmail(email);
      return adoptante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/telefono/{telefono}")
  public ResponseEntity<List<Adoptantes>> searchAdoptantesByTelefono(@PathVariable String telefono) {
    try {
      List<Adoptantes> adoptantes = service.searchAdoptantesByTelefono(telefono);
      if (adoptantes.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adoptantes);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveAdoptante(@RequestBody Adoptantes adoptante) {
    try {
      // Validar campos obligatorios
      if (adoptante.getNombre() == null || adoptante.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del adoptante es obligatorio.");
      }

      // Validar longitud del nombre
      if (adoptante.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud del email
      if (adoptante.getEmail() != null && adoptante.getEmail().length() > 100) {
        return ResponseEntity.badRequest().body("El email no puede exceder los 100 caracteres.");
      }

      // Validar longitud del teléfono
      if (adoptante.getTelefono() != null && adoptante.getTelefono().length() > 20) {
        return ResponseEntity.badRequest().body("El teléfono no puede exceder los 20 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (adoptante.getEstadoRegistro() == null) {
        adoptante.setEstadoRegistro(true);
      }

      int result = service.saveAdoptante(adoptante);
      return result > 0 ? ResponseEntity.ok("Adoptante guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el adoptante");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateAdoptante(@RequestBody Adoptantes adoptante) {
    try {
      // Validar que el ID esté presente para la actualización
      if (adoptante.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del adoptante es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (adoptante.getNombre() == null || adoptante.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del adoptante es obligatorio.");
      }

      // Validar longitud del nombre
      if (adoptante.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud del email
      if (adoptante.getEmail() != null && adoptante.getEmail().length() > 100) {
        return ResponseEntity.badRequest().body("El email no puede exceder los 100 caracteres.");
      }

      // Validar longitud del teléfono
      if (adoptante.getTelefono() != null && adoptante.getTelefono().length() > 20) {
        return ResponseEntity.badRequest().body("El teléfono no puede exceder los 20 caracteres.");
      }

      // Verificar que el adoptante existe antes de actualizar
      Optional<Adoptantes> existingAdoptante = service.getAdoptanteById(adoptante.getId());
      if (!existingAdoptante.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (adoptante.getEstadoRegistro() == null) {
        adoptante.setEstadoRegistro(existingAdoptante.get().getEstadoRegistro());
      }

      int result = service.updateAdoptante(adoptante);
      return result > 0 ? ResponseEntity.ok("Adoptante actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el adoptante");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteAdoptante(@PathVariable Integer id) {
    try {
      // Verificar que el adoptante existe antes de eliminarlo
      Optional<Adoptantes> existingAdoptante = service.getAdoptanteById(id);
      if (!existingAdoptante.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteAdoptante(id);
      return ResponseEntity.ok("Adoptante eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}
