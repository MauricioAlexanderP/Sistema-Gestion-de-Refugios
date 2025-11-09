package mp.project.gestionrefugios.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import mp.project.gestionrefugios.model.Animales;
import mp.project.gestionrefugios.model.CatEstadoAnimal;
import mp.project.gestionrefugios.model.Especies;
import mp.project.gestionrefugios.model.Razas;
import mp.project.gestionrefugios.model.Refugios;
import mp.project.gestionrefugios.services.AnimalesService;

@RestController
@RequestMapping("/animales")
@CrossOrigin(origins = "*")
public class AnimalesController {

  @Autowired
  private AnimalesService service;

  // Directorio donde se guardarán las imágenes (inyectado desde application.properties)
  @Value("${app.upload.dir}")
  private String UPLOAD_DIR;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Animales>> getAnimalesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Animales> animales = service.getAnimalesByStatus(status);
      if (animales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(animales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Animales>> getAnimales() {
    try {
      List<Animales> animales = service.getAnimales();
      if (animales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(animales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Animales> getAnimalById(@PathVariable Integer id) {
    try {
      Optional<Animales> animal = service.getAnimalById(id);
      return animal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<Animales>> searchAnimalesByNombre(@PathVariable String nombre) {
    try {
      List<Animales> animales = service.searchAnimalesByNombre(nombre);
      if (animales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(animales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/especie/{especieId}")
  public ResponseEntity<List<Animales>> getAnimalesByEspecie(@PathVariable Integer especieId) {
    try {
      List<Animales> animales = service.getAnimalesByEspecie(especieId);
      if (animales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(animales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/estado/{estadoId}")
  public ResponseEntity<List<Animales>> getAnimalesByEstado(@PathVariable Integer estadoId) {
    try {
      List<Animales> animales = service.getAnimalesByEstado(estadoId);
      if (animales.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(animales);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveAnimal(@RequestBody Animales animal) {
    try {
      // Validar campos obligatorios
      if (animal.getNombre() == null || animal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del animal es obligatorio.");
      }

      if (animal.getFechaIngreso() == null) {
        return ResponseEntity.badRequest().body("La fecha de ingreso es obligatoria.");
      }

      if (animal.getEspecie() == null || animal.getEspecie().getId() == null) {
        return ResponseEntity.badRequest().body("La especie es obligatoria.");
      }

      if (animal.getRaza() == null || animal.getRaza().getId() == null) {
        return ResponseEntity.badRequest().body("La raza es obligatoria.");
      }

      if (animal.getEstado() == null || animal.getEstado().getId() == null) {
        return ResponseEntity.badRequest().body("El estado es obligatorio.");
      }

      if (animal.getRefugio() == null || animal.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      // Validar edad
      if (animal.getEdad() != null && animal.getEdad() < 0) {
        return ResponseEntity.badRequest().body("La edad no puede ser negativa.");
      }

      // Validar fecha de ingreso
      if (animal.getFechaIngreso().isAfter(LocalDate.now())) {
        return ResponseEntity.badRequest().body("La fecha de ingreso no puede ser futura.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (animal.getEstadoRegistro() == null) {
        animal.setEstadoRegistro(true);
      }

      int result = service.saveAnimal(animal);
      return result > 0 ? ResponseEntity.ok("Animal guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el animal");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PostMapping("/save-with-image")
  public ResponseEntity<String> saveAnimalWithImage(
      @RequestParam("nombre") String nombre,
      @RequestParam("sexo") String sexo,
      @RequestParam(value = "edad", required = false) Integer edad,
      @RequestParam("fechaIngreso") String fechaIngreso,
      @RequestParam("especieId") Integer especieId,
      @RequestParam("razaId") Integer razaId,
      @RequestParam("estadoId") Integer estadoId,
      @RequestParam("refugioId") Integer refugioId,
      @RequestParam(value = "observaciones", required = false) String observaciones,
      @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

    try {
      // Crear el objeto Animales
      Animales animal = new Animales();
      animal.setNombre(nombre);
      animal.setSexo(sexo);
      animal.setEdad(edad);
      animal.setFechaIngreso(LocalDate.parse(fechaIngreso));
      animal.setObservaciones(observaciones);
      animal.setEstadoRegistro(true);

      // Configurar las relaciones
      Especies especie = new Especies();
      especie.setId(especieId);
      animal.setEspecie(especie);

      Razas raza = new Razas();
      raza.setId(razaId);
      animal.setRaza(raza);

      CatEstadoAnimal estado = new CatEstadoAnimal();
      estado.setId(estadoId);
      animal.setEstado(estado);

      Refugios refugio = new Refugios();
      refugio.setId(refugioId);
      animal.setRefugio(refugio);

      // Procesar la imagen si se proporcionó
      if (imagen != null && !imagen.isEmpty()) {
        String imagePath = saveImage(imagen);
        animal.setImagen(imagePath);
      }

      int result = service.saveAnimal(animal);
      return result > 0 ? ResponseEntity.ok("Animal guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el animal");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateAnimal(@RequestBody Animales animal) {
    try {
      // Validar que el ID esté presente para la actualización
      if (animal.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del animal es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (animal.getNombre() == null || animal.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del animal es obligatorio.");
      }

      if (animal.getFechaIngreso() == null) {
        return ResponseEntity.badRequest().body("La fecha de ingreso es obligatoria.");
      }

      if (animal.getEspecie() == null || animal.getEspecie().getId() == null) {
        return ResponseEntity.badRequest().body("La especie es obligatoria.");
      }

      if (animal.getRaza() == null || animal.getRaza().getId() == null) {
        return ResponseEntity.badRequest().body("La raza es obligatoria.");
      }

      if (animal.getEstado() == null || animal.getEstado().getId() == null) {
        return ResponseEntity.badRequest().body("El estado es obligatorio.");
      }

      if (animal.getRefugio() == null || animal.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      // Validar edad
      if (animal.getEdad() != null && animal.getEdad() < 0) {
        return ResponseEntity.badRequest().body("La edad no puede ser negativa.");
      }

      // Validar fecha de ingreso
      if (animal.getFechaIngreso().isAfter(LocalDate.now())) {
        return ResponseEntity.badRequest().body("La fecha de ingreso no puede ser futura.");
      }

      // Verificar que el animal existe antes de actualizar
      Optional<Animales> existingAnimal = service.getAnimalById(animal.getId());
      if (!existingAnimal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (animal.getEstadoRegistro() == null) {
        animal.setEstadoRegistro(existingAnimal.get().getEstadoRegistro());
      }

      // Si no se proporciona imagen, mantener la existente
      if (animal.getImagen() == null || animal.getImagen().isEmpty()) {
        animal.setImagen(existingAnimal.get().getImagen());
      }

      int result = service.updateAnimal(animal);
      return result > 0 ? ResponseEntity.ok("Animal actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el animal");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update-with-image/{id}")
  public ResponseEntity<String> updateAnimalWithImage(
      @PathVariable Integer id,
      @RequestParam("nombre") String nombre,
      @RequestParam("sexo") String sexo,
      @RequestParam(value = "edad", required = false) Integer edad,
      @RequestParam("fechaIngreso") String fechaIngreso,
      @RequestParam("especieId") Integer especieId,
      @RequestParam("razaId") Integer razaId,
      @RequestParam("estadoId") Integer estadoId,
      @RequestParam("refugioId") Integer refugioId,
      @RequestParam(value = "observaciones", required = false) String observaciones,
      @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

    try {
      // Verificar que el animal existe
      Optional<Animales> existingAnimal = service.getAnimalById(id);
      if (!existingAnimal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Actualizar el objeto Animales
      Animales animal = existingAnimal.get();
      animal.setNombre(nombre);
      animal.setSexo(sexo);
      animal.setEdad(edad);
      animal.setFechaIngreso(LocalDate.parse(fechaIngreso));
      animal.setObservaciones(observaciones);

      // Configurar las relaciones
      mp.project.gestionrefugios.model.Especies especie = new mp.project.gestionrefugios.model.Especies();
      especie.setId(especieId);
      animal.setEspecie(especie);

      mp.project.gestionrefugios.model.Razas raza = new mp.project.gestionrefugios.model.Razas();
      raza.setId(razaId);
      animal.setRaza(raza);

      mp.project.gestionrefugios.model.CatEstadoAnimal estado = new mp.project.gestionrefugios.model.CatEstadoAnimal();
      estado.setId(estadoId);
      animal.setEstado(estado);

      mp.project.gestionrefugios.model.Refugios refugio = new mp.project.gestionrefugios.model.Refugios();
      refugio.setId(refugioId);
      animal.setRefugio(refugio);

      // Procesar la imagen si se proporcionó una nueva
      if (imagen != null && !imagen.isEmpty()) {
        // Eliminar la imagen anterior si existe
        if (animal.getImagen() != null && !animal.getImagen().isEmpty()) {
          deleteImage(animal.getImagen());
        }
        String imagePath = saveImage(imagen);
        animal.setImagen(imagePath);
      }

      int result = service.updateAnimal(animal);
      return result > 0 ? ResponseEntity.ok("Animal actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el animal");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteAnimal(@PathVariable Integer id) {
    try {
      // Verificar que el animal existe antes de eliminarlo
      Optional<Animales> existingAnimal = service.getAnimalById(id);
      if (!existingAnimal.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteAnimal(id);
      return ResponseEntity.ok("Animal eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @GetMapping("/imagen/{filename}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) {
    try {
      Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
          contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // Método privado para guardar la imagen
  private String saveImage(MultipartFile file) throws IOException {
    // Crear el directorio si no existe
    Path uploadPath = Paths.get(UPLOAD_DIR);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    // Generar un nombre único para el archivo
    String originalFilename = file.getOriginalFilename();
    String fileExtension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

    // Guardar el archivo
    Path filePath = uploadPath.resolve(uniqueFilename);
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return uniqueFilename;
  }

  // Método privado para eliminar la imagen
  private void deleteImage(String filename) {
    try {
      Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      // Log the error but don't throw exception
      System.err.println("Error al eliminar la imagen: " + e.getMessage());
    }
  }
}
