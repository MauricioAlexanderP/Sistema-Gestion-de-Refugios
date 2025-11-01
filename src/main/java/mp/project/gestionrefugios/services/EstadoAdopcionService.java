package mp.project.gestionrefugios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IEstadoAdopcionService;
import mp.project.gestionrefugios.model.CatEstadoAdopcion;
import mp.project.gestionrefugios.repository.CatEstadoAdopcionRepository;

@Service
public class EstadoAdopcionService implements IEstadoAdopcionService {

  @Autowired
  private CatEstadoAdopcionRepository data;

  @Override
  public void deleteEstadoAdopcion(Integer id) {
    Optional<CatEstadoAdopcion> estadoAdopcionOptional = data.findById(id);
    if (estadoAdopcionOptional.isPresent()) {
      CatEstadoAdopcion estadoAdopcion = estadoAdopcionOptional.get();
      estadoAdopcion.setEstadoRegistro(false);
      data.save(estadoAdopcion);
    }
    
  }

  @Override
  public Optional<CatEstadoAdopcion> getEstadoAdopcionById(Integer id) {
    return data.findById(id);
  }

  @Override
  public List<CatEstadoAdopcion> getEstadoAdopcionByStatus(Boolean status) {
    return data.getEstadoAdopcionByStatus(status);
  }

  @Override
  public List<CatEstadoAdopcion> getEstadoAdopciones() {
    return data.findAll();
  }

  @Override
  public int saveEstadoAdopcion(CatEstadoAdopcion estadoAdopcion) {
    data.save(estadoAdopcion);
    return estadoAdopcion.getId();
  }

  @Override
  public int updateEstadoAdopcion(CatEstadoAdopcion estadoAdopcion) {
    data.save(estadoAdopcion);
    return estadoAdopcion.getId();
  }
  

}
