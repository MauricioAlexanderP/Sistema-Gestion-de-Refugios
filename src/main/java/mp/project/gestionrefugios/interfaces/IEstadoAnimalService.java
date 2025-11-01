package mp.project.gestionrefugios.interfaces;

import java.util.List;
import java.util.Optional;

import mp.project.gestionrefugios.model.CatEstadoAnimal;

public interface IEstadoAnimalService {

  public List<CatEstadoAnimal> getEstadoAnimalesByStatus(Boolean status);
  public List<CatEstadoAnimal> getEstadoAnimales();
  public Optional<CatEstadoAnimal> getEstadoAnimalById(Integer id);
  public int saveEstadoAnimal(CatEstadoAnimal estadoAnimal);
  public int updateEstadoAnimal(CatEstadoAnimal estadoAnimal);
  public void deleteEstadoAnimal(Integer id);
}
