package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Especies;
import mp.project.gestionrefugios.services.EspeciesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/especies")
@CrossOrigin(origins = "*")
public class EspeciesController {

  @Autowired
  private EspeciesService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Especies>> getEspeciesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Especies> especies = service.getEspeciesByStatus(status);
      if (especies.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(especies);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Especies>> getEspecies() {
    try {
      List<Especies> especies = service.getEspecies();
      if (especies.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(especies);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Especies> getEspecieById(@PathVariable Integer id) {
    try {
      Optional<Especies> especie = service.getEspecieById(id);
      return especie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveEspecie(@RequestBody Especies especie) {
    try {
      if (especie.getNombre() == null || especie.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de la especie es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (especie.getEstadoRegistro() == null) {
        especie.setEstadoRegistro(true);
      }

      int result = service.saveEspecie(especie);
      return result > 0 ? ResponseEntity.ok("Especie guardada con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar la especie");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateEspecie(@RequestBody Especies especie) {
    try {
      // Validar que el ID esté presente para la actualización
      if (especie.getId() == null) {
        return ResponseEntity.badRequest().body("El ID de la especie es obligatorio para actualizar.");
      }

      if (especie.getNombre() == null || especie.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de la especie es obligatorio.");
      }

      // Verificar que la especie existe antes de actualizar
      Optional<Especies> existingEspecie = service.getEspecieById(especie.getId());
      if (!existingEspecie.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (especie.getEstadoRegistro() == null) {
        especie.setEstadoRegistro(existingEspecie.get().getEstadoRegistro());
      }

      int result = service.updateEspecie(especie);
      return result > 0 ? ResponseEntity.ok("Especie actualizada con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar la especie");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteEspecie(@PathVariable Integer id) {
    try {
      // Verificar que la especie existe antes de eliminarla
      Optional<Especies> existingEspecie = service.getEspecieById(id);
      if (!existingEspecie.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteEspecie(id);
      return ResponseEntity.ok("Especie eliminada con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}