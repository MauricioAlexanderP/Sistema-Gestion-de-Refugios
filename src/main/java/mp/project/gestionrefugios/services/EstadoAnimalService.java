package mp.project.gestionrefugios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IEstadoAnimalService;
import mp.project.gestionrefugios.model.CatEstadoAnimal;
import mp.project.gestionrefugios.repository.CatEstadoAnimalRepository;

@Service
public class EstadoAnimalService implements IEstadoAnimalService{

  @Autowired
  private CatEstadoAnimalRepository data;

  @Override
  public List<CatEstadoAnimal> getEstadoAnimalesByStatus(Boolean status) {
    return data.getEstadoAnimalesByStatus(status);
  }

  @Override
  public List<CatEstadoAnimal> getEstadoAnimales() {
    return data.findAll();
  }

  @Override
  public Optional<CatEstadoAnimal> getEstadoAnimalById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveEstadoAnimal(CatEstadoAnimal estadoAnimal) {
    data.save(estadoAnimal);
    return estadoAnimal.getId();
  }

  @Override
  public int updateEstadoAnimal(CatEstadoAnimal estadoAnimal) {
    if(estadoAnimal.getId() != null && data.existsById(estadoAnimal.getId())) {
      data.save(estadoAnimal);
      return estadoAnimal.getId();
    }
    return 0;
  }

  @Override
  public void deleteEstadoAnimal(Integer id) {
    Optional<CatEstadoAnimal> estadoAnimalOptional = data.findById(id);
    if (estadoAnimalOptional.isPresent()) {
      CatEstadoAnimal estadoAnimal = estadoAnimalOptional.get();
      estadoAnimal.setEstadoRegistro(false);
      data.save(estadoAnimal);
    }
  }


}
