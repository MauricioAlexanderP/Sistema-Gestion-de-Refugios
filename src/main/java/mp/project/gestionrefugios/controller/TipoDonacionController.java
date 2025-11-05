package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.TipoDonacion;
import mp.project.gestionrefugios.services.TipoDonacionService;

@RestController
@RequestMapping("/tipoDonacion")
@CrossOrigin(origins = "*")
public class TipoDonacionController {

  @Autowired
  private TipoDonacionService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<TipoDonacion>> getTipoDonacionByStatus(@PathVariable("status") Boolean status) {
    try {
      List<TipoDonacion> tipoDonaciones = service.getTipoDonacionByStatus(status);
      if (tipoDonaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoDonaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<TipoDonacion>> getTipoDonaciones() {
    try {
      List<TipoDonacion> tipoDonaciones = service.getTipoDonaciones();
      if (tipoDonaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoDonaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<TipoDonacion> getTipoDonacionById(@PathVariable Integer id) {
    try {
      Optional<TipoDonacion> tipoDonacion = service.getTipoDonacionById(id);
      return tipoDonacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<TipoDonacion>> searchTipoDonacionByNombre(@PathVariable String nombre) {
    try {
      List<TipoDonacion> tipoDonaciones = service.searchTipoDonacionByNombre(nombre);
      if (tipoDonaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoDonaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/exacto/{nombre}")
  public ResponseEntity<TipoDonacion> getTipoDonacionByNombreExacto(@PathVariable String nombre) {
    try {
      Optional<TipoDonacion> tipoDonacion = service.getTipoDonacionByNombreExacto(nombre);
      return tipoDonacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/descripcion/{descripcion}")
  public ResponseEntity<List<TipoDonacion>> searchTipoDonacionByDescripcion(@PathVariable String descripcion) {
    try {
      List<TipoDonacion> tipoDonaciones = service.searchTipoDonacionByDescripcion(descripcion);
      if (tipoDonaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoDonaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveTipoDonacion(@RequestBody TipoDonacion tipoDonacion) {
    try {
      // Validar campos obligatorios
      if (tipoDonacion.getNombre() == null || tipoDonacion.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del tipo de donación es obligatorio.");
      }

      // Validar longitud del nombre
      if (tipoDonacion.getNombre().length() > 50) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 50 caracteres.");
      }

      // Validar longitud de la descripción
      if (tipoDonacion.getDescripcion() != null && tipoDonacion.getDescripcion().length() > 1000) {
        return ResponseEntity.badRequest().body("La descripción no puede exceder los 1000 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (tipoDonacion.getEstadoRegistro() == null) {
        tipoDonacion.setEstadoRegistro(true);
      }

      int result = service.saveTipoDonacion(tipoDonacion);
      return result > 0 ? ResponseEntity.ok("Tipo de donación guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el tipo de donación");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateTipoDonacion(@RequestBody TipoDonacion tipoDonacion) {
    try {
      // Validar que el ID esté presente para la actualización
      if (tipoDonacion.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del tipo de donación es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (tipoDonacion.getNombre() == null || tipoDonacion.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del tipo de donación es obligatorio.");
      }

      // Validar longitud del nombre
      if (tipoDonacion.getNombre().length() > 50) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 50 caracteres.");
      }

      // Validar longitud de la descripción
      if (tipoDonacion.getDescripcion() != null && tipoDonacion.getDescripcion().length() > 1000) {
        return ResponseEntity.badRequest().body("La descripción no puede exceder los 1000 caracteres.");
      }

      // Verificar que el tipo de donación existe antes de actualizar
      Optional<TipoDonacion> existingTipoDonacion = service.getTipoDonacionById(tipoDonacion.getId());
      if (!existingTipoDonacion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (tipoDonacion.getEstadoRegistro() == null) {
        tipoDonacion.setEstadoRegistro(existingTipoDonacion.get().getEstadoRegistro());
      }

      int result = service.updateTipoDonacion(tipoDonacion);
      return result > 0 ? ResponseEntity.ok("Tipo de donación actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el tipo de donación");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteTipoDonacion(@PathVariable Integer id) {
    try {
      // Verificar que el tipo de donación existe antes de eliminarlo
      Optional<TipoDonacion> existingTipoDonacion = service.getTipoDonacionById(id);
      if (!existingTipoDonacion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteTipoDonacion(id);
      return ResponseEntity.ok("Tipo de donación eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}