package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Cargo;
import mp.project.gestionrefugios.services.CargoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cargos")
@CrossOrigin(origins = "*")
public class CargoController {

  @Autowired
  private CargoService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Cargo>> getCargosByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Cargo> cargos = service.getCargosByStatus(status);
      if (cargos.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(cargos);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Cargo>> getCargos() {
    try {
      List<Cargo> cargos = service.getCargos();
      if (cargos.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(cargos);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cargo> getCargoById(@PathVariable Integer id) {
    try {
      Optional<Cargo> cargo = service.getCargoById(id);
      return cargo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveCargo(@RequestBody Cargo cargo) {
    try {
      if (cargo.getNombre() == null || cargo.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del cargo es obligatorio.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (cargo.getEstadoRegistro() == null) {
        cargo.setEstadoRegistro(true);
      }

      int result = service.saveCargo(cargo);
      return result > 0 ? ResponseEntity.ok("Cargo guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el cargo");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateCargo(@RequestBody Cargo cargo) {
    try {
      // Validar que el ID esté presente para la actualización
      if (cargo.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del cargo es obligatorio para actualizar.");
      }

      if (cargo.getNombre() == null || cargo.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del cargo es obligatorio.");
      }

      // Verificar que el cargo existe antes de actualizar
      Optional<Cargo> existingCargo = service.getCargoById(cargo.getId());
      if (!existingCargo.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (cargo.getEstadoRegistro() == null) {
        cargo.setEstadoRegistro(existingCargo.get().getEstadoRegistro());
      }

      int result = service.updateCargo(cargo);
      return result > 0 ? ResponseEntity.ok("Cargo actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el cargo");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteCargo(@PathVariable Integer id) {
    try {
      // Verificar que el cargo existe antes de eliminarlo
      Optional<Cargo> existingCargo = service.getCargoById(id);
      if (!existingCargo.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteCargo(id);
      return ResponseEntity.ok("Cargo eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}