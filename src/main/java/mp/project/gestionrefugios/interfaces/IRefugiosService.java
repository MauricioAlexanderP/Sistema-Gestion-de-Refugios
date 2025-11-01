package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Refugios;

import java.util.List;
import java.util.Optional;

public interface IRefugiosService {

  public List<Refugios> getRefugios();

  public Optional<Refugios> getRefugioById(Integer id);

  public int saveRefugio(Refugios refugio);

  public int updateRefugio(Refugios refugio);

  public void deleteRefugio(Integer id);

  public List<Refugios> getRefugiosByStatus(Boolean status);
}