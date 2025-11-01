package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Refugios;
import mp.project.gestionrefugios.services.RefugiosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/refugios")
@CrossOrigin(origins = "*")
public class RefugiosController {

  @Autowired
  private RefugiosService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Refugios>> getRefugiosByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Refugios> refugios = service.getRefugiosByStatus(status);
      if (refugios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(refugios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Refugios>> getRefugios() {
    try {
      List<Refugios> refugios = service.getRefugios();
      if (refugios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(refugios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Refugios> getRefugioById(@PathVariable Integer id) {
    try {
      Optional<Refugios> refugio = service.getRefugioById(id);
      return refugio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveRefugio(@RequestBody Refugios refugio) {
    try {
      if (refugio.getNombre() == null || refugio.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del refugio es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (refugio.getEstadoRegistro() == null) {
        refugio.setEstadoRegistro(true);
      }

      // Si no se especifica capacidad, establecer 0 por defecto
      if (refugio.getCapacidad() == null) {
        refugio.setCapacidad(0);
      }

      // Si no se especifica estado, establecer "Activo" por defecto
      if (refugio.getEstado() == null || refugio.getEstado().isEmpty()) {
        refugio.setEstado("Activo");
      }

      int result = service.saveRefugio(refugio);
      return result > 0 ? ResponseEntity.ok("Refugio guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el refugio");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateRefugio(@RequestBody Refugios refugio) {
    try {
      System.out.println("Recibido JSON para actualizar: " + refugio);

      // Validar que el ID esté presente para la actualización
      if (refugio.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del refugio es obligatorio para actualizar.");
      }

      if (refugio.getNombre() == null || refugio.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del refugio es obligatorio.");
      }

      // Verificar que el refugio existe antes de actualizar
      Optional<Refugios> existingRefugio = service.getRefugioById(refugio.getId());
      if (!existingRefugio.isPresent()) {
        return ResponseEntity.status(404).body("El refugio con ID " + refugio.getId() + " no existe.");
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (refugio.getEstadoRegistro() == null) {
        refugio.setEstadoRegistro(existingRefugio.get().getEstadoRegistro());
      }

      // Si no se especifica capacidad, mantener el valor actual
      if (refugio.getCapacidad() == null) {
        refugio.setCapacidad(existingRefugio.get().getCapacidad());
      }

      // Si no se especifica estado, mantener el valor actual
      if (refugio.getEstado() == null || refugio.getEstado().isEmpty()) {
        refugio.setEstado(existingRefugio.get().getEstado());
      }

      int result = service.updateRefugio(refugio);
      return result > 0 ? ResponseEntity.ok("Refugio actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el refugio");

    } catch (Exception e) {
      System.err.println("Error al actualizar refugio: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteRefugio(@PathVariable Integer id) {
    try {
      // Verificar que el refugio existe antes de eliminarlo
      Optional<Refugios> existingRefugio = service.getRefugioById(id);
      if (!existingRefugio.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteRefugio(id);
      return ResponseEntity.ok("Refugio eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}