package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Adopciones;

import java.util.List;
import java.util.Optional;

public interface IAdopcionesService {
  public List<Adopciones> getAdopciones();
  public Optional<Adopciones> getAdopcionByID(int id);
  public int saveAdopciones(Adopciones adopciones);
  public int updateAdopciones(Adopciones adopciones);
  public void deleteAdopciones(int id);
  public List<Adopciones> getAdopcionesByEstadoRegistro(boolean b);
}
