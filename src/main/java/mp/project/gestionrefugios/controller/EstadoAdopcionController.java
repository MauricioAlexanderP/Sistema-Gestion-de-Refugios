package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mp.project.gestionrefugios.model.CatEstadoAdopcion;
import mp.project.gestionrefugios.services.EstadoAdopcionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/estadoAdopcion")
@CrossOrigin(origins = "*")
public class EstadoAdopcionController {

  @Autowired
  private EstadoAdopcionService service;

  @GetMapping("/list")
  public ResponseEntity<List<CatEstadoAdopcion>> getEstadoAdopcione() {
    try {
      List<CatEstadoAdopcion> estados = service.getEstadoAdopciones();
      if (estados.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(estados);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<CatEstadoAdopcion>> getEstadoAdopcionByStatus(@PathVariable("status") Boolean status) {
    try {
      List<CatEstadoAdopcion> estados = service.getEstadoAdopcionByStatus(status);
      if (estados.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(estados);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<CatEstadoAdopcion> getEstadoAdopcionById(@PathVariable Integer id) {
    try {
      var estado = service.getEstadoAdopcionById(id);
      return estado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveEstadoAdopcion(@RequestBody CatEstadoAdopcion estadoAdopcion) {
    try {
      if (estadoAdopcion.getNombre() == null || estadoAdopcion.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del estado de adopción es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (estadoAdopcion.getEstadoRegistro() == null) {
        estadoAdopcion.setEstadoRegistro(true);
      }

      int result = service.saveEstadoAdopcion(estadoAdopcion);
      return result > 0 ? ResponseEntity.ok("Estado de adopción guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el estado de adopción.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateEstadoAdopcion(@RequestBody CatEstadoAdopcion estadoAdopcion) {
    try {
      // Validar que el ID esté presente para la actualización
      if (estadoAdopcion.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del estado de adopción es obligatorio para actualizar.");
      }

      if (estadoAdopcion.getNombre() == null || estadoAdopcion.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del estado de adopción es obligatorio.");
      }

      // Verificar que el estado adopción existe antes de actualizar
      Optional<CatEstadoAdopcion> existingEstadoAdopcion = service.getEstadoAdopcionById(estadoAdopcion.getId());
      if (!existingEstadoAdopcion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (estadoAdopcion.getEstadoRegistro() == null) {
        estadoAdopcion.setEstadoRegistro(existingEstadoAdopcion.get().getEstadoRegistro());
      }

      int result = service.updateEstadoAdopcion(estadoAdopcion);
      return result > 0 ? ResponseEntity.ok("Estado de adopción actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el estado de adopción.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEstadoAdopcion(@PathVariable Integer id) {
    try {
      // Verificar que el estado adopción existe antes de eliminarlo
      Optional<CatEstadoAdopcion> estadoAdopcion = service.getEstadoAdopcionById(id);
      if (!estadoAdopcion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteEstadoAdopcion(id);
      return ResponseEntity.ok("Estado de adopción eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}
