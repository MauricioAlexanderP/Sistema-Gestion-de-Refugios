package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Donadores;
import mp.project.gestionrefugios.services.DonadoresService;

@RestController
@RequestMapping("/donadores")
@CrossOrigin(origins = "*")
public class DonadoresController {

  @Autowired
  private DonadoresService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Donadores>> getDonadoresByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Donadores> donadores = service.getDonadoresByStatus(status);
      if (donadores.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donadores);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Donadores>> getDonadores() {
    try {
      List<Donadores> donadores = service.getDonadores();
      if (donadores.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donadores);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Donadores> getDonadorById(@PathVariable Integer id) {
    try {
      Optional<Donadores> donador = service.getDonadorById(id);
      return donador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<Donadores>> searchDonadoresByNombre(@PathVariable String nombre) {
    try {
      List<Donadores> donadores = service.searchDonadoresByNombre(nombre);
      if (donadores.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donadores);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/email/{email}")
  public ResponseEntity<Donadores> getDonadorByEmail(@PathVariable String email) {
    try {
      Optional<Donadores> donador = service.getDonadorByEmail(email);
      return donador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/telefono/{telefono}")
  public ResponseEntity<List<Donadores>> searchDonadoresByTelefono(@PathVariable String telefono) {
    try {
      List<Donadores> donadores = service.searchDonadoresByTelefono(telefono);
      if (donadores.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donadores);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/direccion/{direccion}")
  public ResponseEntity<List<Donadores>> searchDonadoresByDireccion(@PathVariable String direccion) {
    try {
      List<Donadores> donadores = service.searchDonadoresByDireccion(direccion);
      if (donadores.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donadores);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveDonador(@RequestBody Donadores donador) {
    try {
      // Validar campos obligatorios
      if (donador.getNombre() == null || donador.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del donador es obligatorio.");
      }

      // Validar longitud del nombre
      if (donador.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud del email
      if (donador.getEmail() != null && donador.getEmail().length() > 100) {
        return ResponseEntity.badRequest().body("El email no puede exceder los 100 caracteres.");
      }

      // Validar longitud del teléfono
      if (donador.getTelefono() != null && donador.getTelefono().length() > 20) {
        return ResponseEntity.badRequest().body("El teléfono no puede exceder los 20 caracteres.");
      }

      // Validar longitud de la dirección
      if (donador.getDireccion() != null && donador.getDireccion().length() > 150) {
        return ResponseEntity.badRequest().body("La dirección no puede exceder los 150 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (donador.getEstadoRegistro() == null) {
        donador.setEstadoRegistro(true);
      }

      int result = service.saveDonador(donador);
      return result > 0 ? ResponseEntity.ok("Donador guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el donador");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateDonador(@RequestBody Donadores donador) {
    try {
      // Validar que el ID esté presente para la actualización
      if (donador.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del donador es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (donador.getNombre() == null || donador.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del donador es obligatorio.");
      }

      // Validar longitud del nombre
      if (donador.getNombre().length() > 100) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 100 caracteres.");
      }

      // Validar longitud del email
      if (donador.getEmail() != null && donador.getEmail().length() > 100) {
        return ResponseEntity.badRequest().body("El email no puede exceder los 100 caracteres.");
      }

      // Validar longitud del teléfono
      if (donador.getTelefono() != null && donador.getTelefono().length() > 20) {
        return ResponseEntity.badRequest().body("El teléfono no puede exceder los 20 caracteres.");
      }

      // Validar longitud de la dirección
      if (donador.getDireccion() != null && donador.getDireccion().length() > 150) {
        return ResponseEntity.badRequest().body("La dirección no puede exceder los 150 caracteres.");
      }

      // Verificar que el donador existe antes de actualizar
      Optional<Donadores> existingDonador = service.getDonadorById(donador.getId());
      if (!existingDonador.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (donador.getEstadoRegistro() == null) {
        donador.setEstadoRegistro(existingDonador.get().getEstadoRegistro());
      }

      int result = service.updateDonador(donador);
      return result > 0 ? ResponseEntity.ok("Donador actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el donador");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteDonador(@PathVariable Integer id) {
    try {
      // Verificar que el donador existe antes de eliminarlo
      Optional<Donadores> existingDonador = service.getDonadorById(id);
      if (!existingDonador.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteDonador(id);
      return ResponseEntity.ok("Donador eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}