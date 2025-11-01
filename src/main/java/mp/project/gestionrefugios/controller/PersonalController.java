package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Personal;
import mp.project.gestionrefugios.services.PersonalService;
import mp.project.gestionrefugios.services.CargoService;
import mp.project.gestionrefugios.services.RefugiosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/personal")
@CrossOrigin(origins = "*")
public class PersonalController {

  @Autowired
  private PersonalService service;

  @Autowired
  private CargoService cargoService;

  @Autowired
  private RefugiosService refugiosService;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Personal>> getPersonalByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Personal> personal = service.getPersonalByStatus(status);
      if (personal.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(personal);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/getByRefugio/{refugioId}")
  public ResponseEntity<List<Personal>> getPersonalByRefugioId(@PathVariable("refugioId") Integer refugioId) {
    try {
      List<Personal> personal = service.getPersonalByRefugioId(refugioId);
      if (personal.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(personal);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/getByCargo/{cargoId}")
  public ResponseEntity<List<Personal>> getPersonalByCargoId(@PathVariable("cargoId") Integer cargoId) {
    try {
      List<Personal> personal = service.getPersonalByCargoId(cargoId);
      if (personal.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(personal);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Personal>> getPersonal() {
    try {
      List<Personal> personal = service.getPersonal();
      if (personal.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(personal);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Personal> getPersonalById(@PathVariable Integer id) {
    try {
      Optional<Personal> personal = service.getPersonalById(id);
      return personal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> savePersonal(@RequestBody Personal personal) {
    try {
      // Validación de campos obligatorios
      if (personal.getNombre() == null || personal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del personal es obligatorio.");
      }

      if (personal.getCargo() == null || personal.getCargo().getId() == null) {
        return ResponseEntity.badRequest().body("El cargo es obligatorio.");
      }

      if (personal.getRefugio() == null || personal.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      // Verificar que el cargo existe
      if (!cargoService.getCargoById(personal.getCargo().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El cargo especificado no existe.");
      }

      // Verificar que el refugio existe
      if (!refugiosService.getRefugioById(personal.getRefugio().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El refugio especificado no existe.");
      }

      // Validar unicidad de email
      if (!service.isEmailUnique(personal.getEmail())) {
        return ResponseEntity.badRequest().body("El email ya está en uso por otro personal activo.");
      }

      // Validar unicidad de teléfono
      if (!service.isTelefonoUnique(personal.getTelefono())) {
        return ResponseEntity.badRequest().body("El teléfono ya está en uso por otro personal activo.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (personal.getEstadoRegistro() == null) {
        personal.setEstadoRegistro(true);
      }

      int result = service.savePersonal(personal);
      return result > 0 ? ResponseEntity.ok("Personal guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el personal");

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updatePersonal(@RequestBody Personal personal) {
    try {
      System.out.println("Recibido JSON para actualizar: " + personal);

      // Validar que el ID esté presente para la actualización
      if (personal.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del personal es obligatorio para actualizar.");
      }

      // Validación de campos obligatorios
      if (personal.getNombre() == null || personal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del personal es obligatorio.");
      }

      if (personal.getCargo() == null || personal.getCargo().getId() == null) {
        return ResponseEntity.badRequest().body("El cargo es obligatorio.");
      }

      if (personal.getRefugio() == null || personal.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      // Verificar que el personal existe antes de actualizar
      Optional<Personal> existingPersonal = service.getPersonalById(personal.getId());
      if (!existingPersonal.isPresent()) {
        return ResponseEntity.status(404).body("El personal con ID " + personal.getId() + " no existe.");
      }

      // Verificar que el cargo existe
      if (!cargoService.getCargoById(personal.getCargo().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El cargo con ID " + personal.getCargo().getId() + " no existe.");
      }

      // Verificar que el refugio existe
      if (!refugiosService.getRefugioById(personal.getRefugio().getId()).isPresent()) {
        return ResponseEntity.badRequest().body("El refugio con ID " + personal.getRefugio().getId() + " no existe.");
      }

      // Validar unicidad de email para actualización
      if (!service.isEmailUniqueForUpdate(personal.getEmail(), personal.getId())) {
        return ResponseEntity.badRequest().body("El email ya está en uso por otro personal activo.");
      }

      // Validar unicidad de teléfono para actualización
      if (!service.isTelefonoUniqueForUpdate(personal.getTelefono(), personal.getId())) {
        return ResponseEntity.badRequest().body("El teléfono ya está en uso por otro personal activo.");
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (personal.getEstadoRegistro() == null) {
        personal.setEstadoRegistro(existingPersonal.get().getEstadoRegistro());
      }

      int result = service.updatePersonal(personal);
      return result > 0 ? ResponseEntity.ok("Personal actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el personal");

    } catch (Exception e) {
      System.err.println("Error al actualizar personal: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deletePersonal(@PathVariable Integer id) {
    try {
      // Verificar que el personal existe antes de eliminarlo
      Optional<Personal> existingPersonal = service.getPersonalById(id);
      if (!existingPersonal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deletePersonal(id);
      return ResponseEntity.ok("Personal eliminado con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

}