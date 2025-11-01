package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mp.project.gestionrefugios.model.CatEstadoAnimal;
import mp.project.gestionrefugios.services.EstadoAnimalService;

@RestController
@RequestMapping("/estadoAnimal")
@CrossOrigin(origins = "*")
public class EstadoAnimalController {

  @Autowired
  private EstadoAnimalService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<CatEstadoAnimal>> getEstadoAnimalesByStatus(@PathVariable Boolean status) {
    try {
      List<CatEstadoAnimal> estadoAnimales = service.getEstadoAnimalesByStatus(status);
      if (estadoAnimales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(estadoAnimales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<CatEstadoAnimal>> getEstadoAnimales() {
    try {
      List<CatEstadoAnimal> estadoAnimales = service.getEstadoAnimales();
      if (estadoAnimales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(estadoAnimales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<CatEstadoAnimal> getEstadoAnimalById(@PathVariable Integer id) {
    try {
      Optional<CatEstadoAnimal> estadoAnimal = service.getEstadoAnimalById(id);
      return estadoAnimal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveEstadoAnimal(@RequestBody CatEstadoAnimal estadoAnimal) {
    try {
      if (estadoAnimal.getNombre() == null || estadoAnimal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del estado animal es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (estadoAnimal.getEstadoRegistro() == null) {
        estadoAnimal.setEstadoRegistro(true);
      }

      int result = service.saveEstadoAnimal(estadoAnimal);
      return result > 0 ? ResponseEntity.ok("Estado animal guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el estado animal");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateEstadoAnimal(@RequestBody CatEstadoAnimal estadoAnimal) {
    try {
      // Validar que el ID esté presente para la actualización
      if (estadoAnimal.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del estado animal es obligatorio para actualizar.");
      }

      if (estadoAnimal.getNombre() == null || estadoAnimal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del estado animal es obligatorio.");
      }

      // Verificar que el estado animal existe antes de actualizar
      Optional<CatEstadoAnimal> existingEstadoAnimal = service.getEstadoAnimalById(estadoAnimal.getId());
      if (!existingEstadoAnimal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (estadoAnimal.getEstadoRegistro() == null) {
        estadoAnimal.setEstadoRegistro(existingEstadoAnimal.get().getEstadoRegistro());
      }

      int result = service.updateEstadoAnimal(estadoAnimal);
      return result > 0 ? ResponseEntity.ok("Estado animal actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el estado animal");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEstadoAnimal(@PathVariable Integer id) {
    try {
      // Verificar que el estado animal existe antes de eliminarlo
      Optional<CatEstadoAnimal> existingEstadoAnimal = service.getEstadoAnimalById(id);
      if (!existingEstadoAnimal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteEstadoAnimal(id);
      return ResponseEntity.ok("Estado animal eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}