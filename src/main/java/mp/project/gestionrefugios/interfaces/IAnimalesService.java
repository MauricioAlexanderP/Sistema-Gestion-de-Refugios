package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Animales;

import java.util.List;
import java.util.Optional;

public interface IAnimalesService {

  public List<Animales> getAnimales();

  public Optional<Animales> getAnimalById(Integer id);

  public int saveAnimal(Animales animal);

  public int updateAnimal(Animales animal);

  public void deleteAnimal(Integer id);

  public List<Animales> getAnimalesByStatus(Boolean status);

  public List<Animales> searchAnimalesByNombre(String nombre);

  public List<Animales> getAnimalesByEspecie(Integer especieId);

  public List<Animales> getAnimalesByEstado(Integer estadoId);
}
