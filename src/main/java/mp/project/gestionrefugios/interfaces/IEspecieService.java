package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Especies;

import java.util.List;
import java.util.Optional;

public interface IEspecieService {

  public List<Especies> getEspecies();

  public Optional<Especies> getEspecieById(Integer id);

  public int saveEspecie(Especies especie);

  public int updateEspecie(Especies especie);

  public void deleteEspecie(Integer id);

  public List<Especies> getEspeciesByStatus(Boolean status);
}