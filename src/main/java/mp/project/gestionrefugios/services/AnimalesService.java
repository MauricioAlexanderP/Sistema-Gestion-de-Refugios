package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Animales;
import mp.project.gestionrefugios.interfaces.IAnimalesService;
import mp.project.gestionrefugios.repository.AnimalesRepository;
import mp.project.gestionrefugios.repository.EspeciesRepository;
import mp.project.gestionrefugios.repository.RazasRepository;
import mp.project.gestionrefugios.repository.CatEstadoAnimalRepository;
import mp.project.gestionrefugios.repository.RefugiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalesService implements IAnimalesService {

  @Autowired
  private AnimalesRepository data;

  @Autowired
  private EspeciesRepository especiesRepository;

  @Autowired
  private RazasRepository razasRepository;

  @Autowired
  private CatEstadoAnimalRepository estadoAnimalRepository;

  @Autowired
  private RefugiosRepository refugiosRepository;

  @Override
  public List<Animales> getAnimales() {
    return data.findAll();
  }

  @Override
  public Optional<Animales> getAnimalById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveAnimal(Animales animal) {
    // Validaciones
    validateAnimal(animal);

    data.save(animal);
    return animal.getId();
  }

  @Override
  public int updateAnimal(Animales animal) {
    // Verificar que el animal existe antes de actualizarlo
    if (animal.getId() != null && data.existsById(animal.getId())) {
      // Validaciones
      validateAnimal(animal);

      data.save(animal);
      return animal.getId();
    }
    return 0;
  }

  @Override
  public void deleteAnimal(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Animales> animalOptional = data.findById(id);
    if (animalOptional.isPresent()) {
      Animales animal = animalOptional.get();
      animal.setEstadoRegistro(false);
      data.save(animal);
    }
  }

  @Override
  public List<Animales> getAnimalesByStatus(Boolean status) {
    return data.getAnimalesByStatus(status);
  }

  @Override
  public List<Animales> searchAnimalesByNombre(String nombre) {
    return data.searchAnimalesByNombre(nombre);
  }

  @Override
  public List<Animales> getAnimalesByEspecie(Integer especieId) {
    return data.getAnimalesByEspecie(especieId);
  }

  @Override
  public List<Animales> getAnimalesByEstado(Integer estadoId) {
    return data.getAnimalesByEstado(estadoId);
  }

  // Método privado para validaciones
  private void validateAnimal(Animales animal) {
    // Validar edad (debe ser mayor o igual a 0)
    if (animal.getEdad() != null && animal.getEdad() < 0) {
      throw new IllegalArgumentException("La edad no puede ser negativa");
    }

    // Validar fecha de ingreso (no puede ser futura)
    if (animal.getFechaIngreso() != null && animal.getFechaIngreso().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
    }

    // Validar que la especie existe
    if (animal.getEspecie() != null && animal.getEspecie().getId() != null) {
      if (!especiesRepository.existsById(animal.getEspecie().getId())) {
        throw new IllegalArgumentException("La especie especificada no existe");
      }
    }

    // Validar que la raza existe
    if (animal.getRaza() != null && animal.getRaza().getId() != null) {
      if (!razasRepository.existsById(animal.getRaza().getId())) {
        throw new IllegalArgumentException("La raza especificada no existe");
      }
    }

    // Validar que el estado existe
    if (animal.getEstado() != null && animal.getEstado().getId() != null) {
      if (!estadoAnimalRepository.existsById(animal.getEstado().getId())) {
        throw new IllegalArgumentException("El estado especificado no existe");
      }
    }

    // Validar que el refugio existe
    if (animal.getRefugio() != null && animal.getRefugio().getId() != null) {
      if (!refugiosRepository.existsById(animal.getRefugio().getId())) {
        throw new IllegalArgumentException("El refugio especificado no existe");
      }
    }
  }
}
