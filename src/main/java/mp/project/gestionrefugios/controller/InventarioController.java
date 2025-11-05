package mp.project.gestionrefugios.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Inventario;
import mp.project.gestionrefugios.services.InventarioService;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

  @Autowired
  private InventarioService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Inventario>> getInventarioByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Inventario> inventarios = service.getInventarioByStatus(status);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Inventario>> getInventarios() {
    try {
      List<Inventario> inventarios = service.getInventarios();
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Inventario> getInventarioById(@PathVariable Integer id) {
    try {
      Optional<Inventario> inventario = service.getInventarioById(id);
      return inventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<Inventario>> searchInventarioByNombre(@PathVariable String nombre) {
    try {
      List<Inventario> inventarios = service.searchInventarioByNombre(nombre);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/exacto/{nombre}")
  public ResponseEntity<Inventario> getInventarioByNombreExacto(@PathVariable String nombre) {
    try {
      Optional<Inventario> inventario = service.getInventarioByNombreExacto(nombre);
      return inventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/tipo/{tipoId}")
  public ResponseEntity<List<Inventario>> getInventarioByTipo(@PathVariable Integer tipoId) {
    try {
      List<Inventario> inventarios = service.getInventarioByTipo(tipoId);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/refugio/{refugioId}")
  public ResponseEntity<List<Inventario>> getInventarioByRefugio(@PathVariable Integer refugioId) {
    try {
      List<Inventario> inventarios = service.getInventarioByRefugio(refugioId);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/tipo/{tipoId}/refugio/{refugioId}")
  public ResponseEntity<List<Inventario>> getInventarioByTipoAndRefugio(
      @PathVariable Integer tipoId, @PathVariable Integer refugioId) {
    try {
      List<Inventario> inventarios = service.getInventarioByTipoAndRefugio(tipoId, refugioId);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  // Endpoints específicos para notificaciones de stock mínimo
  @GetMapping("/stock-bajo")
  public ResponseEntity<List<Inventario>> getInventarioBajoStockMinimo() {
    try {
      List<Inventario> inventarios = service.getInventarioBajoStockMinimo();
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/stock-bajo/refugio/{refugioId}")
  public ResponseEntity<List<Inventario>> getInventarioBajoStockMinimoByRefugio(@PathVariable Integer refugioId) {
    try {
      List<Inventario> inventarios = service.getInventarioBajoStockMinimoByRefugio(refugioId);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/stock-menor/{cantidad}")
  public ResponseEntity<List<Inventario>> getInventarioConStockMenorA(@PathVariable Integer cantidad) {
    try {
      List<Inventario> inventarios = service.getInventarioConStockMenorA(cantidad);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/stock-critico/refugio/{refugioId}")
  public ResponseEntity<List<Inventario>> getInventarioEnStockCritico(@PathVariable Integer refugioId) {
    try {
      List<Inventario> inventarios = service.getInventarioEnStockCritico(refugioId);
      if (inventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(inventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveInventario(@RequestBody Inventario inventario) {
    try {
      // Validar campos obligatorios
      if (inventario.getNombre() == null || inventario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del inventario es obligatorio.");
      }

      if (inventario.getTipo() == null || inventario.getTipo().getId() == null) {
        return ResponseEntity.badRequest().body("El tipo de inventario es obligatorio.");
      }

      if (inventario.getCantidad() == null) {
        return ResponseEntity.badRequest().body("La cantidad es obligatoria.");
      }

      if (inventario.getRefugio() == null || inventario.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      if (inventario.getFechaIngreso() == null) {
        return ResponseEntity.badRequest().body("La fecha de ingreso es obligatoria.");
      }

      // Validar longitud del nombre
      if (inventario.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud de la unidad
      if (inventario.getUnidad() != null && inventario.getUnidad().length() > 20) {
        return ResponseEntity.badRequest().body("La unidad no puede exceder los 20 caracteres.");
      }

      // Validar cantidad
      if (inventario.getCantidad() < 0) {
        return ResponseEntity.badRequest().body("La cantidad no puede ser negativa.");
      }

      // Validar stock mínimo
      if (inventario.getStockMinimo() != null && inventario.getStockMinimo() < 0) {
        return ResponseEntity.badRequest().body("El stock mínimo no puede ser negativo.");
      }

      // Validar fecha de ingreso
      if (inventario.getFechaIngreso().isAfter(LocalDate.now())) {
        return ResponseEntity.badRequest().body("La fecha de ingreso no puede ser futura.");
      }

      // Si no se especifica stock mínimo, establecer 5 por defecto
      if (inventario.getStockMinimo() == null) {
        inventario.setStockMinimo(5);
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (inventario.getEstadoRegistro() == null) {
        inventario.setEstadoRegistro(true);
      }

      int result = service.saveInventario(inventario);
      return result > 0 ? ResponseEntity.ok("Inventario guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el inventario");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateInventario(@RequestBody Inventario inventario) {
    try {
      // Validar que el ID esté presente para la actualización
      if (inventario.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del inventario es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (inventario.getNombre() == null || inventario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del inventario es obligatorio.");
      }

      if (inventario.getTipo() == null || inventario.getTipo().getId() == null) {
        return ResponseEntity.badRequest().body("El tipo de inventario es obligatorio.");
      }

      if (inventario.getCantidad() == null) {
        return ResponseEntity.badRequest().body("La cantidad es obligatoria.");
      }

      if (inventario.getRefugio() == null || inventario.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      if (inventario.getFechaIngreso() == null) {
        return ResponseEntity.badRequest().body("La fecha de ingreso es obligatoria.");
      }

      // Validar longitud del nombre
      if (inventario.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud de la unidad
      if (inventario.getUnidad() != null && inventario.getUnidad().length() > 20) {
        return ResponseEntity.badRequest().body("La unidad no puede exceder los 20 caracteres.");
      }

      // Validar cantidad
      if (inventario.getCantidad() < 0) {
        return ResponseEntity.badRequest().body("La cantidad no puede ser negativa.");
      }

      // Validar stock mínimo
      if (inventario.getStockMinimo() != null && inventario.getStockMinimo() < 0) {
        return ResponseEntity.badRequest().body("El stock mínimo no puede ser negativo.");
      }

      // Validar fecha de ingreso
      if (inventario.getFechaIngreso().isAfter(LocalDate.now())) {
        return ResponseEntity.badRequest().body("La fecha de ingreso no puede ser futura.");
      }

      // Verificar que el inventario existe antes de actualizarlo
      Optional<Inventario> existingInventario = service.getInventarioById(inventario.getId());
      if (!existingInventario.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (inventario.getEstadoRegistro() == null) {
        inventario.setEstadoRegistro(existingInventario.get().getEstadoRegistro());
      }

      // Si no se especifica stock mínimo, mantener el valor actual
      if (inventario.getStockMinimo() == null) {
        inventario.setStockMinimo(existingInventario.get().getStockMinimo());
      }

      int result = service.updateInventario(inventario);
      return result > 0 ? ResponseEntity.ok("Inventario actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el inventario");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteInventario(@PathVariable Integer id) {
    try {
      // Verificar que el inventario existe antes de eliminarlo
      Optional<Inventario> existingInventario = service.getInventarioById(id);
      if (!existingInventario.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteInventario(id);
      return ResponseEntity.ok("Inventario eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  // Endpoint especial para actualizar solo la cantidad (útil para movimientos de
  // inventario)
  @PutMapping("/actualizar-cantidad/{id}")
  public ResponseEntity<String> actualizarCantidad(@PathVariable Integer id, @RequestBody Integer nuevaCantidad) {
    try {
      if (nuevaCantidad == null) {
        return ResponseEntity.badRequest().body("La nueva cantidad es obligatoria.");
      }

      if (nuevaCantidad < 0) {
        return ResponseEntity.badRequest().body("La cantidad no puede ser negativa.");
      }

      Optional<Inventario> inventarioOptional = service.getInventarioById(id);
      if (!inventarioOptional.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      Inventario inventario = inventarioOptional.get();
      inventario.setCantidad(nuevaCantidad);

      int result = service.updateInventario(inventario);
      return result > 0 ? ResponseEntity.ok("Cantidad actualizada con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar la cantidad");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}