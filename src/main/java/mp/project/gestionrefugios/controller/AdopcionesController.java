package mp.project.gestionrefugios.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Adopciones;
import mp.project.gestionrefugios.services.AdopcionesService;

@RestController
@RequestMapping("/adopciones")
@CrossOrigin(origins = "*")
public class AdopcionesController {

  @Autowired
  private AdopcionesService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Adopciones>> getAdopcionesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByStatus(status);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Adopciones>> getAdopciones() {
    try {
      List<Adopciones> adopciones = service.getAdopciones();
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Adopciones> getAdopcionById(@PathVariable Integer id) {
    try {
      Optional<Adopciones> adopcion = service.getAdopcionById(id);
      return adopcion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/adoptante/{adoptanteId}")
  public ResponseEntity<List<Adopciones>> getAdopcionesByAdoptante(@PathVariable Integer adoptanteId) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByAdoptante(adoptanteId);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/animal/{animalId}")
  public ResponseEntity<List<Adopciones>> getAdopcionesByAnimal(@PathVariable Integer animalId) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByAnimal(animalId);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/estado/{estadoId}")
  public ResponseEntity<List<Adopciones>> getAdopcionesByEstadoAdopcion(@PathVariable Integer estadoId) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByEstadoAdopcion(estadoId);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/fechaSolicitud")
  public ResponseEntity<List<Adopciones>> getAdopcionesByFechaSolicitud(
      @RequestParam LocalDate fechaInicio,
      @RequestParam LocalDate fechaFin) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByFechaSolicitud(fechaInicio, fechaFin);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/fechaAprobacion")
  public ResponseEntity<List<Adopciones>> getAdopcionesByFechaAprobacion(
      @RequestParam LocalDate fechaInicio,
      @RequestParam LocalDate fechaFin) {
    try {
      List<Adopciones> adopciones = service.getAdopcionesByFechaAprobacion(fechaInicio, fechaFin);
      if (adopciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(adopciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveAdopcion(@RequestBody Adopciones adopcion) {
    try {
      // Validar campos obligatorios
      if (adopcion.getAnimal() == null || adopcion.getAnimal().getId() == null) {
        return ResponseEntity.badRequest().body("El animal es obligatorio para la adopción.");
      }

      if (adopcion.getAdoptante() == null || adopcion.getAdoptante().getId() == null) {
        return ResponseEntity.badRequest().body("El adoptante es obligatorio para la adopción.");
      }

      if (adopcion.getEstado() == null || adopcion.getEstado().getId() == null) {
        return ResponseEntity.badRequest().body("El estado de adopción es obligatorio.");
      }

      if (adopcion.getFechaSolicitud() == null) {
        return ResponseEntity.badRequest().body("La fecha de solicitud es obligatoria.");
      }

      // Validar longitud de observaciones
      if (adopcion.getObservaciones() != null && adopcion.getObservaciones().length() > 1000) {
        return ResponseEntity.badRequest().body("Las observaciones no pueden exceder los 1000 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (adopcion.getEstadoRegistro() == null) {
        adopcion.setEstadoRegistro(true);
      }

      int result = service.saveAdopcion(adopcion);
      return result > 0 ? ResponseEntity.ok("Adopción guardada con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar la adopción");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateAdopcion(@RequestBody Adopciones adopcion) {
    try {
      // Validar que el ID esté presente para la actualización
      if (adopcion.getId() == null) {
        return ResponseEntity.badRequest().body("El ID de la adopción es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (adopcion.getAnimal() == null || adopcion.getAnimal().getId() == null) {
        return ResponseEntity.badRequest().body("El animal es obligatorio para la adopción.");
      }

      if (adopcion.getAdoptante() == null || adopcion.getAdoptante().getId() == null) {
        return ResponseEntity.badRequest().body("El adoptante es obligatorio para la adopción.");
      }

      if (adopcion.getEstado() == null || adopcion.getEstado().getId() == null) {
        return ResponseEntity.badRequest().body("El estado de adopción es obligatorio.");
      }

      if (adopcion.getFechaSolicitud() == null) {
        return ResponseEntity.badRequest().body("La fecha de solicitud es obligatoria.");
      }

      // Validar longitud de observaciones
      if (adopcion.getObservaciones() != null && adopcion.getObservaciones().length() > 1000) {
        return ResponseEntity.badRequest().body("Las observaciones no pueden exceder los 1000 caracteres.");
      }

      // Verificar que la adopción existe antes de actualizar
      Optional<Adopciones> existingAdopcion = service.getAdopcionById(adopcion.getId());
      if (!existingAdopcion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (adopcion.getEstadoRegistro() == null) {
        adopcion.setEstadoRegistro(existingAdopcion.get().getEstadoRegistro());
      }

      int result = service.updateAdopcion(adopcion);
      return result > 0 ? ResponseEntity.ok("Adopción actualizada con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar la adopción");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteAdopcion(@PathVariable Integer id) {
    try {
      // Verificar que la adopción existe antes de eliminarla
      Optional<Adopciones> existingAdopcion = service.getAdopcionById(id);
      if (!existingAdopcion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteAdopcion(id);
      return ResponseEntity.ok("Adopción eliminada lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  // Mantener compatibilidad con endpoints existentes
  @GetMapping("/all")
  public List<Adopciones> getAllAdopciones() {
    return service.getAdopciones();
  }

  @GetMapping("/byEstadoRegistro")
  public List<Adopciones> getAdopcionesByEstadoRegistro(@RequestParam boolean estadoRegistro) {
    return service.getAdopcionesByEstadoRegistro(estadoRegistro);
  }
}
