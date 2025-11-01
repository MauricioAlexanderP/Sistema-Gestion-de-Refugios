package mp.project.gestionrefugios.interfaces;

import java.util.List;
import java.util.Optional;

import mp.project.gestionrefugios.model.CatEstadoAdopcion;

public interface IEstadoAdopcionService {

  public List<CatEstadoAdopcion> getEstadoAdopcionByStatus(Boolean status);
  public List<CatEstadoAdopcion> getEstadoAdopciones();
  public Optional<CatEstadoAdopcion> getEstadoAdopcionById(Integer id);
  public int saveEstadoAdopcion(CatEstadoAdopcion estadoAdopcion);
  public int updateEstadoAdopcion(CatEstadoAdopcion estadoAdopcion);
  public void deleteEstadoAdopcion(Integer id);
}
