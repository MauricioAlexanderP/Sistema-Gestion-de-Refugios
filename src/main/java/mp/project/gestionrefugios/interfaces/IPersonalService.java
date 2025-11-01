package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Personal;

import java.util.List;
import java.util.Optional;

public interface IPersonalService {

  public List<Personal> getPersonal();

  public Optional<Personal> getPersonalById(Integer id);

  public int savePersonal(Personal personal);

  public int updatePersonal(Personal personal);

  public void deletePersonal(Integer id);

  public List<Personal> getPersonalByStatus(Boolean status);

  public List<Personal> getPersonalByRefugioId(Integer refugioId);

  public List<Personal> getPersonalByCargoId(Integer cargoId);
}