package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Adoptantes;

import java.util.List;
import java.util.Optional;

public interface IAdoptantesService {

  public List<Adoptantes> getAdoptantes();

  public Optional<Adoptantes> getAdoptanteById(Integer id);

  public int saveAdoptante(Adoptantes adoptante);

  public int updateAdoptante(Adoptantes adoptante);

  public void deleteAdoptante(Integer id);

  public List<Adoptantes> getAdoptantesByStatus(Boolean status);

  public List<Adoptantes> searchAdoptantesByNombre(String nombre);

  public Optional<Adoptantes> getAdoptanteByEmail(String email);

  public List<Adoptantes> searchAdoptantesByTelefono(String telefono);
}
