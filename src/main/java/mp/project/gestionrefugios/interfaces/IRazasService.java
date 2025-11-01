package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Razas;

import java.util.List;
import java.util.Optional;

public interface IRazasService {

  public List<Razas> getRazas();

  public Optional<Razas> getRazaById(Integer id);

  public int saveRaza(Razas raza);

  public int updateRaza(Razas raza);

  public void deleteRaza(Integer id);

  public List<Razas> getRazasByStatus(Boolean status);

  public List<Razas> getRazasByEspecieId(Integer especieId);
}