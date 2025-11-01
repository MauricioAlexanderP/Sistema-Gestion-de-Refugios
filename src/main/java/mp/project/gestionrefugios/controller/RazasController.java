package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Razas;
import mp.project.gestionrefugios.services.RazasService;
import mp.project.gestionrefugios.services.EspeciesService;

@RestController
@RequestMapping("/razas")
@CrossOrigin(origins = "*")
public class RazasController {

  @Autowired
  private RazasService service;

  @Autowired
  private EspeciesService especiesService;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Razas>> getRazasByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Razas> razas = service.getRazasByStatus(status);
      if (razas.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(razas);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/getByEspecie/{especieId}")
  public ResponseEntity<List<Razas>> getRazasByEspecieId(@PathVariable("especieId") Integer especieId) {
    try {
      List<Razas> razas = service.getRazasByEspecieId(especieId);
      if (razas.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(razas);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Razas>> getRazas() {
    try {
      List<Razas> razas = service.getRazas();
      if (razas.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(razas);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Razas> getRazaById(@PathVariable Integer id) {
    try {
      Optional<Razas> raza = service.getRazaById(id);
      return raza.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveRaza(@RequestBody Razas raza) {
    try {
      if (raza.getNombre() == null || raza.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de la raza es obligatorio.");
      }

      if (raza.getEspecie() == null || raza.getEspecie().getId() == null) {
        return ResponseEntity.badRequest().body("La especie es obligatoria.");
      }

      // Verificar que la especie existe
      if (!especiesService.getEspecieById(raza.getEspecie().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("La especie especificada no existe.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (raza.getEstadoRegistro() == null) {
        raza.setEstadoRegistro(true);
      }

      int result = service.saveRaza(raza);
      return result > 0 ? ResponseEntity.ok("Raza guardada con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar la raza");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateRaza(@RequestBody Razas raza) {
    try {
      System.out.println("Recibido JSON para actualizar: " + raza);

      // Validar que el ID esté presente para la actualización
      if (raza.getId() == null) {
        return ResponseEntity.badRequest().body("El ID de la raza es obligatorio para actualizar.");
      }

      if (raza.getNombre() == null || raza.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de la raza es obligatorio.");
      }

      if (raza.getEspecie() == null || raza.getEspecie().getId() == null) {
        return ResponseEntity.badRequest().body("La especie es obligatoria.");
      }

      // Verificar que la raza existe antes de actualizar
      Optional<Razas> existingRaza = service.getRazaById(raza.getId());
      if (!existingRaza.isPresent()) {
        return ResponseEntity.status(404).body("La raza con ID " + raza.getId() + " no existe.");
      }

      // Verificar que la especie existe
      if (!especiesService.getEspecieById(raza.getEspecie().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("La especie con ID " + raza.getEspecie().getId() + " no existe.");
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (raza.getEstadoRegistro() == null) {
        raza.setEstadoRegistro(existingRaza.get().getEstadoRegistro());
      }

      int result = service.updateRaza(raza);
      return result > 0 ? ResponseEntity.ok("Raza actualizada con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar la raza");

    } catch (Exception e) {
      System.err.println("Error al actualizar raza: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteRaza(@PathVariable Integer id) {
    try {
      // Verificar que la raza existe antes de eliminarla
      Optional<Razas> existingRaza = service.getRazaById(id);
      if (!existingRaza.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteRaza(id);
      return ResponseEntity.ok("Raza eliminada con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}